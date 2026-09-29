from datetime import datetime
from typing import Optional
from pydantic import BaseModel, ConfigDict
from app.models.complaint import ComplaintStatus


class ComplaintCreateRequest(BaseModel):
    category: str
    description: str
    order_id: Optional[int] = None


class ComplaintAdminUpdateRequest(BaseModel):
    status: ComplaintStatus
    admin_notes: Optional[str] = None


class ComplaintOut(BaseModel):
    id: int
    user_id: int
    order_id: Optional[int] = None
    category: str
    description: str
    status: ComplaintStatus
    created_at: datetime
    updated_at: datetime

    model_config = ConfigDict(from_attributes=True)


class ComplaintAdminOut(ComplaintOut):
    admin_notes: Optional[str] = None

    model_config = ConfigDict(from_attributes=True)
