from datetime import datetime
from typing import Any, Dict, List, Optional
from pydantic import BaseModel, ConfigDict, Field
from app.models.order import OrderStatus, PaymentStatus


class OrderItemRequest(BaseModel):
    product_id: int
    quantity: int = Field(default=1, gt=0)


class OrderCreateRequest(BaseModel):
    address_id: int
    payment_method: str = "COD"
    coupon_code: Optional[str] = None
    items: Optional[List[OrderItemRequest]] = None  # If empty, order is built from user's cart


class OrderItemOut(BaseModel):
    id: int
    order_id: int
    product_id: Optional[int] = None
    seller_id: int
    product_name_snapshot: str
    unit_price: float
    quantity: int
    total_price: float

    model_config = ConfigDict(from_attributes=True)


class OrderTrackingEventOut(BaseModel):
    id: int
    order_id: int
    status: OrderStatus
    description: str
    timestamp: datetime

    model_config = ConfigDict(from_attributes=True)


class OrderOut(BaseModel):
    id: int
    order_number: str
    customer_id: int
    seller_id: Optional[int] = None
    subtotal: float
    delivery_fee: float
    discount: float
    total: float
    address_snapshot: Dict[str, Any]
    payment_method: str
    payment_status: PaymentStatus
    order_status: OrderStatus
    created_at: datetime
    updated_at: datetime
    items: List[OrderItemOut] = []
    tracking_events: List[OrderTrackingEventOut] = []

    model_config = ConfigDict(from_attributes=True)


class OrderStatusUpdateRequest(BaseModel):
    status: OrderStatus
    description: Optional[str] = None
