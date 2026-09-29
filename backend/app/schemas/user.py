from datetime import datetime
from typing import Optional
from pydantic import BaseModel, ConfigDict, EmailStr
from app.models.user import UserRole, UserStatus


class UserBase(BaseModel):
    email: EmailStr
    name: str
    phone: Optional[str] = None
    profile_image_url: Optional[str] = None


class UserCreate(UserBase):
    password: Optional[str] = None
    role: UserRole = UserRole.CUSTOMER
    firebase_uid: Optional[str] = None


class UserUpdate(BaseModel):
    name: Optional[str] = None
    phone: Optional[str] = None
    profile_image_url: Optional[str] = None
    profile_completed: Optional[bool] = None


class UserOut(UserBase):
    id: int
    role: UserRole
    status: UserStatus
    profile_completed: bool
    must_change_password: bool
    created_at: datetime
    updated_at: datetime

    model_config = ConfigDict(from_attributes=True)


class UserStatusUpdate(BaseModel):
    status: UserStatus
