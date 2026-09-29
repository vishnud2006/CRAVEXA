from typing import List, Optional
from fastapi import APIRouter, Depends, HTTPException, Query, status
from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy.orm import selectinload

from app.core.dependencies import require_admin
from app.database.session import get_db
from app.models.seller import SellerProfile, SellerStatus
from app.models.user import User
from app.schemas.seller import SellerProfileOut, SellerStatusUpdate

router = APIRouter(prefix="/sellers", tags=["Admin - Sellers"])


@router.get("", response_model=List[SellerProfileOut])
async def list_all_sellers(
    seller_status: Optional[SellerStatus] = Query(None, alias="status", description="Filter by seller status"),
    skip: int = Query(0, ge=0),
    limit: int = Query(20, ge=1, le=100),
    admin: User = Depends(require_admin),
    db: AsyncSession = Depends(get_db),
):
    """
    Admin endpoint to view all sellers.
    Enables reviewing PENDING seller applications, APPROVED vendors, and SUSPENDED accounts.
    """
    query = select(SellerProfile)
    if seller_status:
        query = query.where(SellerProfile.seller_status == seller_status)

    query = query.order_by(SellerProfile.created_at.desc()).offset(skip).limit(limit)
    result = await db.execute(query)
    return result.scalars().all()


@router.get("/{seller_id}", response_model=SellerProfileOut)
async def get_seller_detail(
    seller_id: int,
    admin: User = Depends(require_admin),
    db: AsyncSession = Depends(get_db),
):
    """Admin endpoint to view complete details of a specific seller profile."""
    result = await db.execute(
        select(SellerProfile).where(SellerProfile.id == seller_id)
    )
    seller = result.scalar_one_or_none()
    if not seller:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="Seller profile not found",
        )
    return seller


@router.put("/{seller_id}/status", response_model=SellerProfileOut)
async def update_seller_approval_status(
    seller_id: int,
    status_update: SellerStatusUpdate,
    admin: User = Depends(require_admin),
    db: AsyncSession = Depends(get_db),
):
    """
    Admin approval/rejection/suspension workflow for sellers.
    Only authorized Admin processes can approve sellers.
    """
    result = await db.execute(select(SellerProfile).where(SellerProfile.id == seller_id))
    seller = result.scalar_one_or_none()
    if not seller:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="Seller not found",
        )

    seller.seller_status = status_update.seller_status
    await db.commit()
    await db.refresh(seller)
    return seller
