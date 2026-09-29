from typing import List, Optional
from datetime import datetime, timezone
from fastapi import APIRouter, Depends, HTTPException, Query, status
from sqlalchemy import select, update
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy.orm import selectinload

from app.core.dependencies import require_authenticated_user
from app.database.session import get_db
from app.models.order import Order, OrderItem, OrderStatus, OrderTrackingEvent
from app.models.product import Product
from app.models.user import User, UserRole
from app.schemas.order import OrderCreateRequest, OrderOut, OrderTrackingEventOut
from app.services.order_service import OrderService

router = APIRouter(prefix="/orders", tags=["Orders"])


@router.post("", response_model=OrderOut, status_code=status.HTTP_201_CREATED)
async def create_order(
    order_in: OrderCreateRequest,
    current_user: User = Depends(require_authenticated_user),
    db: AsyncSession = Depends(get_db),
):
    """
    Place a new CRAVEXA order.
    The backend computes all prices, discounts, and delivery fees authoritatively.
    Deducts stock concurrency-safely and clears purchased items from cart.
    """
    return await OrderService.create_order(db, current_user, order_in)


@router.get("", response_model=List[OrderOut])
async def get_my_orders(
    skip: int = Query(0, ge=0),
    limit: int = Query(20, ge=1, le=100),
    current_user: User = Depends(require_authenticated_user),
    db: AsyncSession = Depends(get_db),
):
    """Retrieve order history for the authenticated customer."""
    result = await db.execute(
        select(Order)
        .options(
            selectinload(Order.items),
            selectinload(Order.tracking_events),
        )
        .where(Order.customer_id == current_user.id)
        .order_by(Order.created_at.desc())
        .offset(skip)
        .limit(limit)
    )
    return result.scalars().all()


@router.get("/{order_id}", response_model=OrderOut)
async def get_order_by_id(
    order_id: int,
    current_user: User = Depends(require_authenticated_user),
    db: AsyncSession = Depends(get_db),
):
    """Retrieve full details of a specific order."""
    result = await db.execute(
        select(Order)
        .options(
            selectinload(Order.items),
            selectinload(Order.tracking_events),
        )
        .where(Order.id == order_id)
    )
    order = result.scalar_one_or_none()
    if not order:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="Order not found",
        )

    # Ownership check: Customer who placed it, Admin, or Seller who has an item
    if current_user.role != UserRole.ADMIN and order.customer_id != current_user.id:
        has_seller_item = any(item.seller_id == current_user.id for item in order.items)
        if not has_seller_item and order.seller_id != current_user.id:
            raise HTTPException(
                status_code=status.HTTP_403_FORBIDDEN,
                detail="You do not have permission to view this order",
            )

    return order


@router.get("/{order_id}/tracking", response_model=List[OrderTrackingEventOut])
async def get_order_tracking(
    order_id: int,
    current_user: User = Depends(require_authenticated_user),
    db: AsyncSession = Depends(get_db),
):
    """Retrieve the tracking lifecycle timeline for an order."""
    result = await db.execute(
        select(Order)
        .options(selectinload(Order.tracking_events))
        .where(Order.id == order_id)
    )
    order = result.scalar_one_or_none()
    if not order:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="Order not found",
        )

    if current_user.role != UserRole.ADMIN and order.customer_id != current_user.id:
        raise HTTPException(
            status_code=status.HTTP_403_FORBIDDEN,
            detail="You do not have permission to track this order",
        )

    return sorted(order.tracking_events, key=lambda x: x.timestamp)


@router.post("/{order_id}/cancel", response_model=OrderOut)
async def cancel_order(
    order_id: int,
    current_user: User = Depends(require_authenticated_user),
    db: AsyncSession = Depends(get_db),
):
    """
    Cancel an order if it hasn't progressed past preparation.
    Restores stock to inventory and appends cancellation to tracking timeline.
    """
    result = await db.execute(
        select(Order)
        .options(
            selectinload(Order.items),
            selectinload(Order.tracking_events),
        )
        .where(Order.id == order_id)
    )
    order = result.scalar_one_or_none()
    if not order:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="Order not found",
        )

    if current_user.role != UserRole.ADMIN and order.customer_id != current_user.id:
        raise HTTPException(
            status_code=status.HTTP_403_FORBIDDEN,
            detail="You do not have permission to cancel this order",
        )

    cancellable_statuses = [OrderStatus.ORDER_PLACED, OrderStatus.PAYMENT_CONFIRMED]
    if order.order_status not in cancellable_statuses:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail=f"Order in state '{order.order_status.value}' can no longer be cancelled directly by customer",
        )

    order.order_status = OrderStatus.CANCELLED
    order.updated_at = datetime.now(timezone.utc)

    # Restore inventory stock
    for item in order.items:
        if item.product_id:
            await db.execute(
                update(Product)
                .where(Product.id == item.product_id)
                .values(stock=Product.stock + item.quantity)
            )

    tracking = OrderTrackingEvent(
        order_id=order.id,
        status=OrderStatus.CANCELLED,
        description="Order cancelled by customer. Stock restored to inventory.",
    )
    db.add(tracking)

    await db.commit()
    await db.refresh(order)
    return order
