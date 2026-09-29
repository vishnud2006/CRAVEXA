from typing import List
from fastapi import APIRouter, Depends, HTTPException, Query, status
from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession

from app.core.dependencies import get_current_seller_profile, require_authenticated_user
from app.database.session import get_db
from app.models.review import Review, ReviewStatus
from app.models.seller import SellerProfile
from app.models.user import User
from app.schemas.review import ReviewCreateRequest, ReviewOut, ReviewResponseRequest
from app.services.review_service import ReviewService

router = APIRouter(tags=["Reviews"])


@router.post("/reviews", response_model=ReviewOut, status_code=status.HTTP_201_CREATED)
async def create_review(
    review_in: ReviewCreateRequest,
    current_user: User = Depends(require_authenticated_user),
    db: AsyncSession = Depends(get_db),
):
    """
    Submit a customer review for a purchased product.
    Strictly verifies that the customer actually bought the item and the order is DELIVERED.
    """
    return await ReviewService.create_review(db, current_user, review_in)


@router.get("/products/{product_id}/reviews", response_model=List[ReviewOut])
async def get_product_reviews(
    product_id: int,
    skip: int = Query(0, ge=0),
    limit: int = Query(20, ge=1, le=100),
    db: AsyncSession = Depends(get_db),
):
    """Get all approved reviews for a specific product."""
    result = await db.execute(
        select(Review)
        .where(
            Review.product_id == product_id,
            Review.status == ReviewStatus.APPROVED,
        )
        .order_by(Review.created_at.desc())
        .offset(skip)
        .limit(limit)
    )
    return result.scalars().all()


@router.post("/reviews/{review_id}/response", response_model=ReviewOut)
async def respond_to_review(
    review_id: int,
    response_in: ReviewResponseRequest,
    seller: SellerProfile = Depends(get_current_seller_profile),
    db: AsyncSession = Depends(get_db),
):
    """
    Post a seller response to a review on one of their products.
    """
    result = await db.execute(select(Review).where(Review.id == review_id))
    review = result.scalar_one_or_none()
    if not review:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="Review not found",
        )

    if review.seller_id != seller.id:
        raise HTTPException(
            status_code=status.HTTP_403_FORBIDDEN,
            detail="You can only respond to reviews on your own products",
        )

    review.seller_response = response_in.seller_response
    await db.commit()
    await db.refresh(review)
    return review
