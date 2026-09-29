from typing import List, Optional
from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy.orm import selectinload

from app.core.dependencies import require_authenticated_user
from app.database.session import get_db
from app.models.product import Product
from app.models.user import User
from app.models.wishlist import WishlistItem
from app.schemas.wishlist import WishlistAddRequest, WishlistItemOut

router = APIRouter(prefix="/wishlist", tags=["Wishlist"])


@router.get("", response_model=List[WishlistItemOut])
async def get_my_wishlist(
    current_user: User = Depends(require_authenticated_user),
    db: AsyncSession = Depends(get_db),
):
    """Retrieve all items in the authenticated user's wishlist."""
    result = await db.execute(
        select(WishlistItem)
        .options(
            selectinload(WishlistItem.product).selectinload(Product.images)
        )
        .where(WishlistItem.customer_id == current_user.id)
        .order_by(WishlistItem.created_at.desc())
    )
    return result.scalars().all()


@router.post("/{product_id}", response_model=WishlistItemOut, status_code=status.HTTP_201_CREATED)
async def add_to_wishlist(
    product_id: int,
    current_user: User = Depends(require_authenticated_user),
    db: AsyncSession = Depends(get_db),
):
    """Add a product to the user's wishlist."""
    # Check product existence
    prod_res = await db.execute(
        select(Product).options(selectinload(Product.images)).where(Product.id == product_id)
    )
    product = prod_res.scalar_one_or_none()
    if not product:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="Product not found",
        )

    # Check already in wishlist
    existing_res = await db.execute(
        select(WishlistItem).where(
            WishlistItem.customer_id == current_user.id,
            WishlistItem.product_id == product_id,
        )
    )
    item = existing_res.scalar_one_or_none()
    if not item:
        item = WishlistItem(
            customer_id=current_user.id,
            product_id=product_id,
        )
        db.add(item)
        await db.commit()
        await db.refresh(item)

    item.product = product
    return item


@router.delete("/{product_id}", status_code=status.HTTP_204_NO_CONTENT)
async def remove_from_wishlist(
    product_id: int,
    current_user: User = Depends(require_authenticated_user),
    db: AsyncSession = Depends(get_db),
):
    """Remove a product from the user's wishlist."""
    result = await db.execute(
        select(WishlistItem).where(
            WishlistItem.customer_id == current_user.id,
            WishlistItem.product_id == product_id,
        )
    )
    item = result.scalar_one_or_none()
    if item:
        await db.delete(item)
        await db.commit()

    return None
