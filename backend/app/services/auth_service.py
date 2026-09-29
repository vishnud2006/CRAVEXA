from datetime import datetime, timezone
from typing import Optional
from fastapi import HTTPException, status
from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession

from app.core.config import settings
from app.core.security import get_password_hash, verify_password
from app.models.user import User, UserRole, UserStatus
from app.schemas.auth import ChangePasswordRequest, RegisterRequest


class AuthService:
    @staticmethod
    async def authenticate_user(
        db: AsyncSession,
        email: str,
        password: str
    ) -> Optional[User]:
        """Authenticate user by email and plaintext password."""
        result = await db.execute(select(User).where(User.email == email.lower().strip()))
        user = result.scalar_one_or_none()

        if not user or not user.hashed_password:
            return None

        if not verify_password(password, user.hashed_password):
            return None

        return user

    @staticmethod
    async def register_user(
        db: AsyncSession,
        req: RegisterRequest
    ) -> User:
        """Register a new customer or seller (ADMIN cannot register publicly)."""
        email_clean = req.email.lower().strip()
        existing = await db.execute(select(User).where(User.email == email_clean))
        if existing.scalar_one_or_none():
            raise HTTPException(
                status_code=status.HTTP_409_CONFLICT,
                detail="A user with this email address already exists",
            )

        new_user = User(
            email=email_clean,
            name=req.name.strip(),
            phone=req.phone.strip() if req.phone else None,
            role=req.role,
            status=UserStatus.ACTIVE,
            hashed_password=get_password_hash(req.password),
            must_change_password=False,
            profile_completed=False,
        )
        db.add(new_user)
        await db.commit()
        await db.refresh(new_user)
        return new_user

    @staticmethod
    async def change_password(
        db: AsyncSession,
        user: User,
        req: ChangePasswordRequest
    ) -> User:
        """Change user password, validating current password and clearing must_change_password."""
        if not user.hashed_password or not verify_password(req.current_password, user.hashed_password):
            raise HTTPException(
                status_code=status.HTTP_400_BAD_REQUEST,
                detail="Incorrect current password",
            )

        user.hashed_password = get_password_hash(req.new_password)
        user.must_change_password = False
        user.updated_at = datetime.now(timezone.utc)
        await db.commit()
        await db.refresh(user)
        return user

    @staticmethod
    async def bootstrap_admin(db: AsyncSession) -> Optional[User]:
        """Seed the initial root admin if not already present."""
        admin_email = settings.ADMIN_EMAIL.lower().strip()
        result = await db.execute(select(User).where(User.email == admin_email))
        admin = result.scalar_one_or_none()

        if not admin:
            admin = User(
                email=admin_email,
                name="CRAVEXA Super Admin",
                role=UserRole.ADMIN,
                status=UserStatus.ACTIVE,
                hashed_password=get_password_hash(settings.ADMIN_INITIAL_PASSWORD),
                must_change_password=True,
                profile_completed=True,
            )
            db.add(admin)
            await db.commit()
            await db.refresh(admin)
        return admin
