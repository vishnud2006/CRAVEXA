from typing import Annotated
from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy.ext.asyncio import AsyncSession

from app.core.dependencies import get_current_user, require_authenticated_user
from app.core.security import create_access_token
from app.database.session import get_db
from app.models.user import User, UserRole, UserStatus
from app.schemas.auth import (
    ChangePasswordRequest,
    LoginRequest,
    RegisterRequest,
    SyncFirebaseUserRequest,
    TokenResponse,
)
from app.schemas.common import MessageResponse
from app.schemas.user import UserOut
from app.services.auth_service import AuthService

router = APIRouter(prefix="/auth", tags=["Authentication"])


@router.post("/login", response_model=TokenResponse)
async def login(
    req: LoginRequest,
    db: Annotated[AsyncSession, Depends(get_db)]
):
    """Authenticate with email and password and receive a Bearer JWT."""
    user = await AuthService.authenticate_user(db, req.email, req.password)
    if not user:
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Invalid email or password",
            headers={"WWW-Authenticate": "Bearer"},
        )

    if user.status != UserStatus.ACTIVE:
        raise HTTPException(
            status_code=status.HTTP_403_FORBIDDEN,
            detail=f"Account is {user.status.value}",
        )

    access_token = create_access_token(
        subject=user.id,
        role=user.role.value,
        extra_claims={"must_change_password": user.must_change_password}
    )

    return TokenResponse(
        access_token=access_token,
        token_type="bearer",
        user=UserOut.model_validate(user)
    )


@router.post("/register", response_model=TokenResponse, status_code=status.HTTP_201_CREATED)
async def register(
    req: RegisterRequest,
    db: Annotated[AsyncSession, Depends(get_db)]
):
    """Register a new CUSTOMER or SELLER (ADMIN registration is strictly prohibited)."""
    user = await AuthService.register_user(db, req)
    access_token = create_access_token(subject=user.id, role=user.role.value)
    return TokenResponse(
        access_token=access_token,
        token_type="bearer",
        user=UserOut.model_validate(user)
    )


@router.post("/sync-user", response_model=TokenResponse)
async def sync_firebase_user(
    req: SyncFirebaseUserRequest,
    db: Annotated[AsyncSession, Depends(get_db)]
):
    """Sync an authenticated user from Firebase Auth token (Server-side validation stub)."""
    # For Phase 7A backend foundation, extract UID or placeholder verified claim
    # In full production Firebase integration, firebase_admin.auth.verify_id_token will be used
    if not req.firebase_id_token or len(req.firebase_id_token) < 5:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail="Invalid Firebase ID token",
        )

    from sqlalchemy import select
    uid = f"fb_{abs(hash(req.firebase_id_token)) % 100000000}"
    email = f"user_{uid}@cravexa.com"

    result = await db.execute(select(User).where(User.firebase_uid == uid))
    user = result.scalar_one_or_none()

    if not user:
        user = User(
            firebase_uid=uid,
            email=email,
            name=req.name or "CRAVEXA User",
            phone=req.phone,
            role=req.role,
            status=UserStatus.ACTIVE,
            profile_completed=False,
        )
        db.add(user)
        await db.commit()
        await db.refresh(user)

    token = create_access_token(subject=user.id, role=user.role.value)
    return TokenResponse(
        access_token=token,
        token_type="bearer",
        user=UserOut.model_validate(user)
    )


@router.get("/me", response_model=UserOut)
async def get_me(
    current_user: Annotated[User, Depends(require_authenticated_user)]
):
    """Return the profile of the currently authenticated user."""
    return UserOut.model_validate(current_user)


@router.post("/change-password", response_model=MessageResponse)
async def change_password(
    req: ChangePasswordRequest,
    current_user: Annotated[User, Depends(require_authenticated_user)],
    db: Annotated[AsyncSession, Depends(get_db)]
):
    """Change password for authenticated user (required for initial Admin bootstrap)."""
    await AuthService.change_password(db, current_user, req)
    return MessageResponse(message="Password successfully updated")
