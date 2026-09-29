import pytest
from httpx import AsyncClient


@pytest.mark.asyncio
async def test_unauthenticated_request_rejected(client: AsyncClient):
    res = await client.get("/users/me")
    assert res.status_code == 401


@pytest.mark.asyncio
async def test_customer_cannot_access_admin_endpoints(client: AsyncClient, customer_token: str):
    res = await client.get(
        "/admin/dashboard/stats",
        headers={"Authorization": f"Bearer {customer_token}"},
    )
    assert res.status_code == 403
    assert "Admin privileges required" in res.json()["detail"]


@pytest.mark.asyncio
async def test_seller_cannot_access_admin_endpoints(client: AsyncClient, seller_token: str):
    res = await client.get(
        "/admin/dashboard/stats",
        headers={"Authorization": f"Bearer {seller_token}"},
    )
    assert res.status_code == 403


@pytest.mark.asyncio
async def test_customer_cannot_access_seller_portal(client: AsyncClient, customer_token: str):
    res = await client.get(
        "/sellers/me",
        headers={"Authorization": f"Bearer {customer_token}"},
    )
    assert res.status_code == 403


@pytest.mark.asyncio
async def test_cross_customer_address_isolation(
    client: AsyncClient, customer_token: str, seller_token: str
):
    # Customer creates an address
    addr_payload = {
        "full_name": "Test Customer",
        "phone": "9876543210",
        "address_line1": "Flat 101, Palm Grove",
        "city": "Bengaluru",
        "state": "Karnataka",
        "pincode": "560001",
        "address_type": "HOME",
        "is_default": True,
    }
    create_res = await client.post(
        "/addresses",
        headers={"Authorization": f"Bearer {customer_token}"},
        json=addr_payload,
    )
    assert create_res.status_code == 201
    address_id = create_res.json()["id"]

    # Another user (seller/other user) tries to delete it -> returns 404 (isolated)
    del_res = await client.delete(
        f"/addresses/{address_id}",
        headers={"Authorization": f"Bearer {seller_token}"},
    )
    assert del_res.status_code == 404
