import re
from typing import List, Optional
from fastapi import HTTPException, status
from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy.orm import selectinload

from app.models.category import Category
from app.models.product import Product, ProductImage, ProductStatus
from app.models.seller import SellerProfile
from app.schemas.product import ProductCreate, ProductUpdate


class ProductService:
    @staticmethod
    def slugify(text: str) -> str:
        text = text.lower().strip()
        text = re.sub(r'[^\w\s-]', '', text)
        return re.sub(r'[-\s]+', '-', text)

    @staticmethod
    async def create_product(
        db: AsyncSession,
        seller: SellerProfile,
        data: ProductCreate
    ) -> Product:
        # Validate category
        cat_result = await db.execute(
            select(Category).where(Category.id == data.category_id, Category.is_active.is_(True))
        )
        if not cat_result.scalar_one_or_none():
            raise HTTPException(
                status_code=status.HTTP_404_NOT_FOUND,
                detail=f"Category with ID {data.category_id} not found or inactive",
            )

        slug = data.slug.strip() if data.slug else ProductService.slugify(data.name)
        seller_id = seller.id if hasattr(seller, "id") else int(seller)
        product = Product(
            seller_id=seller_id,
            category_id=data.category_id,
            name=data.name.strip(),
            slug=slug,
            description=data.description.strip(),
            price=data.price,
            original_price=data.original_price,
            weight=data.weight,
            ingredients=data.ingredients,
            shelf_life=data.shelf_life,
            storage_instructions=data.storage_instructions,
            region=data.region,
            stock=data.stock,
            availability=data.availability,
            status=ProductStatus.PENDING,  # Seller products always start as PENDING for admin review
        )
        db.add(product)
        await db.flush()

        if data.images:
            for idx, img in enumerate(data.images):
                p_img = ProductImage(
                    product_id=product.id,
                    image_url=img.image_url,
                    sort_order=img.sort_order if img.sort_order else idx
                )
                db.add(p_img)

        await db.commit()
        res = await db.execute(
            select(Product).options(selectinload(Product.images)).where(Product.id == product.id)
        )
        return res.scalar_one()

    @staticmethod
    async def update_seller_product(
        db: AsyncSession,
        product_id: int,
        seller: SellerProfile,
        data: ProductUpdate
    ) -> Product:
        result = await db.execute(
            select(Product).options(selectinload(Product.images)).where(Product.id == product_id)
        )
        product = result.scalar_one_or_none()

        if not product:
            raise HTTPException(
                status_code=status.HTTP_404_NOT_FOUND,
                detail="Product not found",
            )

        seller_id = seller.id if hasattr(seller, "id") else int(seller)
        if product.seller_id != seller_id:
            raise HTTPException(
                status_code=status.HTTP_403_FORBIDDEN,
                detail="Forbidden: You do not have permission to modify this product",
            )

        update_dict = data.model_dump(exclude_unset=True)
        images = update_dict.pop("images", None)

        for key, value in update_dict.items():
            setattr(product, key, value)

        if images is not None:
            # Replace images
            for existing_img in product.images:
                await db.delete(existing_img)
            for idx, img in enumerate(images):
                p_img = ProductImage(
                    product_id=product.id,
                    image_url=img["image_url"],
                    sort_order=img.get("sort_order", idx)
                )
                db.add(p_img)

        await db.commit()
        res = await db.execute(
            select(Product).options(selectinload(Product.images)).where(Product.id == product.id)
        )
        return res.scalar_one()

    @staticmethod
    async def delete_seller_product(
        db: AsyncSession,
        product_id: int,
        seller: object
    ) -> None:
        result = await db.execute(
            select(Product).where(Product.id == product_id)
        )
        product = result.scalar_one_or_none()

        if not product:
            raise HTTPException(
                status_code=status.HTTP_404_NOT_FOUND,
                detail="Product not found",
            )

        seller_id = seller.id if hasattr(seller, "id") else int(seller)
        if product.seller_id != seller_id:
            raise HTTPException(
                status_code=status.HTTP_403_FORBIDDEN,
                detail="Forbidden: You do not have permission to delete this product",
            )

        await db.delete(product)
        await db.commit()

    update_product = update_seller_product
    delete_product = delete_seller_product
