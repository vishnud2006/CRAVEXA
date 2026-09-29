from datetime import datetime
from typing import Optional
from pydantic import BaseModel, ConfigDict
from app.models.order import PaymentStatus


class PaymentOut(BaseModel):
    id: int
    order_id: int
    provider: str
    provider_payment_id: Optional[str] = None
    amount: float
    status: PaymentStatus
    created_at: datetime
    updated_at: datetime

    model_config = ConfigDict(from_attributes=True)
