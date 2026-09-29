from typing import List
from datetime import datetime, timezone
from fastapi import APIRouter, Depends
from sqlalchemy import or_, select
from sqlalchemy.ext.asyncio import AsyncSession

from app.database.session import get_db
from app.models.banner import Banner
from app.schemas.banner import BannerOut

router = APIRouter(prefix="/banners", tags=["Banners"])


@router.get("", response_model=List[BannerOut])
async def list_active_banners(
    db: AsyncSession = Depends(get_db),
):
    """Retrieve all active promotional banners for the CRAVEXA homepage/app banner carousel."""
    now = datetime.now(timezone.utc)
    result = await db.execute(
        select(Banner)
        .where(
            Banner.is_active.is_(True),
            or_(Banner.start_date.is_(None), Banner.start_date <= now),
            or_(Banner.end_date.is_(None), Banner.end_date >= now),
        )
        .order_by(Banner.created_at.desc())
    )
    return result.scalars().all()
