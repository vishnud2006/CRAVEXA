from typing import List
from fastapi import APIRouter, Depends, HTTPException, Query, status
from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession

from app.core.dependencies import require_authenticated_user
from app.database.session import get_db
from app.models.order import Order, OrderStatus
from app.models.refund import Refund, RefundStatus
from app.models.user import User, UserRole
from app.schemas.refund import RefundCreateRequest, RefundOut

router = APIRouter(prefix="/refunds", tags=["Refunds"])


@router.post("", response_model=RefundOut, status_code=status.HTTP_201_CREATED)
async def request_refund(
    refund_in: RefundCreateRequest,
    current_user: User = Depends(require_authenticated_user),
    db: AsyncSession = Depends(get_db),
):
    """
    Request a refund for an eligible cancelled or returned order.
    The refund request is queued with PENDING status for Admin review.
    """
    order_res = await db.execute(
        select(Order).where(Order.id == refund_in.order_id, Order.customer_id == current_user.id)
    )
    order = order_res.scalar_one_or_none()
    if not order:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="Order not found or does not belong to you",
        )

    if refund_in.amount > order.total:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail=f"Refund amount ({refund_in.amount}) cannot exceed order total ({order.total})",
        )

    refund = Refund(
        order_id=order.id,
        amount=refund_in.amount,
        reason=refund_in.reason,
        status=RefundStatus.PENDING,
    )
    db.add(refund)
    await db.commit()
    await db.refresh(refund)
    return refund


@router.get("", response_model=List[RefundOut])
async def get_my_refunds(
    skip: int = Query(0, ge=0),
    limit: int = Query(20, ge=1, le=100),
    current_user: User = Depends(require_authenticated_user),
    db: AsyncSession = Depends(get_db),
):
    """Retrieve refund requests submitted by the authenticated customer."""
    result = await db.execute(
        select(Refund)
        .join(Order, Order.id == Refund.order_id)
        .where(Order.customer_id == current_user.id)
        .order_by(Refund.created_at.desc())
        .offset(skip)
        .limit(limit)
    )
    return result.scalars().all()


@router.get("/{refund_id}", response_model=RefundOut)
async def get_refund_by_id(
    refund_id: int,
    current_user: User = Depends(require_authenticated_user),
    db: AsyncSession = Depends(get_db),
):
    """Retrieve details for a specific refund request."""
    result = await db.execute(select(Refund).where(Refund.id == refund_id))
    refund = result.scalar_one_or_none()
    if not refund:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="Refund not found",
        )

    # Ownership check
    order_res = await db.execute(select(Order).where(Order.id == refund.order_id))
    order = order_res.scalar_one_or_none()
    if not order or (order.customer_id != current_user.id and current_user.role != UserRole.ADMIN):
        raise HTTPException(
            status_code=status.HTTP_403_FORBIDDEN,
            detail="Forbidden",
        )

    return refund
