from typing import Optional
from fastapi import HTTPException, status
from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy.orm import selectinload

from app.models.cart import Cart, CartItem
from app.models.product import Product, ProductStatus


class CartService:
    @staticmethod
    async def get_or_create_cart(db: AsyncSession, user_id: int) -> Cart:
        result = await db.execute(
            select(Cart)
            .options(
                selectinload(Cart.items).selectinload(CartItem.product).selectinload(Product.images)
            )
            .where(Cart.customer_id == user_id)
        )
        cart = result.scalar_one_or_none()
        if not cart:
            cart = Cart(customer_id=user_id)
            db.add(cart)
            await db.commit()
            await db.refresh(cart)
            cart.items = []
        return cart

    @staticmethod
    async def add_item(
        db: AsyncSession,
        user_id: int,
        product_id: int,
        quantity: int
    ) -> CartItem:
        # Validate product
        prod_result = await db.execute(
            select(Product).options(selectinload(Product.images)).where(Product.id == product_id)
        )
        product = prod_result.scalar_one_or_none()

        if not product:
            raise HTTPException(
                status_code=status.HTTP_404_NOT_FOUND,
                detail="Product not found",
            )

        if product.status != ProductStatus.APPROVED or not product.availability:
            raise HTTPException(
                status_code=status.HTTP_400_BAD_REQUEST,
                detail="This product is currently not available for purchase",
            )

        cart = await CartService.get_or_create_cart(db, user_id)

        # Check existing item
        existing_item_res = await db.execute(
            select(CartItem).where(CartItem.cart_id == cart.id, CartItem.product_id == product_id)
        )
        cart_item = existing_item_res.scalar_one_or_none()

        target_qty = quantity if not cart_item else (cart_item.quantity + quantity)
        if product.stock < target_qty:
            raise HTTPException(
                status_code=status.HTTP_400_BAD_REQUEST,
                detail=f"Requested quantity ({target_qty}) exceeds available stock ({product.stock})",
            )

        if cart_item:
            cart_item.quantity = target_qty
        else:
            cart_item = CartItem(
                cart_id=cart.id,
                product_id=product_id,
                quantity=quantity
            )
            db.add(cart_item)

        await db.commit()
        await db.refresh(cart_item)
        cart_item.product = product
        return cart_item

    @staticmethod
    async def update_item(
        db: AsyncSession,
        user_id: int,
        item_id: int,
        quantity: int
    ) -> CartItem:
        cart = await CartService.get_or_create_cart(db, user_id)
        result = await db.execute(
            select(CartItem)
            .options(selectinload(CartItem.product).selectinload(Product.images))
            .where(CartItem.id == item_id, CartItem.cart_id == cart.id)
        )
        item = result.scalar_one_or_none()

        if not item:
            raise HTTPException(
                status_code=status.HTTP_404_NOT_FOUND,
                detail="Cart item not found in your cart",
            )

        if item.product.stock < quantity:
            raise HTTPException(
                status_code=status.HTTP_400_BAD_REQUEST,
                detail=f"Requested quantity ({quantity}) exceeds available stock ({item.product.stock})",
            )

        item.quantity = quantity
        await db.commit()
        await db.refresh(item)
        return item

    @staticmethod
    async def remove_item(
        db: AsyncSession,
        user_id: int,
        item_id: int
    ) -> None:
        cart = await CartService.get_or_create_cart(db, user_id)
        result = await db.execute(
            select(CartItem).where(CartItem.id == item_id, CartItem.cart_id == cart.id)
        )
        item = result.scalar_one_or_none()

        if not item:
            raise HTTPException(
                status_code=status.HTTP_404_NOT_FOUND,
                detail="Cart item not found",
            )

        await db.delete(item)
        await db.commit()

    @staticmethod
    async def clear_cart(db: AsyncSession, user_id: int) -> None:
        cart = await CartService.get_or_create_cart(db, user_id)
        for item in cart.items:
            await db.delete(item)
        await db.commit()
