from datetime import datetime
from typing import Optional
from pydantic import BaseModel, ConfigDict, Field
from app.models.review import ReviewStatus


class ReviewCreateRequest(BaseModel):
    product_id: int
    order_id: int
    rating: int = Field(..., ge=1, le=5)
    comment: Optional[str] = None
    image_url: Optional[str] = None


class ReviewResponseRequest(BaseModel):
    seller_response: str


class ReviewOut(BaseModel):
    id: int
    customer_id: int
    product_id: int
    seller_id: int
    order_id: int
    rating: int
    comment: Optional[str] = None
    image_url: Optional[str] = None
    seller_response: Optional[str] = None
    status: ReviewStatus
    created_at: datetime
    updated_at: datetime

    model_config = ConfigDict(from_attributes=True)
