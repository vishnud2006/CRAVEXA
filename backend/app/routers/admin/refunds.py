from datetime import datetime, timezone
from typing import List, Optional
from fastapi import APIRouter, Depends, HTTPException, Query, status
from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession

from app.core.dependencies import require_admin
from app.database.session import get_db
from app.models.order import Order, OrderStatus
from app.models.refund import Refund, RefundStatus
from app.models.user import User
from app.schemas.refund import RefundOut, RefundStatusUpdateRequest

router = APIRouter(prefix="/refunds", tags=["Admin - Refunds"])


@router.get("", response_model=List[RefundOut])
async def list_all_refunds(
    refund_status: Optional[RefundStatus] = Query(None, alias="status", description="Filter by status"),
    skip: int = Query(0, ge=0),
    limit: int = Query(20, ge=1, le=100),
    admin: User = Depends(require_admin),
    db: AsyncSession = Depends(get_db),
):
    """Admin endpoint to view and triage customer refund requests."""
    query = select(Refund)
    if refund_status:
        query = query.where(Refund.status == refund_status)

    query = query.order_by(Refund.created_at.desc()).offset(skip).limit(limit)
    result = await db.execute(query)
    return result.scalars().all()


@router.put("/{refund_id}/status", response_model=RefundOut)
async def update_refund_status(
    refund_id: int,
    status_update: RefundStatusUpdateRequest,
    admin: User = Depends(require_admin),
    db: AsyncSession = Depends(get_db),
):
    """
    Admin workflow to approve or reject a refund request.
    If approved, marks the related order as REFUNDED.
    """
    result = await db.execute(select(Refund).where(Refund.id == refund_id))
    refund = result.scalar_one_or_none()
    if not refund:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="Refund not found",
        )

    refund.status = status_update.status
    refund.updated_at = datetime.now(timezone.utc)

    if status_update.status == RefundStatus.APPROVED:
        order_res = await db.execute(select(Order).where(Order.id == refund.order_id))
        order = order_res.scalar_one_or_none()
        if order:
            order.order_status = OrderStatus.REFUNDED

    await db.commit()
    await db.refresh(refund)
    return refund
