from datetime import datetime, timezone
from typing import Annotated
from fastapi import APIRouter, Depends
from sqlalchemy.ext.asyncio import AsyncSession

from app.core.dependencies import require_authenticated_user
from app.database.session import get_db
from app.models.user import User
from app.schemas.user import UserOut, UserUpdate

router = APIRouter(prefix="/users", tags=["Users"])


@router.get("/me", response_model=UserOut)
async def get_my_profile(
    current_user: Annotated[User, Depends(require_authenticated_user)]
):
    """Retrieve currently logged in user profile."""
    return UserOut.model_validate(current_user)


@router.put("/me", response_model=UserOut)
async def update_my_profile(
    data: UserUpdate,
    current_user: Annotated[User, Depends(require_authenticated_user)],
    db: Annotated[AsyncSession, Depends(get_db)]
):
    """Update profile details for the currently logged in user."""
    update_data = data.model_dump(exclude_unset=True)
    for field, value in update_data.items():
        setattr(current_user, field, value)

    current_user.updated_at = datetime.now(timezone.utc)
    await db.commit()
    await db.refresh(current_user)
    return UserOut.model_validate(current_user)
