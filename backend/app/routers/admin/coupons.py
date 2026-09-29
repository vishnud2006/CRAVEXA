from typing import List
from fastapi import APIRouter, Depends, HTTPException, Query, status
from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession

from app.core.dependencies import require_admin
from app.database.session import get_db
from app.models.coupon import Coupon
from app.models.user import User
from app.schemas.coupon import CouponCreate, CouponOut

router = APIRouter(prefix="/coupons", tags=["Admin - Coupons"])


@router.get("", response_model=List[CouponOut])
async def list_all_coupons(
    skip: int = Query(0, ge=0),
    limit: int = Query(20, ge=1, le=100),
    admin: User = Depends(require_admin),
    db: AsyncSession = Depends(get_db),
):
    """Admin endpoint to list all discount coupons and monitor redemption counts."""
    result = await db.execute(
        select(Coupon).order_by(Coupon.created_at.desc()).offset(skip).limit(limit)
    )
    return result.scalars().all()


@router.post("", response_model=CouponOut, status_code=status.HTTP_201_CREATED)
async def create_coupon(
    coupon_in: CouponCreate,
    admin: User = Depends(require_admin),
    db: AsyncSession = Depends(get_db),
):
    """Admin endpoint to create a promotional coupon code."""
    code_clean = coupon_in.code.strip().upper()
    existing = await db.execute(select(Coupon).where(Coupon.code == code_clean))
    if existing.scalar_one_or_none():
        raise HTTPException(
            status_code=status.HTTP_409_CONFLICT,
            detail="A coupon with this code already exists",
        )

    coupon_dict = coupon_in.model_dump()
    coupon_dict["code"] = code_clean
    coupon = Coupon(**coupon_dict)
    db.add(coupon)
    await db.commit()
    await db.refresh(coupon)
    return coupon


@router.put("/{coupon_id}", response_model=CouponOut)
async def update_coupon(
    coupon_id: int,
    coupon_in: CouponCreate,
    admin: User = Depends(require_admin),
    db: AsyncSession = Depends(get_db),
):
    """Admin endpoint to update coupon terms, limits, or dates."""
    result = await db.execute(select(Coupon).where(Coupon.id == coupon_id))
    coupon = result.scalar_one_or_none()
    if not coupon:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="Coupon not found",
        )

    for field, val in coupon_in.model_dump(exclude_unset=True).items():
        setattr(coupon, field, val)

    await db.commit()
    await db.refresh(coupon)
    return coupon


@router.delete("/{coupon_id}", status_code=status.HTTP_204_NO_CONTENT)
async def delete_coupon(
    coupon_id: int,
    admin: User = Depends(require_admin),
    db: AsyncSession = Depends(get_db),
):
    """Admin endpoint to deactivate or remove a coupon."""
    result = await db.execute(select(Coupon).where(Coupon.id == coupon_id))
    coupon = result.scalar_one_or_none()
    if coupon:
        coupon.is_active = False
        await db.commit()

    return None
