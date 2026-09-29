import uuid
from typing import Optional
from fastapi import APIRouter, Depends, HTTPException, status
from pydantic import BaseModel
from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession

from app.core.dependencies import require_authenticated_user
from app.database.session import get_db
from app.models.order import Order, OrderStatus, PaymentStatus
from app.models.payment import Payment
from app.models.user import User, UserRole
from app.schemas.payment import PaymentOut

router = APIRouter(prefix="/payments", tags=["Payments"])


class PaymentIntentRequest(BaseModel):
    order_id: int


class PaymentIntentResponse(BaseModel):
    order_id: int
    amount: float
    currency: str = "INR"
    client_order_id: str
    status: str


class PaymentVerifyRequest(BaseModel):
    order_id: int
    provider_payment_id: str


@router.get("/{order_id}", response_model=PaymentOut)
async def get_order_payment(
    order_id: int,
    current_user: User = Depends(require_authenticated_user),
    db: AsyncSession = Depends(get_db),
):
    """Retrieve payment details for a specific order."""
    order_res = await db.execute(select(Order).where(Order.id == order_id))
    order = order_res.scalar_one_or_none()
    if not order:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="Order not found",
        )

    if current_user.role != UserRole.ADMIN and order.customer_id != current_user.id:
        raise HTTPException(
            status_code=status.HTTP_403_FORBIDDEN,
            detail="Forbidden",
        )

    pay_res = await db.execute(select(Payment).where(Payment.order_id == order_id))
    payment = pay_res.scalar_one_or_none()
    if not payment:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="Payment record not found for this order",
        )
    return payment


@router.post("/create-intent", response_model=PaymentIntentResponse)
async def create_payment_intent(
    data: PaymentIntentRequest,
    current_user: User = Depends(require_authenticated_user),
    db: AsyncSession = Depends(get_db),
):
    """
    Phase 7A Payment Intent stub:
    Initializes a transaction reference for customer checkout without executing external charges.
    """
    order_res = await db.execute(
        select(Order).where(Order.id == data.order_id, Order.customer_id == current_user.id)
    )
    order = order_res.scalar_one_or_none()
    if not order:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="Order not found or does not belong to you",
        )

    intent_id = f"intent_{uuid.uuid4().hex[:12]}"
    return PaymentIntentResponse(
        order_id=order.id,
        amount=order.total,
        currency="INR",
        client_order_id=intent_id,
        status="created",
    )


@router.post("/verify", response_model=PaymentOut)
async def verify_payment(
    data: PaymentVerifyRequest,
    current_user: User = Depends(require_authenticated_user),
    db: AsyncSession = Depends(get_db),
):
    """
    Verify payment completion and update order status to PAYMENT_CONFIRMED.
    """
    order_res = await db.execute(
        select(Order).where(Order.id == data.order_id, Order.customer_id == current_user.id)
    )
    order = order_res.scalar_one_or_none()
    if not order:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="Order not found",
        )

    pay_res = await db.execute(select(Payment).where(Payment.order_id == data.order_id))
    payment = pay_res.scalar_one_or_none()
    if not payment:
        payment = Payment(
            order_id=order.id,
            provider="ONLINE",
            amount=order.total,
            status=PaymentStatus.COMPLETED,
            provider_payment_id=data.provider_payment_id,
        )
        db.add(payment)
    else:
        payment.status = PaymentStatus.COMPLETED
        payment.provider_payment_id = data.provider_payment_id

    order.payment_status = PaymentStatus.COMPLETED
    if order.order_status == OrderStatus.ORDER_PLACED:
        order.order_status = OrderStatus.PAYMENT_CONFIRMED

    await db.commit()
    await db.refresh(payment)
    return payment
