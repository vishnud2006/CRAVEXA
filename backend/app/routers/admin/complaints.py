from typing import List, Optional
from fastapi import APIRouter, Depends, HTTPException, Query, status
from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession

from app.core.dependencies import require_admin
from app.database.session import get_db
from app.models.complaint import Complaint, ComplaintStatus
from app.models.user import User
from app.schemas.complaint import ComplaintAdminOut, ComplaintAdminUpdateRequest

router = APIRouter(prefix="/complaints", tags=["Admin - Complaints"])


@router.get("", response_model=List[ComplaintAdminOut])
async def list_all_complaints(
    complaint_status: Optional[ComplaintStatus] = Query(None, alias="status", description="Filter by status"),
    skip: int = Query(0, ge=0),
    limit: int = Query(20, ge=1, le=100),
    admin: User = Depends(require_admin),
    db: AsyncSession = Depends(get_db),
):
    """Admin endpoint to triage and monitor customer complaints."""
    query = select(Complaint)
    if complaint_status:
        query = query.where(Complaint.status == complaint_status)

    query = query.order_by(Complaint.created_at.desc()).offset(skip).limit(limit)
    result = await db.execute(query)
    return result.scalars().all()


@router.put("/{complaint_id}", response_model=ComplaintAdminOut)
async def update_complaint_status(
    complaint_id: int,
    update_in: ComplaintAdminUpdateRequest,
    admin: User = Depends(require_admin),
    db: AsyncSession = Depends(get_db),
):
    """Admin endpoint to resolve or advance the status of a complaint and attach resolution notes."""
    result = await db.execute(select(Complaint).where(Complaint.id == complaint_id))
    complaint = result.scalar_one_or_none()
    if not complaint:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="Complaint not found",
        )

    complaint.status = update_in.status
    if update_in.admin_notes is not None:
        complaint.admin_notes = update_in.admin_notes

    await db.commit()
    await db.refresh(complaint)
    return complaint
