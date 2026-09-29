from datetime import datetime
from typing import Optional
from pydantic import BaseModel, ConfigDict, Field
from app.models.coupon import DiscountType


class CouponBase(BaseModel):
    code: str
    discount_type: DiscountType
    discount_value: float = Field(..., gt=0)
    minimum_order: float = Field(default=0.0, ge=0)
    maximum_discount: Optional[float] = None
    usage_limit: Optional[int] = None
    start_date: datetime
    end_date: datetime
    is_active: bool = True


class CouponCreate(CouponBase):
    pass


class CouponOut(CouponBase):
    id: int
    times_used: int
    created_at: datetime
    updated_at: datetime

    model_config = ConfigDict(from_attributes=True)
