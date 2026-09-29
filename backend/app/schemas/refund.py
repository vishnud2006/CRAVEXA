from datetime import datetime
from pydantic import BaseModel, ConfigDict
from app.models.refund import RefundStatus


class RefundCreateRequest(BaseModel):
    order_id: int
    amount: float
    reason: str


class RefundStatusUpdateRequest(BaseModel):
    status: RefundStatus


class RefundOut(BaseModel):
    id: int
    order_id: int
    amount: float
    reason: str
    status: RefundStatus
    created_at: datetime
    updated_at: datetime

    model_config = ConfigDict(from_attributes=True)
