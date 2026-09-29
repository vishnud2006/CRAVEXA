from typing import List
from fastapi import APIRouter, Depends, HTTPException, Query, status
from sqlalchemy import or_, select
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy.orm import selectinload

from app.database.session import get_db
from app.models.category import Category
from app.models.product import Product, ProductStatus
from app.schemas.category import CategoryOut
from app.schemas.product import ProductOut

router = APIRouter(prefix="/categories", tags=["Categories"])


@router.get("", response_model=List[CategoryOut])
async def list_categories(
    db: AsyncSession = Depends(get_db),
):
    """List all active CRAVEXA categories."""
    result = await db.execute(
        select(Category)
        .where(Category.is_active.is_(True))
        .order_by(Category.name.asc())
    )
    return result.scalars().all()


@router.get("/{identifier}", response_model=CategoryOut)
async def get_category(
    identifier: str,
    db: AsyncSession = Depends(get_db),
):
    """Retrieve category details by ID or slug."""
    if identifier.isdigit():
        query = select(Category).where(
            Category.id == int(identifier), Category.is_active.is_(True)
        )
    else:
        query = select(Category).where(
            Category.slug == identifier, Category.is_active.is_(True)
        )

    result = await db.execute(query)
    category = result.scalar_one_or_none()
    if not category:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="Category not found",
        )
    return category


@router.get("/{category_id}/products", response_model=List[ProductOut])
async def get_category_products(
    category_id: int,
    skip: int = Query(0, ge=0),
    limit: int = Query(20, ge=1, le=100),
    db: AsyncSession = Depends(get_db),
):
    """Retrieve approved products in a specific category."""
    result = await db.execute(
        select(Product)
        .options(selectinload(Product.images))
        .where(
            Product.category_id == category_id,
            Product.status == ProductStatus.APPROVED,
            Product.availability.is_(True),
        )
        .order_by(Product.created_at.desc())
        .offset(skip)
        .limit(limit)
    )
    return result.scalars().all()
