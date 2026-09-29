from datetime import datetime
from typing import Optional
from pydantic import BaseModel, ConfigDict
from app.models.seller import FssaiStatus


class FSSAISubmitRequest(BaseModel):
    fssai_number: str
    document_url: str


class FSSAIReviewRequest(BaseModel):
    status: FssaiStatus
    rejection_reason: Optional[str] = None


class FSSAIRecordOut(BaseModel):
    id: int
    seller_id: int
    fssai_number: str
    document_url: str
    status: FssaiStatus
    submitted_at: datetime
    verified_at: Optional[datetime] = None
    rejection_reason: Optional[str] = None
    created_at: datetime
    updated_at: datetime

    model_config = ConfigDict(from_attributes=True)
