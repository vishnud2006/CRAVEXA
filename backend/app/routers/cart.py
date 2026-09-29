from fastapi import APIRouter, Depends, status
from sqlalchemy.ext.asyncio import AsyncSession

from app.core.dependencies import require_authenticated_user
from app.database.session import get_db
from app.models.user import User
from app.schemas.cart import CartItemAdd, CartItemOut, CartItemUpdate, CartOut
from app.services.cart_service import CartService

router = APIRouter(prefix="/cart", tags=["Cart"])


def _build_cart_out(cart) -> CartOut:
    items_out = []
    total_items = 0
    estimated_subtotal = 0.0

    for item in cart.items:
        items_out.append(CartItemOut.model_validate(item))
        total_items += item.quantity
        if item.product:
            estimated_subtotal += item.product.price * item.quantity

    return CartOut(
        id=cart.id,
        customer_id=cart.customer_id,
        items=items_out,
        total_items=total_items,
        estimated_subtotal=round(estimated_subtotal, 2),
        created_at=cart.created_at,
        updated_at=cart.updated_at,
    )


@router.get("", response_model=CartOut)
async def get_my_cart(
    current_user: User = Depends(require_authenticated_user),
    db: AsyncSession = Depends(get_db),
):
    """Retrieve the current user's active shopping cart."""
    cart = await CartService.get_or_create_cart(db, current_user.id)
    return _build_cart_out(cart)


@router.post("/items", response_model=CartItemOut, status_code=status.HTTP_201_CREATED)
async def add_cart_item(
    item_in: CartItemAdd,
    current_user: User = Depends(require_authenticated_user),
    db: AsyncSession = Depends(get_db),
):
    """Add a product to the user's shopping cart with stock checking."""
    return await CartService.add_item(
        db,
        user_id=current_user.id,
        product_id=item_in.product_id,
        quantity=item_in.quantity,
    )


@router.put("/items/{item_id}", response_model=CartItemOut)
async def update_cart_item_quantity(
    item_id: int,
    item_in: CartItemUpdate,
    current_user: User = Depends(require_authenticated_user),
    db: AsyncSession = Depends(get_db),
):
    """Update item quantity in cart with stock validation."""
    return await CartService.update_item(
        db,
        user_id=current_user.id,
        item_id=item_id,
        quantity=item_in.quantity,
    )


@router.delete("/items/{item_id}", status_code=status.HTTP_204_NO_CONTENT)
async def remove_cart_item(
    item_id: int,
    current_user: User = Depends(require_authenticated_user),
    db: AsyncSession = Depends(get_db),
):
    """Remove a specific item from the shopping cart."""
    await CartService.remove_item(db, user_id=current_user.id, item_id=item_id)
    return None


@router.delete("", status_code=status.HTTP_204_NO_CONTENT)
async def clear_cart(
    current_user: User = Depends(require_authenticated_user),
    db: AsyncSession = Depends(get_db),
):
    """Clear all items from the current user's cart."""
    await CartService.clear_cart(db, user_id=current_user.id)
    return None
