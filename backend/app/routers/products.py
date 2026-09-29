from typing import List, Optional
from fastapi import APIRouter, Depends, HTTPException, Query, status
from sqlalchemy import or_, select
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy.orm import selectinload

from app.database.session import get_db
from app.models.product import Product, ProductImage, ProductStatus
from app.schemas.product import ProductOut

router = APIRouter(prefix="/products", tags=["Products"])


@router.get("", response_model=List[ProductOut])
async def list_products(
    category_id: Optional[int] = Query(None, description="Filter by category ID"),
    min_price: Optional[float] = Query(None, ge=0, description="Minimum price filter"),
    max_price: Optional[float] = Query(None, ge=0, description="Maximum price filter"),
    region: Optional[str] = Query(None, description="Filter by regional cuisine / origin"),
    sort_by: Optional[str] = Query(
        "newest",
        description="Sort by: 'price_asc', 'price_desc', 'newest'",
    ),
    skip: int = Query(0, ge=0),
    limit: int = Query(20, ge=1, le=100),
    db: AsyncSession = Depends(get_db),
):
    """
    Public product catalog.
    Lists all APPROVED and available products with category, price range, and regional filters.
    """
    query = (
        select(Product)
        .options(selectinload(Product.images))
        .where(
            Product.status == ProductStatus.APPROVED,
            Product.availability.is_(True),
        )
    )

    if category_id is not None:
        query = query.where(Product.category_id == category_id)

    if min_price is not None:
        query = query.where(Product.price >= min_price)

    if max_price is not None:
        query = query.where(Product.price <= max_price)

    if region:
        query = query.where(Product.region.ilike(f"%{region}%"))

    if sort_by == "price_asc":
        query = query.order_by(Product.price.asc())
    elif sort_by == "price_desc":
        query = query.order_by(Product.price.desc())
    else:
        query = query.order_by(Product.created_at.desc())

    query = query.offset(skip).limit(limit)
    result = await db.execute(query)
    return result.scalars().all()


@router.get("/search", response_model=List[ProductOut])
async def search_products(
    q: str = Query(..., min_length=1, description="Search keyword"),
    skip: int = Query(0, ge=0),
    limit: int = Query(20, ge=1, le=100),
    db: AsyncSession = Depends(get_db),
):
    """Search approved products across title, description, region, and ingredients."""
    search_pattern = f"%{q}%"
    query = (
        select(Product)
        .options(selectinload(Product.images))
        .where(
            Product.status == ProductStatus.APPROVED,
            Product.availability.is_(True),
            or_(
                Product.name.ilike(search_pattern),
                Product.description.ilike(search_pattern),
                Product.region.ilike(search_pattern),
                Product.ingredients.ilike(search_pattern),
            ),
        )
        .order_by(Product.created_at.desc())
        .offset(skip)
        .limit(limit)
    )
    result = await db.execute(query)
    return result.scalars().all()


@router.get("/{product_id}", response_model=ProductOut)
async def get_product(
    product_id: int,
    db: AsyncSession = Depends(get_db),
):
    """Retrieve detailed information and images for a specific product."""
    result = await db.execute(
        select(Product)
        .options(selectinload(Product.images))
        .where(Product.id == product_id)
    )
    product = result.scalar_one_or_none()
    if not product:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="Product not found",
        )
    return product
