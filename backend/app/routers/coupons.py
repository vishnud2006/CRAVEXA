from datetime import datetime, timezone
from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession

from app.core.dependencies import require_authenticated_user
from app.database.session import get_db
from app.models.coupon import Coupon
from app.models.user import User
from app.schemas.coupon import CouponOut

router = APIRouter(prefix="/coupons", tags=["Coupons"])


@router.get("/validate/{code}", response_model=CouponOut)
async def validate_coupon(
    code: str,
    current_user: User = Depends(require_authenticated_user),
    db: AsyncSession = Depends(get_db),
):
    """
    Validate a coupon code and retrieve its discount details.
    Ensures coupon is currently active, within valid date range, and usage limits.
    """
    now = datetime.now(timezone.utc)
    result = await db.execute(
        select(Coupon).where(
            Coupon.code == code.strip().upper(),
            Coupon.is_active.is_(True),
            Coupon.start_date <= now,
            Coupon.end_date >= now,
        )
    )
    coupon = result.scalar_one_or_none()
    if not coupon:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="Coupon code is invalid or has expired",
        )

    if coupon.usage_limit and coupon.times_used >= coupon.usage_limit:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail="This coupon has reached its maximum usage limit",
        )

    return coupon
