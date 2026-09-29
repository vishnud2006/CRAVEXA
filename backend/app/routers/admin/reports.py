from typing import Dict, Any
from fastapi import APIRouter, Depends
from sqlalchemy import func, select
from sqlalchemy.ext.asyncio import AsyncSession

from app.core.dependencies import require_admin
from app.database.session import get_db
from app.models.order import Order, OrderStatus
from app.models.seller import SellerProfile, SellerStatus
from app.models.user import User

router = APIRouter(prefix="/reports", tags=["Admin - Reports"])


@router.get("/sales", response_model=Dict[str, Any])
async def get_sales_report(
    admin: User = Depends(require_admin),
    db: AsyncSession = Depends(get_db),
):
    """Admin analytics report for orders, revenue, and fulfillment metrics."""
    total_orders = (await db.execute(select(func.count(Order.id)))).scalar() or 0
    delivered_orders = (
        await db.execute(select(func.count(Order.id)).where(Order.order_status == OrderStatus.DELIVERED))
    ).scalar() or 0
    cancelled_orders = (
        await db.execute(select(func.count(Order.id)).where(Order.order_status == OrderStatus.CANCELLED))
    ).scalar() or 0
    gross_revenue = (
        await db.execute(
            select(func.coalesce(func.sum(Order.total), 0.0)).where(Order.order_status == OrderStatus.DELIVERED)
        )
    ).scalar() or 0.0

    return {
        "total_orders": total_orders,
        "delivered_orders": delivered_orders,
        "cancelled_orders": cancelled_orders,
        "gross_revenue": float(gross_revenue),
        "fulfillment_rate_percent": round((delivered_orders / total_orders * 100), 2) if total_orders > 0 else 0.0,
    }
