from typing import List, Optional
from fastapi import APIRouter, Depends, Query
from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession

from app.core.dependencies import require_admin
from app.database.session import get_db
from app.models.order import PaymentStatus
from app.models.payment import Payment
from app.models.user import User
from app.schemas.payment import PaymentOut

router = APIRouter(prefix="/payments", tags=["Admin - Payments"])


@router.get("", response_model=List[PaymentOut])
async def list_payments(
    payment_status: Optional[PaymentStatus] = Query(None, alias="status", description="Filter by payment status"),
    skip: int = Query(0, ge=0),
    limit: int = Query(20, ge=1, le=100),
    admin: User = Depends(require_admin),
    db: AsyncSession = Depends(get_db),
):
    """Admin endpoint to inspect all recorded payment transactions."""
    query = select(Payment)
    if payment_status:
        query = query.where(Payment.status == payment_status)

    query = query.order_by(Payment.created_at.desc()).offset(skip).limit(limit)
    result = await db.execute(query)
    return result.scalars().all()
