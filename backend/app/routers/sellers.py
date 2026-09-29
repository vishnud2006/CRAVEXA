from typing import List, Optional
from fastapi import APIRouter, Depends, HTTPException, Query, status
from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy.orm import selectinload

from app.core.dependencies import get_current_seller_profile, require_seller
from app.database.session import get_db
from app.models.fssai import FSSAIRecord
from app.models.order import Order, OrderItem, OrderStatus, OrderTrackingEvent
from app.models.product import Product, ProductImage, ProductStatus
from app.models.seller import FssaiStatus, SellerProfile, SellerStatus
from app.models.user import User
from app.schemas.fssai import FSSAIRecordOut, FSSAISubmitRequest
from app.schemas.order import OrderOut, OrderStatusUpdateRequest
from app.schemas.product import ProductCreate, ProductOut, ProductUpdate
from app.schemas.seller import SellerProfileOut, SellerProfileUpdate
from app.services.product_service import ProductService

router = APIRouter(prefix="/sellers", tags=["Sellers"])


# --- Authenticated Seller Endpoints ---

@router.get("/me", response_model=SellerProfileOut)
async def get_my_seller_profile(
    seller: SellerProfile = Depends(get_current_seller_profile),
):
    """Retrieve the authenticated seller's profile."""
    return seller


@router.put("/me", response_model=SellerProfileOut)
async def update_my_seller_profile(
    profile_in: SellerProfileUpdate,
    seller: SellerProfile = Depends(get_current_seller_profile),
    db: AsyncSession = Depends(get_db),
):
    """Update the authenticated seller's profile details."""
    update_data = profile_in.model_dump(exclude_unset=True)
    for field, val in update_data.items():
        setattr(seller, field, val)

    await db.commit()
    await db.refresh(seller)
    return seller


@router.get("/me/status")
async def get_my_seller_status(
    seller: SellerProfile = Depends(get_current_seller_profile),
):
    """Get the current seller's verification and approval statuses."""
    return {
        "seller_id": seller.id,
        "business_name": seller.business_name,
        "seller_status": seller.seller_status,
        "fssai_status": seller.fssai_status,
        "fssai_number": seller.fssai_number,
    }


@router.post("/me/fssai", response_model=FSSAIRecordOut)
async def submit_fssai_record(
    fssai_in: FSSAISubmitRequest,
    seller: SellerProfile = Depends(get_current_seller_profile),
    db: AsyncSession = Depends(get_db),
):
    """
    Submit or update FSSAI documentation for verification.
    Sets status to PENDING. Verification can only be done by Admin.
    """
    record = FSSAIRecord(
        seller_id=seller.id,
        fssai_number=fssai_in.fssai_number,
        document_url=fssai_in.document_url,
        status=FssaiStatus.PENDING,
    )
    db.add(record)

    seller.fssai_number = fssai_in.fssai_number
    seller.fssai_status = FssaiStatus.PENDING

    await db.commit()
    await db.refresh(record)
    return record


@router.get("/me/fssai", response_model=List[FSSAIRecordOut])
async def get_my_fssai_records(
    seller: SellerProfile = Depends(get_current_seller_profile),
    db: AsyncSession = Depends(get_db),
):
    """List all FSSAI submission records for the authenticated seller."""
    result = await db.execute(
        select(FSSAIRecord)
        .where(FSSAIRecord.seller_id == seller.id)
        .order_by(FSSAIRecord.created_at.desc())
    )
    return result.scalars().all()


@router.get("/me/products", response_model=List[ProductOut])
async def get_my_products(
    seller: SellerProfile = Depends(get_current_seller_profile),
    db: AsyncSession = Depends(get_db),
):
    """List all products owned by the authenticated seller across all statuses."""
    result = await db.execute(
        select(Product)
        .options(selectinload(Product.images))
        .where(Product.seller_id == seller.id)
        .order_by(Product.created_at.desc())
    )
    return result.scalars().all()


@router.post("/me/products", response_model=ProductOut, status_code=status.HTTP_201_CREATED)
async def create_product(
    product_in: ProductCreate,
    seller: SellerProfile = Depends(get_current_seller_profile),
    db: AsyncSession = Depends(get_db),
):
    """
    Create a new product for the authenticated seller.
    New products default to PENDING status and require Admin approval before going live.
    """
    return await ProductService.create_product(db, seller.id, product_in)


@router.put("/me/products/{product_id}", response_model=ProductOut)
async def update_product(
    product_id: int,
    product_in: ProductUpdate,
    seller: SellerProfile = Depends(get_current_seller_profile),
    db: AsyncSession = Depends(get_db),
):
    """Update a product owned by the authenticated seller."""
    return await ProductService.update_product(db, product_id, seller.id, product_in)


@router.delete("/me/products/{product_id}", status_code=status.HTTP_204_NO_CONTENT)
async def delete_product(
    product_id: int,
    seller: SellerProfile = Depends(get_current_seller_profile),
    db: AsyncSession = Depends(get_db),
):
    """Delete a product owned by the authenticated seller."""
    await ProductService.delete_product(db, product_id, seller.id)
    return None


@router.get("/me/orders", response_model=List[OrderOut])
async def get_my_seller_orders(
    seller: SellerProfile = Depends(get_current_seller_profile),
    db: AsyncSession = Depends(get_db),
):
    """List orders that contain items belonging to this seller."""
    result = await db.execute(
        select(Order)
        .join(OrderItem, OrderItem.order_id == Order.id)
        .where(OrderItem.seller_id == seller.id)
        .distinct()
        .options(
            selectinload(Order.items),
            selectinload(Order.tracking_events),
        )
        .order_by(Order.created_at.desc())
    )
    return result.scalars().all()


@router.put("/me/orders/{order_id}/status", response_model=OrderOut)
async def update_seller_order_status(
    order_id: int,
    status_update: OrderStatusUpdateRequest,
    seller: SellerProfile = Depends(get_current_seller_profile),
    db: AsyncSession = Depends(get_db),
):
    """
    Update order fulfillment status by the seller (e.g. PROCESSING, SHIPPED).
    Validates that the order actually contains items belonging to this seller.
    """
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

    seller_items = [item for item in order.items if item.seller_id == seller.id]
    if not seller_items and order.seller_id != seller.id:
        raise HTTPException(
            status_code=status.HTTP_403_FORBIDDEN,
            detail="You are not authorized to update this order",
        )

    # Transition status
    order.order_status = status_update.status
    tracking_event = OrderTrackingEvent(
        order_id=order.id,
        status=status_update.status,
        description=status_update.description or f"Order updated to {status_update.status.value} by seller",
    )
    db.add(tracking_event)
    await db.commit()
    await db.refresh(order)
    return order


# --- Public Seller Endpoints ---

@router.get("/{seller_id}", response_model=SellerProfileOut)
async def get_public_seller_profile(
    seller_id: int,
    db: AsyncSession = Depends(get_db),
):
    """Get public seller store profile (only visible if APPROVED)."""
    result = await db.execute(
        select(SellerProfile).where(
            SellerProfile.id == seller_id,
            SellerProfile.seller_status == SellerStatus.APPROVED,
        )
    )
    seller = result.scalar_one_or_none()
    if not seller:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="Seller not found or not currently active",
        )
    return seller


@router.get("/{seller_id}/products", response_model=List[ProductOut])
async def get_public_seller_products(
    seller_id: int,
    skip: int = Query(0, ge=0),
    limit: int = Query(20, ge=1, le=100),
    db: AsyncSession = Depends(get_db),
):
    """Get active/approved products of a specific seller for customer storefront."""
    result = await db.execute(
        select(Product)
        .options(selectinload(Product.images))
        .where(
            Product.seller_id == seller_id,
            Product.status == ProductStatus.APPROVED,
            Product.availability.is_(True),
        )
        .offset(skip)
        .limit(limit)
    )
    return result.scalars().all()
