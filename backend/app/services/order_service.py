import uuid
from datetime import datetime, timezone
from typing import List, Optional
from fastapi import HTTPException, status
from sqlalchemy import select, update
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy.orm import selectinload

from app.models.address import Address
from app.models.cart import Cart, CartItem
from app.models.coupon import Coupon, DiscountType
from app.models.order import Order, OrderItem, OrderStatus, OrderTrackingEvent, PaymentStatus
from app.models.payment import Payment
from app.models.product import Product, ProductStatus
from app.models.user import User
from app.schemas.order import OrderCreateRequest

# Allowed state machine transitions
ALLOWED_TRANSITIONS = {
    OrderStatus.ORDER_PLACED: [OrderStatus.PAYMENT_CONFIRMED, OrderStatus.PREPARING, OrderStatus.CANCELLED],
    OrderStatus.PAYMENT_CONFIRMED: [OrderStatus.PREPARING, OrderStatus.CANCELLED],
    OrderStatus.PREPARING: [OrderStatus.READY_FOR_PICKUP, OrderStatus.CANCELLED],
    OrderStatus.READY_FOR_PICKUP: [OrderStatus.PICKED_UP, OrderStatus.CANCELLED],
    OrderStatus.PICKED_UP: [OrderStatus.IN_TRANSIT],
    OrderStatus.IN_TRANSIT: [OrderStatus.OUT_FOR_DELIVERY],
    OrderStatus.OUT_FOR_DELIVERY: [OrderStatus.DELIVERED, OrderStatus.CANCELLED],
    OrderStatus.DELIVERED: [OrderStatus.REFUNDED],
    OrderStatus.CANCELLED: [OrderStatus.REFUNDED],
    OrderStatus.REFUNDED: [],
}

# Seller allowed updates
SELLER_ALLOWED_TRANSITIONS = [
    OrderStatus.PREPARING,
    OrderStatus.READY_FOR_PICKUP,
]


class OrderService:
    @staticmethod
    def generate_order_number() -> str:
        date_str = datetime.now(timezone.utc).strftime("%Y%m%d")
        unique_suffix = uuid.uuid4().hex[:6].upper()
        return f"CRV-{date_str}-{unique_suffix}"

    @staticmethod
    async def create_order(
        db: AsyncSession,
        customer: User,
        data: OrderCreateRequest
    ) -> Order:
        # 1. Validate Delivery Address ownership
        addr_result = await db.execute(
            select(Address).where(Address.id == data.address_id, Address.customer_id == customer.id)
        )
        address = addr_result.scalar_one_or_none()
        if not address:
            raise HTTPException(
                status_code=status.HTTP_404_NOT_FOUND,
                detail="Selected delivery address not found or does not belong to you",
            )

        address_snapshot = {
            "full_name": address.full_name,
            "phone": address.phone,
            "house": address.house,
            "street": address.street,
            "area": address.area,
            "landmark": address.landmark,
            "city": address.city,
            "state": address.state,
            "pincode": address.pincode,
            "address_type": address.address_type.value,
        }

        # 2. Collect Items (from request or from user's active Cart)
        items_to_order = []
        is_from_cart = False

        if data.items and len(data.items) > 0:
            for itm in data.items:
                items_to_order.append((itm.product_id, itm.quantity))
        else:
            cart_res = await db.execute(
                select(Cart).options(selectinload(Cart.items)).where(Cart.customer_id == customer.id)
            )
            cart = cart_res.scalar_one_or_none()
            if not cart or not cart.items:
                raise HTTPException(
                    status_code=status.HTTP_400_BAD_REQUEST,
                    detail="Cart is empty. Please add items before checkout",
                )
            for itm in cart.items:
                items_to_order.append((itm.product_id, itm.quantity))
            is_from_cart = True

        # 3. Authoritative Price, Availability, and Stock Validation
        subtotal = 0.0
        order_items_entities = []
        primary_seller_id: Optional[int] = None

        for product_id, quantity in items_to_order:
            # Query product with select for update or direct check
            prod_result = await db.execute(
                select(Product).where(Product.id == product_id)
            )
            product = prod_result.scalar_one_or_none()

            if not product:
                raise HTTPException(
                    status_code=status.HTTP_404_NOT_FOUND,
                    detail=f"Product with ID {product_id} no longer exists",
                )

            if product.status != ProductStatus.APPROVED or not product.availability:
                raise HTTPException(
                    status_code=status.HTTP_400_BAD_REQUEST,
                    detail=f"Product '{product.name}' is currently unavailable for order",
                )

            if product.stock < quantity:
                raise HTTPException(
                    status_code=status.HTTP_400_BAD_REQUEST,
                    detail=f"Insufficient stock for '{product.name}'. Available: {product.stock}, requested: {quantity}",
                )

            # Atomic stock deduction
            product.stock -= quantity
            if product.stock == 0:
                product.availability = False

            item_total = round(product.price * quantity, 2)
            subtotal += item_total
            if primary_seller_id is None:
                primary_seller_id = product.seller_id

            order_items_entities.append(
                OrderItem(
                    product_id=product.id,
                    seller_id=product.seller_id,
                    product_name_snapshot=product.name,
                    unit_price=product.price,
                    quantity=quantity,
                    total_price=item_total,
                )
            )

        subtotal = round(subtotal, 2)

        # 4. Authoritative Delivery Fee Calculation
        delivery_fee = 0.0 if subtotal >= 500.0 else 40.0

        # 5. Authoritative Coupon Discount Calculation
        discount = 0.0
        if data.coupon_code:
            code_clean = data.coupon_code.strip().upper()
            now = datetime.now(timezone.utc)
            cpn_res = await db.execute(
                select(Coupon).where(
                    Coupon.code == code_clean,
                    Coupon.is_active.is_(True),
                    Coupon.start_date <= now,
                    Coupon.end_date >= now,
                )
            )
            coupon = cpn_res.scalar_one_or_none()
            if not coupon:
                raise HTTPException(
                    status_code=status.HTTP_400_BAD_REQUEST,
                    detail="Invalid or expired coupon code",
                )
            if subtotal < coupon.minimum_order:
                raise HTTPException(
                    status_code=status.HTTP_400_BAD_REQUEST,
                    detail=f"Minimum order value for coupon {code_clean} is Rs. {coupon.minimum_order}",
                )
            if coupon.usage_limit and coupon.times_used >= coupon.usage_limit:
                raise HTTPException(
                    status_code=status.HTTP_400_BAD_REQUEST,
                    detail="Coupon usage limit has been reached",
                )

            if coupon.discount_type == DiscountType.PERCENTAGE:
                discount = round((subtotal * coupon.discount_value) / 100.0, 2)
                if coupon.maximum_discount and discount > coupon.maximum_discount:
                    discount = coupon.maximum_discount
            else:
                discount = min(coupon.discount_value, subtotal)

            coupon.times_used += 1

        total = round(max(0.0, subtotal + delivery_fee - discount), 2)

        # 6. Transactional Order Record Creation
        order_number = OrderService.generate_order_number()
        new_order = Order(
            order_number=order_number,
            customer_id=customer.id,
            seller_id=primary_seller_id,
            subtotal=subtotal,
            delivery_fee=delivery_fee,
            discount=discount,
            total=total,
            address_snapshot=address_snapshot,
            payment_method=data.payment_method.upper(),
            payment_status=PaymentStatus.PENDING,
            order_status=OrderStatus.ORDER_PLACED,
        )
        db.add(new_order)
        await db.flush()

        # Link order items
        for itm in order_items_entities:
            itm.order_id = new_order.id
            db.add(itm)

        # Initial Tracking Event
        tracking = OrderTrackingEvent(
            order_id=new_order.id,
            status=OrderStatus.ORDER_PLACED,
            description="Order has been placed and received by CRAVEXA",
        )
        db.add(tracking)

        # Initial Payment Record
        payment = Payment(
            order_id=new_order.id,
            provider=data.payment_method.upper(),
            amount=total,
            status=PaymentStatus.PENDING,
        )
        db.add(payment)

        # Clear Cart if ordered from cart
        if is_from_cart:
            cart_res = await db.execute(
                select(Cart).options(selectinload(Cart.items)).where(Cart.customer_id == customer.id)
            )
            cart = cart_res.scalar_one_or_none()
            if cart and cart.items:
                for cart_itm in cart.items:
                    await db.delete(cart_itm)

        await db.commit()
        await db.refresh(new_order)

        # Reload with items and tracking
        full_res = await db.execute(
            select(Order)
            .options(
                selectinload(Order.items),
                selectinload(Order.tracking_events),
            )
            .where(Order.id == new_order.id)
        )
        return full_res.scalar_one()

    @staticmethod
    async def update_order_status(
        db: AsyncSession,
        order_id: int,
        new_status: OrderStatus,
        description: Optional[str] = None,
        is_admin: bool = False,
        seller_id: Optional[int] = None
    ) -> Order:
        result = await db.execute(
            select(Order)
            .options(selectinload(Order.items), selectinload(Order.tracking_events))
            .where(Order.id == order_id)
        )
        order = result.scalar_one_or_none()

        if not order:
            raise HTTPException(
                status_code=status.HTTP_404_NOT_FOUND,
                detail="Order not found",
            )

        if not is_admin:
            if seller_id and order.seller_id != seller_id:
                raise HTTPException(
                    status_code=status.HTTP_403_FORBIDDEN,
                    detail="Forbidden: You cannot modify orders from other sellers",
                )
            if new_status not in SELLER_ALLOWED_TRANSITIONS:
                raise HTTPException(
                    status_code=status.HTTP_403_FORBIDDEN,
                    detail=f"Sellers are not authorized to transition order to '{new_status.value}'",
                )

        allowed = ALLOWED_TRANSITIONS.get(order.order_status, [])
        if new_status not in allowed:
            raise HTTPException(
                status_code=status.HTTP_400_BAD_REQUEST,
                detail=f"Invalid order status transition from '{order.order_status.value}' to '{new_status.value}'",
            )

        order.order_status = new_status
        order.updated_at = datetime.now(timezone.utc)

        tracking = OrderTrackingEvent(
            order_id=order.id,
            status=new_status,
            description=description or f"Order status updated to {new_status.value.replace('_', ' ').title()}",
        )
        db.add(tracking)

        await db.commit()
        await db.refresh(order)
        return order
