from typing import List, Optional
from fastapi import APIRouter, Depends, HTTPException, Query, status
from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession

from app.core.dependencies import require_admin
from app.database.session import get_db
from app.models.user import User, UserRole, UserStatus
from app.schemas.user import UserOut, UserStatusUpdate

router = APIRouter(prefix="/users", tags=["Admin - Users"])


@router.get("", response_model=List[UserOut])
async def list_users(
    role: Optional[UserRole] = Query(None, description="Filter by user role"),
    user_status: Optional[UserStatus] = Query(None, alias="status", description="Filter by status"),
    skip: int = Query(0, ge=0),
    limit: int = Query(20, ge=1, le=100),
    admin: User = Depends(require_admin),
    db: AsyncSession = Depends(get_db),
):
    """Admin endpoint to list all platform users with role and status filtering."""
    query = select(User)
    if role:
        query = query.where(User.role == role)
    if user_status:
        query = query.where(User.status == user_status)

    query = query.order_by(User.created_at.desc()).offset(skip).limit(limit)
    result = await db.execute(query)
    return result.scalars().all()


@router.put("/{user_id}/status", response_model=UserOut)
async def update_user_status(
    user_id: int,
    status_in: UserStatusUpdate,
    admin: User = Depends(require_admin),
    db: AsyncSession = Depends(get_db),
):
    """Admin endpoint to update user status (ACTIVE, SUSPENDED, DEACTIVATED)."""
    if admin.id == user_id and status_in.status != UserStatus.ACTIVE:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail="Admin cannot deactivate or suspend their own account",
        )

    result = await db.execute(select(User).where(User.id == user_id))
    user = result.scalar_one_or_none()
    if not user:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="User not found",
        )

    user.status = status_in.status
    await db.commit()
    await db.refresh(user)
    return user
