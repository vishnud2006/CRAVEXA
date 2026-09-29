from datetime import datetime
from typing import Optional
from pydantic import BaseModel, ConfigDict


class BannerBase(BaseModel):
    title: str
    subtitle: Optional[str] = None
    image_url: str
    action: Optional[str] = None
    start_date: Optional[datetime] = None
    end_date: Optional[datetime] = None
    is_active: bool = True


class BannerCreate(BannerBase):
    pass


class BannerOut(BannerBase):
    id: int
    created_at: datetime
    updated_at: datetime

    model_config = ConfigDict(from_attributes=True)
