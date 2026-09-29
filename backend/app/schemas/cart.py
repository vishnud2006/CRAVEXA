from datetime import datetime
from typing import List, Optional
from pydantic import BaseModel, ConfigDict, Field
from app.schemas.product import ProductOut


class CartItemAdd(BaseModel):
    product_id: int
    quantity: int = Field(default=1, gt=0)


class CartItemUpdate(BaseModel):
    quantity: int = Field(..., gt=0)


class CartItemOut(BaseModel):
    id: int
    product_id: int
    quantity: int
    created_at: datetime
    updated_at: datetime
    product: Optional[ProductOut] = None

    model_config = ConfigDict(from_attributes=True)


class CartOut(BaseModel):
    id: int
    customer_id: int
    items: List[CartItemOut] = []
    total_items: int = 0
    estimated_subtotal: float = 0.0
    created_at: datetime
    updated_at: datetime

    model_config = ConfigDict(from_attributes=True)
