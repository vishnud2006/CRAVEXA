from datetime import datetime
from typing import Optional
from pydantic import BaseModel, ConfigDict
from app.models.address import AddressType


class AddressBase(BaseModel):
    full_name: str
    phone: str
    house: Optional[str] = ""
    street: Optional[str] = ""
    area: Optional[str] = ""
    address_line1: Optional[str] = None
    address_line2: Optional[str] = None
    landmark: Optional[str] = None
    city: str
    state: str
    pincode: str
    address_type: AddressType = AddressType.HOME
    is_default: bool = False


class AddressCreate(AddressBase):
    pass


class AddressUpdate(BaseModel):
    full_name: Optional[str] = None
    phone: Optional[str] = None
    house: Optional[str] = None
    street: Optional[str] = None
    area: Optional[str] = None
    landmark: Optional[str] = None
    city: Optional[str] = None
    state: Optional[str] = None
    pincode: Optional[str] = None
    address_type: Optional[AddressType] = None
    is_default: Optional[bool] = None


class AddressOut(AddressBase):
    id: int
    customer_id: int
    created_at: datetime
    updated_at: datetime

    model_config = ConfigDict(from_attributes=True)
