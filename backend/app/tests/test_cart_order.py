import pytest
from httpx import AsyncClient
from app.models.product import Product


@pytest.mark.asyncio
async def test_cart_operations_and_stock_validation(
    client: AsyncClient, customer_token: str, sample_product: Product
):
    # 1. Add item within stock limits
    add_res = await client.post(
        "/cart/items",
        headers={"Authorization": f"Bearer {customer_token}"},
        json={"product_id": sample_product.id, "quantity": 2},
    )
    assert add_res.status_code == 201
    item_id = add_res.json()["id"]

    # 2. View cart
    cart_res = await client.get("/cart", headers={"Authorization": f"Bearer {customer_token}"})
    assert cart_res.status_code == 200
    cart_data = cart_res.json()
    assert cart_data["total_items"] == 2
    assert cart_data["estimated_subtotal"] == 500.0

    # 3. Attempt to add more than available stock (50 max)
    exceed_res = await client.post(
        "/cart/items",
        headers={"Authorization": f"Bearer {customer_token}"},
        json={"product_id": sample_product.id, "quantity": 100},
    )
    assert exceed_res.status_code == 400
    assert "exceeds available stock" in exceed_res.json()["detail"]


@pytest.mark.asyncio
async def test_authoritative_order_placement_and_stock_deduction(
    client: AsyncClient, customer_token: str, sample_product: Product
):
    initial_stock = sample_product.stock

    # 1. Create Address
    addr_payload = {
        "full_name": "Delivery Recipient",
        "phone": "9876543210",
        "address_line1": "Flat 304, Green Meadows",
        "city": "Bengaluru",
        "state": "Karnataka",
        "pincode": "560001",
        "address_type": "HOME",
        "is_default": True,
    }
    addr_res = await client.post(
        "/addresses",
        headers={"Authorization": f"Bearer {customer_token}"},
        json=addr_payload,
    )
    assert addr_res.status_code == 201
    address_id = addr_res.json()["id"]

    # 2. Place Order (client only supplies address_id and items with quantity, NO PRICES)
    order_payload = {
        "address_id": address_id,
        "payment_method": "COD",
        "items": [{"product_id": sample_product.id, "quantity": 3}],
    }
    order_res = await client.post(
        "/orders",
        headers={"Authorization": f"Bearer {customer_token}"},
        json=order_payload,
    )
    assert order_res.status_code == 201
    order_data = order_res.json()

    # Subtotal: 3 * 250 = 750
    # Delivery fee: 0.0 because >= 500
    assert order_data["subtotal"] == 750.0
    assert order_data["delivery_fee"] == 0.0
    assert order_data["total"] == 750.0
    assert order_data["order_status"] == "ORDER_PLACED"
    order_id = order_data["id"]

    # 3. Check stock deducted from product
    prod_res = await client.get(f"/products/{sample_product.id}")
    assert prod_res.status_code == 200
    assert prod_res.json()["stock"] == initial_stock - 3

    # 4. Customer cancels order -> stock restored
    cancel_res = await client.post(
        f"/orders/{order_id}/cancel",
        headers={"Authorization": f"Bearer {customer_token}"},
    )
    assert cancel_res.status_code == 200
    assert cancel_res.json()["order_status"] == "CANCELLED"

    # Verify restored stock
    prod_restored = await client.get(f"/products/{sample_product.id}")
    assert prod_restored.json()["stock"] == initial_stock
