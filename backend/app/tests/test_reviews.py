import pytest
from httpx import AsyncClient
from app.models.product import Product


@pytest.mark.asyncio
async def test_review_eligibility_and_verification(
    client: AsyncClient,
    customer_token: str,
    admin_token: str,
    seller_token: str,
    sample_product: Product,
):
    # 1. Attempt to review without purchasing
    fake_review_res = await client.post(
        "/reviews",
        headers={"Authorization": f"Bearer {customer_token}"},
        json={
            "product_id": sample_product.id,
            "order_id": 99999,
            "rating": 5,
            "comment": "Tastes amazing!",
        },
    )
    assert fake_review_res.status_code == 404

    # 2. Setup Address and create legitimate order
    addr_res = await client.post(
        "/addresses",
        headers={"Authorization": f"Bearer {customer_token}"},
        json={
            "full_name": "Reviewer",
            "phone": "9876543210",
            "address_line1": "100 Indiranagar",
            "city": "Bengaluru",
            "state": "Karnataka",
            "pincode": "560038",
            "address_type": "HOME",
            "is_default": True,
        },
    )
    address_id = addr_res.json()["id"]

    order_res = await client.post(
        "/orders",
        headers={"Authorization": f"Bearer {customer_token}"},
        json={
            "address_id": address_id,
            "items": [{"product_id": sample_product.id, "quantity": 1}],
        },
    )
    order_id = order_res.json()["id"]

    # 3. Attempt review while order is still in ORDER_PLACED -> rejected
    premature_review = await client.post(
        "/reviews",
        headers={"Authorization": f"Bearer {customer_token}"},
        json={
            "product_id": sample_product.id,
            "order_id": order_id,
            "rating": 5,
            "comment": "Haven't received it yet!",
        },
    )
    assert premature_review.status_code == 400
    assert "delivered orders" in premature_review.json()["detail"]

    # 4. Advance order to DELIVERED (simulating lifecycle)
    # Transitions: ORDER_PLACED -> PAYMENT_CONFIRMED -> PREPARING -> READY_FOR_PICKUP -> PICKED_UP -> IN_TRANSIT -> OUT_FOR_DELIVERY -> DELIVERED
    transitions = [
        "PAYMENT_CONFIRMED",
        "PREPARING",
        "READY_FOR_PICKUP",
        "PICKED_UP",
        "IN_TRANSIT",
        "OUT_FOR_DELIVERY",
        "DELIVERED",
    ]
    for status_step in transitions:
        await client.put(
            f"/admin/orders/{order_id}/status",
            headers={"Authorization": f"Bearer {admin_token}"},
            json={"status": status_step},
        )

    # 5. Now submit review for DELIVERED order -> succeeds
    review_res = await client.post(
        "/reviews",
        headers={"Authorization": f"Bearer {customer_token}"},
        json={
            "product_id": sample_product.id,
            "order_id": order_id,
            "rating": 5,
            "comment": "Authentic and truly homemade taste. Absolutely loved it!",
        },
    )
    assert review_res.status_code == 201
    review_id = review_res.json()["id"]
    assert review_res.json()["status"] == "APPROVED"

    # 6. Duplicate review for same product and order -> 409
    dup_review = await client.post(
        "/reviews",
        headers={"Authorization": f"Bearer {customer_token}"},
        json={
            "product_id": sample_product.id,
            "order_id": order_id,
            "rating": 4,
            "comment": "Submitting another review.",
        },
    )
    assert dup_review.status_code == 409

    # 7. Seller can post a response
    resp_res = await client.post(
        f"/reviews/{review_id}/response",
        headers={"Authorization": f"Bearer {seller_token}"},
        json={"seller_response": "Thank you so much! We are glad you enjoyed it."},
    )
    assert resp_res.status_code == 200
    assert resp_res.json()["seller_response"] == "Thank you so much! We are glad you enjoyed it."
