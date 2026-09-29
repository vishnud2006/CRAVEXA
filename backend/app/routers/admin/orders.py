from typing import List, Optional
from fastapi import APIRouter, Depends, HTTPException, Query, status
from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy.orm import selectinload

from app.core.dependencies import require_admin
from app.database.session import get_db
from app.models.order import Order, OrderStatus
from app.models.user import User
from app.schemas.order import OrderOut, OrderStatusUpdateRequest
from app.services.order_service import OrderService

router = APIRouter(prefix="/orders", tags=["Admin - Orders"])


@router.get("", response_model=List[OrderOut])
async def list_all_orders(
    order_status: Optional[OrderStatus] = Query(None, alias="status", description="Filter by order status"),
    customer_id: Optional[int] = Query(None, description="Filter by customer ID"),
    skip: int = Query(0, ge=0),
    limit: int = Query(20, ge=1, le=100),
    admin: User = Depends(require_admin),
    db: AsyncSession = Depends(get_db),
):
    """Admin endpoint to view and filter all platform orders."""
    query = (
        select(Order)
        .options(
            selectinload(Order.items),
            selectinload(Order.tracking_events),
        )
    )
    if order_status:
        query = query.where(Order.order_status == order_status)
    if customer_id:
        query = query.where(Order.customer_id == customer_id)

    query = query.order_by(Order.created_at.desc()).offset(skip).limit(limit)
    result = await db.execute(query)
    return result.scalars().all()


@router.get("/{order_id}", response_model=OrderOut)
async def get_order_detail(
    order_id: int,
    admin: User = Depends(require_admin),
    db: AsyncSession = Depends(get_db),
):
    """Admin endpoint to view complete order details with tracking events and items."""
    result = await db.execute(
        select(Order)
        .options(
            selectinload(Order.items),
            selectinload(Order.tracking_events),
        )
        .where(Order.id == order_id)
    )
    order = result.scalar_one_or_none()
    if not order:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="Order not found",
        )
    return order


@router.put("/{order_id}/status", response_model=OrderOut)
async def update_order_status_admin(
    order_id: int,
    status_update: OrderStatusUpdateRequest,
    admin: User = Depends(require_admin),
    db: AsyncSession = Depends(get_db),
):
    """
    Admin workflow to transition order status across the lifecycle:
    ORDER_PLACED -> PAYMENT_CONFIRMED -> PREPARING -> READY_FOR_PICKUP ->
    PICKED_UP -> IN_TRANSIT -> OUT_FOR_DELIVERY -> DELIVERED -> REFUNDED.
    """
    return await OrderService.update_order_status(
        db=db,
        order_id=order_id,
        new_status=status_update.status,
        description=status_update.description,
        is_admin=True,
    )
