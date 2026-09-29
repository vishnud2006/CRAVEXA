from datetime import datetime
from typing import Optional
from pydantic import BaseModel, ConfigDict, EmailStr
from app.models.seller import FssaiStatus, SellerStatus


class SellerProfileBase(BaseModel):
    business_name: str
    about: Optional[str] = None
    phone: str
    email: EmailStr
    address: str
    city: str
    state: str
    pincode: str
    food_category: str
    profile_image_url: Optional[str] = None


class SellerProfileCreate(SellerProfileBase):
    fssai_number: Optional[str] = None


class SellerProfileUpdate(BaseModel):
    business_name: Optional[str] = None
    about: Optional[str] = None
    phone: Optional[str] = None
    address: Optional[str] = None
    city: Optional[str] = None
    state: Optional[str] = None
    pincode: Optional[str] = None
    food_category: Optional[str] = None
    profile_image_url: Optional[str] = None


class SellerProfileOut(SellerProfileBase):
    id: int
    user_id: int
    seller_status: SellerStatus
    fssai_status: FssaiStatus
    fssai_number: Optional[str] = None
    created_at: datetime
    updated_at: datetime

    model_config = ConfigDict(from_attributes=True)


class SellerStatusUpdate(BaseModel):
    seller_status: SellerStatus
    rejection_reason: Optional[str] = None
