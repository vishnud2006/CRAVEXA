from typing import Optional
from pydantic import BaseModel, EmailStr, field_validator
from app.models.user import UserRole
from app.schemas.user import UserOut


class LoginRequest(BaseModel):
    email: EmailStr
    password: str


class TokenResponse(BaseModel):
    access_token: str
    token_type: str = "bearer"
    user: UserOut


class RegisterRequest(BaseModel):
    email: EmailStr
    password: str
    name: str
    phone: Optional[str] = None
    role: UserRole = UserRole.CUSTOMER

    @field_validator("role")
    @classmethod
    def prevent_admin_registration(cls, v: UserRole) -> UserRole:
        if v == UserRole.ADMIN:
            raise ValueError("Registration as ADMIN is strictly prohibited")
        return v


class SyncFirebaseUserRequest(BaseModel):
    firebase_id_token: str
    role: UserRole = UserRole.CUSTOMER
    name: Optional[str] = None
    phone: Optional[str] = None

    @field_validator("role")
    @classmethod
    def prevent_admin_sync(cls, v: UserRole) -> UserRole:
        if v == UserRole.ADMIN:
            raise ValueError("Admin role cannot be synced via client request")
        return v


class ChangePasswordRequest(BaseModel):
    current_password: str
    new_password: str
