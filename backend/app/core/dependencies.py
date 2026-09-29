from typing import Optional
from fastapi import Depends, HTTPException, status
from fastapi.security import OAuth2PasswordBearer
from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy.orm import selectinload

from app.core.security import decode_access_token
from app.database.session import get_db
from app.models.seller import SellerProfile
from app.models.user import User, UserRole, UserStatus

oauth2_scheme = OAuth2PasswordBearer(tokenUrl="/auth/login", auto_error=False)


async def get_current_user(
    token: Optional[str] = Depends(oauth2_scheme),
    db: AsyncSession = Depends(get_db),
) -> User:
    """Retrieve and validate the currently authenticated user from Bearer JWT token."""
    credentials_exception = HTTPException(
        status_code=status.HTTP_401_UNAUTHORIZED,
        detail="Could not validate credentials",
        headers={"WWW-Authenticate": "Bearer"},
    )
    if not token:
        raise credentials_exception

    payload = decode_access_token(token)
    if payload is None:
        raise credentials_exception

    user_id: Optional[str] = payload.get("sub")
    if user_id is None:
        raise credentials_exception

    try:
        user_id_int = int(user_id)
    except ValueError:
        raise credentials_exception

    result = await db.execute(
        select(User).options(selectinload(User.seller_profile)).where(User.id == user_id_int)
    )
    user = result.scalar_one_or_none()
    if user is None:
        raise credentials_exception

    if user.status != UserStatus.ACTIVE:
        raise HTTPException(
            status_code=status.HTTP_403_FORBIDDEN,
            detail=f"Account is {user.status.value.lower()}",
        )

    return user


async def require_authenticated_user(
    current_user: User = Depends(get_current_user),
) -> User:
    """Dependency that guarantees the caller is an active authenticated user."""
    return current_user


async def require_customer(
    current_user: User = Depends(require_authenticated_user),
) -> User:
    """Dependency enforcing CUSTOMER role."""
    if current_user.role != UserRole.CUSTOMER and current_user.role != UserRole.ADMIN:
        raise HTTPException(
            status_code=status.HTTP_403_FORBIDDEN,
            detail="Customer privileges required for this action",
        )
    return current_user


async def require_seller(
    current_user: User = Depends(require_authenticated_user),
) -> User:
    """Dependency enforcing SELLER role."""
    if current_user.role != UserRole.SELLER and current_user.role != UserRole.ADMIN:
        raise HTTPException(
            status_code=status.HTTP_403_FORBIDDEN,
            detail="Seller privileges required for this action",
        )
    return current_user


async def get_current_seller_profile(
    current_user: User = Depends(require_seller),
    db: AsyncSession = Depends(get_db),
) -> SellerProfile:
    """Dependency returning the SellerProfile of the authenticated seller."""
    result = await db.execute(
        select(SellerProfile).where(SellerProfile.user_id == current_user.id)
    )
    profile = result.scalar_one_or_none()
    if not profile:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="Seller profile not found for this user",
        )
    return profile


async def require_admin(
    current_user: User = Depends(require_authenticated_user),
) -> User:
    """Dependency enforcing strictly ADMIN role."""
    if current_user.role != UserRole.ADMIN:
        raise HTTPException(
            status_code=status.HTTP_403_FORBIDDEN,
            detail="Admin privileges required for this action",
        )
    return current_user
