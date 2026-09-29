import pytest
from httpx import AsyncClient
from app.models.category import Category
from app.models.product import Product


@pytest.mark.asyncio
async def test_seller_product_creation_defaults_to_pending(
    client: AsyncClient, seller_token: str, sample_category: Category
):
    prod_payload = {
        "category_id": sample_category.id,
        "name": "Homemade Spicy Garlic Pickle",
        "slug": "homemade-spicy-garlic-pickle",
        "description": "Slow cooked authentic garlic pickle with cold pressed mustard oil.",
        "price": 280.0,
        "original_price": 320.0,
        "weight": "400g",
        "stock": 25,
        "availability": True,
        "region": "Rajasthan",
        "ingredients": "Garlic, Mustard Oil, Fenugreek, Red Chilli",
    }
    create_res = await client.post(
        "/sellers/me/products",
        headers={"Authorization": f"Bearer {seller_token}"},
        json=prod_payload,
    )
    assert create_res.status_code == 201
    prod_data = create_res.json()
    assert prod_data["status"] == "PENDING"
    product_id = prod_data["id"]

    # Not visible in public catalog
    pub_res = await client.get("/products")
    assert pub_res.status_code == 200
    pub_ids = [p["id"] for p in pub_res.json()]
    assert product_id not in pub_ids


@pytest.mark.asyncio
async def test_admin_product_approval_makes_it_public(
    client: AsyncClient, seller_token: str, admin_token: str, sample_category: Category
):
    # 1. Seller creates product
    prod_payload = {
        "category_id": sample_category.id,
        "name": "Artisanal Mysore Pak",
        "slug": "artisanal-mysore-pak",
        "description": "Melt-in-mouth traditional ghee Mysore Pak.",
        "price": 450.0,
        "stock": 40,
        "availability": True,
    }
    create_res = await client.post(
        "/sellers/me/products",
        headers={"Authorization": f"Bearer {seller_token}"},
        json=prod_payload,
    )
    assert create_res.status_code == 201
    product_id = create_res.json()["id"]

    # 2. Admin approves it
    admin_approval_res = await client.put(
        f"/admin/products/{product_id}/status",
        headers={"Authorization": f"Bearer {admin_token}"},
        json={"status": "APPROVED"},
    )
    assert admin_approval_res.status_code == 200
    assert admin_approval_res.json()["status"] == "APPROVED"

    # 3. Now visible in public catalog
    pub_res = await client.get("/products")
    assert pub_res.status_code == 200
    pub_ids = [p["id"] for p in pub_res.json()]
    assert product_id in pub_ids

    # 4. Searchable by name
    search_res = await client.get("/products/search?q=Mysore")
    assert search_res.status_code == 200
    search_ids = [p["id"] for p in search_res.json()]
    assert product_id in search_ids
