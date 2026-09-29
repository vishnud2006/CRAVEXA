from datetime import datetime
from typing import List, Optional
from pydantic import BaseModel, ConfigDict, Field
from app.models.product import ProductStatus


class ProductImageBase(BaseModel):
    image_url: str
    sort_order: int = 0


class ProductImageCreate(ProductImageBase):
    pass


class ProductImageOut(ProductImageBase):
    id: int
    created_at: datetime

    model_config = ConfigDict(from_attributes=True)


class ProductBase(BaseModel):
    category_id: int
    name: str
    slug: str
    description: str
    price: float = Field(..., gt=0)
    original_price: Optional[float] = None
    weight: Optional[str] = None
    ingredients: Optional[str] = None
    shelf_life: Optional[str] = None
    storage_instructions: Optional[str] = None
    region: Optional[str] = None
    stock: int = Field(default=0, ge=0)
    availability: bool = True


class ProductCreate(ProductBase):
    images: Optional[List[ProductImageCreate]] = []


class ProductUpdate(BaseModel):
    category_id: Optional[int] = None
    name: Optional[str] = None
    slug: Optional[str] = None
    description: Optional[str] = None
    price: Optional[float] = Field(None, gt=0)
    original_price: Optional[float] = None
    weight: Optional[str] = None
    ingredients: Optional[str] = None
    shelf_life: Optional[str] = None
    storage_instructions: Optional[str] = None
    region: Optional[str] = None
    stock: Optional[int] = Field(None, ge=0)
    availability: Optional[bool] = None
    images: Optional[List[ProductImageCreate]] = None


class ProductOut(ProductBase):
    id: int
    seller_id: int
    status: ProductStatus
    created_at: datetime
    updated_at: datetime
    images: List[ProductImageOut] = []

    model_config = ConfigDict(from_attributes=True)


class ProductStatusUpdate(BaseModel):
    status: ProductStatus
