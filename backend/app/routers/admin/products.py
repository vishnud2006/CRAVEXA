from typing import List, Optional
from fastapi import APIRouter, Depends, HTTPException, Query, status
from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy.orm import selectinload

from app.core.dependencies import require_admin
from app.database.session import get_db
from app.models.product import Product, ProductImage, ProductStatus
from app.models.user import User
from app.schemas.product import ProductOut, ProductStatusUpdate

router = APIRouter(prefix="/products", tags=["Admin - Products"])


@router.get("", response_model=List[ProductOut])
async def list_all_products(
    product_status: Optional[ProductStatus] = Query(None, alias="status", description="Filter by status"),
    category_id: Optional[int] = Query(None, description="Filter by category"),
    seller_id: Optional[int] = Query(None, description="Filter by seller"),
    skip: int = Query(0, ge=0),
    limit: int = Query(20, ge=1, le=100),
    admin: User = Depends(require_admin),
    db: AsyncSession = Depends(get_db),
):
    """
    Admin catalog management:
    View all products across all statuses (including PENDING review products).
    """
    query = select(Product).options(selectinload(Product.images))
    if product_status:
        query = query.where(Product.status == product_status)
    if category_id:
        query = query.where(Product.category_id == category_id)
    if seller_id:
        query = query.where(Product.seller_id == seller_id)

    query = query.order_by(Product.created_at.desc()).offset(skip).limit(limit)
    result = await db.execute(query)
    return result.scalars().all()


@router.put("/{product_id}/status", response_model=ProductOut)
async def update_product_status(
    product_id: int,
    status_update: ProductStatusUpdate,
    admin: User = Depends(require_admin),
    db: AsyncSession = Depends(get_db),
):
    """
    Admin moderation:
    Approve, reject, or disable a product.
    Only APPROVED products appear in customer listings and search.
    """
    result = await db.execute(
        select(Product).options(selectinload(Product.images)).where(Product.id == product_id)
    )
    product = result.scalar_one_or_none()
    if not product:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="Product not found",
        )

    product.status = status_update.status
    await db.commit()
    await db.refresh(product)
    return product
