from datetime import datetime
from typing import Optional
from pydantic import BaseModel, ConfigDict
from app.schemas.product import ProductOut


class WishlistAddRequest(BaseModel):
    product_id: int


class WishlistItemOut(BaseModel):
    id: int
    customer_id: int
    product_id: int
    created_at: datetime
    product: Optional[ProductOut] = None

    model_config = ConfigDict(from_attributes=True)
