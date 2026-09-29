from fastapi import HTTPException, status
from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession

from app.models.order import Order, OrderItem, OrderStatus
from app.models.product import Product
from app.models.review import Review, ReviewStatus
from app.models.user import User
from app.schemas.review import ReviewCreateRequest


class ReviewService:
    @staticmethod
    async def create_review(
        db: AsyncSession,
        customer: User,
        data: ReviewCreateRequest
    ) -> Review:
        # 1. Enforce verified purchase eligibility:
        # Must be an order belonging to customer, with status DELIVERED, containing the product
        order_res = await db.execute(
            select(Order).where(
                Order.id == data.order_id,
                Order.customer_id == customer.id,
            )
        )
        order = order_res.scalar_one_or_none()

        if not order:
            raise HTTPException(
                status_code=status.HTTP_404_NOT_FOUND,
                detail="Order not found or does not belong to you",
            )

        if order.order_status != OrderStatus.DELIVERED:
            raise HTTPException(
                status_code=status.HTTP_400_BAD_REQUEST,
                detail="You can only review products from successfully delivered orders",
            )

        # Check if product was part of this order
        item_res = await db.execute(
            select(OrderItem).where(
                OrderItem.order_id == order.id,
                OrderItem.product_id == data.product_id
            )
        )
        order_item = item_res.scalar_one_or_none()
        if not order_item:
            raise HTTPException(
                status_code=status.HTTP_400_BAD_REQUEST,
                detail="This product was not purchased in the specified order",
            )

        # Check for existing review for this order + product
        existing = await db.execute(
            select(Review).where(
                Review.customer_id == customer.id,
                Review.order_id == order.id,
                Review.product_id == data.product_id
            )
        )
        if existing.scalar_one_or_none():
            raise HTTPException(
                status_code=status.HTTP_409_CONFLICT,
                detail="You have already submitted a review for this product from this order",
            )

        # Get product to find seller
        prod_res = await db.execute(select(Product).where(Product.id == data.product_id))
        product = prod_res.scalar_one_or_none()
        if not product:
            raise HTTPException(
                status_code=status.HTTP_404_NOT_FOUND,
                detail="Product no longer exists",
            )

        review = Review(
            customer_id=customer.id,
            product_id=data.product_id,
            seller_id=product.seller_id,
            order_id=order.id,
            rating=data.rating,
            comment=data.comment,
            image_url=data.image_url,
            status=ReviewStatus.APPROVED,
        )
        db.add(review)
        await db.commit()
        await db.refresh(review)
        return review
