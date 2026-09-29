from typing import Any, Dict, List
from pydantic import BaseModel


class AdminDashboardStats(BaseModel):
    total_users: int = 0
    total_customers: int
    total_sellers: int
    pending_sellers: int
    total_products: int
    pending_products: int
    total_orders: int
    total_revenue: float
    pending_refunds: int
    pending_complaints: int
    fssai_requests: int


class AdminReport(BaseModel):
    title: str
    generated_at: str
    metrics: Dict[str, Any]
