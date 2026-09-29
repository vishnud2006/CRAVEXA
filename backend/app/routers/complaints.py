from typing import List
from fastapi import APIRouter, Depends, HTTPException, Query, status
from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession

from app.core.dependencies import require_authenticated_user
from app.database.session import get_db
from app.models.complaint import Complaint, ComplaintStatus
from app.models.order import Order
from app.models.user import User
from app.schemas.complaint import ComplaintCreateRequest, ComplaintOut

router = APIRouter(prefix="/complaints", tags=["Complaints"])


@router.post("", response_model=ComplaintOut, status_code=status.HTTP_201_CREATED)
async def submit_complaint(
    complaint_in: ComplaintCreateRequest,
    current_user: User = Depends(require_authenticated_user),
    db: AsyncSession = Depends(get_db),
):
    """Submit a customer support complaint or grievance."""
    if complaint_in.order_id:
        order_res = await db.execute(
            select(Order).where(Order.id == complaint_in.order_id, Order.customer_id == current_user.id)
        )
        if not order_res.scalar_one_or_none():
            raise HTTPException(
                status_code=status.HTTP_404_NOT_FOUND,
                detail="Order not found or does not belong to you",
            )

    complaint = Complaint(
        user_id=current_user.id,
        order_id=complaint_in.order_id,
        category=complaint_in.category,
        description=complaint_in.description,
        status=ComplaintStatus.OPEN,
    )
    db.add(complaint)
    await db.commit()
    await db.refresh(complaint)
    return complaint


@router.get("", response_model=List[ComplaintOut])
async def get_my_complaints(
    skip: int = Query(0, ge=0),
    limit: int = Query(20, ge=1, le=100),
    current_user: User = Depends(require_authenticated_user),
    db: AsyncSession = Depends(get_db),
):
    """List customer complaints submitted by current user."""
    result = await db.execute(
        select(Complaint)
        .where(Complaint.user_id == current_user.id)
        .order_by(Complaint.created_at.desc())
        .offset(skip)
        .limit(limit)
    )
    return result.scalars().all()
