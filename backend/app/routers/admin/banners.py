from typing import List
from fastapi import APIRouter, Depends, HTTPException, Query, status
from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession

from app.core.dependencies import require_admin
from app.database.session import get_db
from app.models.banner import Banner
from app.models.user import User
from app.schemas.banner import BannerCreate, BannerOut

router = APIRouter(prefix="/banners", tags=["Admin - Banners"])


@router.get("", response_model=List[BannerOut])
async def list_all_banners(
    skip: int = Query(0, ge=0),
    limit: int = Query(20, ge=1, le=100),
    admin: User = Depends(require_admin),
    db: AsyncSession = Depends(get_db),
):
    """Admin endpoint to list all configured banners."""
    result = await db.execute(
        select(Banner).order_by(Banner.created_at.desc()).offset(skip).limit(limit)
    )
    return result.scalars().all()


@router.post("", response_model=BannerOut, status_code=status.HTTP_201_CREATED)
async def create_banner(
    banner_in: BannerCreate,
    admin: User = Depends(require_admin),
    db: AsyncSession = Depends(get_db),
):
    """Admin endpoint to create a promotional banner."""
    banner = Banner(**banner_in.model_dump())
    db.add(banner)
    await db.commit()
    await db.refresh(banner)
    return banner


@router.put("/{banner_id}", response_model=BannerOut)
async def update_banner(
    banner_id: int,
    banner_in: BannerCreate,
    admin: User = Depends(require_admin),
    db: AsyncSession = Depends(get_db),
):
    """Admin endpoint to update a banner's image, copy, or active schedule."""
    result = await db.execute(select(Banner).where(Banner.id == banner_id))
    banner = result.scalar_one_or_none()
    if not banner:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="Banner not found",
        )

    for field, val in banner_in.model_dump(exclude_unset=True).items():
        setattr(banner, field, val)

    await db.commit()
    await db.refresh(banner)
    return banner


@router.delete("/{banner_id}", status_code=status.HTTP_204_NO_CONTENT)
async def delete_banner(
    banner_id: int,
    admin: User = Depends(require_admin),
    db: AsyncSession = Depends(get_db),
):
    """Admin endpoint to remove a banner."""
    result = await db.execute(select(Banner).where(Banner.id == banner_id))
    banner = result.scalar_one_or_none()
    if banner:
        await db.delete(banner)
        await db.commit()

    return None
