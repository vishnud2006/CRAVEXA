from datetime import datetime, timezone
from typing import List, Optional
from fastapi import APIRouter, Depends, HTTPException, Query, status
from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession

from app.core.dependencies import require_admin
from app.database.session import get_db
from app.models.fssai import FSSAIRecord
from app.models.seller import FssaiStatus, SellerProfile
from app.models.user import User
from app.schemas.fssai import FSSAIRecordOut, FSSAIReviewRequest

router = APIRouter(prefix="/fssai", tags=["Admin - FSSAI Verification"])


@router.get("", response_model=List[FSSAIRecordOut])
async def list_fssai_records(
    fssai_status: Optional[FssaiStatus] = Query(None, alias="status", description="Filter by status"),
    skip: int = Query(0, ge=0),
    limit: int = Query(20, ge=1, le=100),
    admin: User = Depends(require_admin),
    db: AsyncSession = Depends(get_db),
):
    """Admin endpoint to view submitted FSSAI records with status filter."""
    query = select(FSSAIRecord)
    if fssai_status:
        query = query.where(FSSAIRecord.status == fssai_status)

    query = query.order_by(FSSAIRecord.created_at.desc()).offset(skip).limit(limit)
    result = await db.execute(query)
    return result.scalars().all()


@router.get("/pending", response_model=List[FSSAIRecordOut])
async def list_pending_fssai(
    skip: int = Query(0, ge=0),
    limit: int = Query(20, ge=1, le=100),
    admin: User = Depends(require_admin),
    db: AsyncSession = Depends(get_db),
):
    """Admin queue of FSSAI documents awaiting verification."""
    result = await db.execute(
        select(FSSAIRecord)
        .where(FSSAIRecord.status == FssaiStatus.PENDING)
        .order_by(FSSAIRecord.submitted_at.asc())
        .offset(skip)
        .limit(limit)
    )
    return result.scalars().all()


@router.put("/{record_id}/verify", response_model=FSSAIRecordOut)
async def verify_fssai_document(
    record_id: int,
    admin: User = Depends(require_admin),
    db: AsyncSession = Depends(get_db),
):
    """
    Mark an FSSAI submission as VERIFIED after inspecting credentials.
    Updates both the audit record and the seller's live profile.
    """
    result = await db.execute(select(FSSAIRecord).where(FSSAIRecord.id == record_id))
    record = result.scalar_one_or_none()
    if not record:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="FSSAI record not found",
        )

    record.status = FssaiStatus.VERIFIED
    record.verified_at = datetime.now(timezone.utc)
    record.rejection_reason = None

    # Update seller profile status
    seller_res = await db.execute(
        select(SellerProfile).where(SellerProfile.id == record.seller_id)
    )
    seller = seller_res.scalar_one_or_none()
    if seller:
        seller.fssai_status = FssaiStatus.VERIFIED
        seller.fssai_number = record.fssai_number

    await db.commit()
    await db.refresh(record)
    return record


@router.put("/{record_id}/reject", response_model=FSSAIRecordOut)
async def reject_fssai_document(
    record_id: int,
    review_in: FSSAIReviewRequest,
    admin: User = Depends(require_admin),
    db: AsyncSession = Depends(get_db),
):
    """
    Reject an FSSAI submission with a recorded reason (e.g., illegible, expired, mismatch).
    Updates both the audit record and the seller's live profile.
    """
    result = await db.execute(select(FSSAIRecord).where(FSSAIRecord.id == record_id))
    record = result.scalar_one_or_none()
    if not record:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="FSSAI record not found",
        )

    record.status = FssaiStatus.REJECTED
    record.rejection_reason = review_in.rejection_reason or "Document rejected by administration"

    seller_res = await db.execute(
        select(SellerProfile).where(SellerProfile.id == record.seller_id)
    )
    seller = seller_res.scalar_one_or_none()
    if seller:
        seller.fssai_status = FssaiStatus.REJECTED

    await db.commit()
    await db.refresh(record)
    return record
