import pytest
from httpx import AsyncClient


@pytest.mark.asyncio
async def test_admin_dashboard_metrics(client: AsyncClient, admin_token: str):
    res = await client.get(
        "/admin/dashboard/stats",
        headers={"Authorization": f"Bearer {admin_token}"},
    )
    assert res.status_code == 200
    data = res.json()
    assert "total_users" in data
    assert "total_customers" in data
    assert "total_sellers" in data
    assert "total_orders" in data
    assert "total_revenue" in data


@pytest.mark.asyncio
async def test_admin_seller_approval_flow(client: AsyncClient, admin_token: str):
    # 1. Register a new seller (starts as PENDING)
    reg_payload = {
        "email": "new_baker@cravexa.com",
        "password": "BakerPassword123!",
        "name": "Artisan Baker",
        "phone": "9991112223",
        "role": "SELLER",
        "business_name": "Artisan Bakery",
        "city": "Bengaluru",
        "state": "Karnataka",
        "pincode": "560025",
        "food_category": "Baked Goods",
        "address": "45 Baker Lane",
    }
    reg_res = await client.post("/auth/register", json=reg_payload)
    assert reg_res.status_code == 201

    # 2. Login to get seller token
    login_res = await client.post(
        "/auth/login",
        json={"email": "new_baker@cravexa.com", "password": "BakerPassword123!"},
    )
    seller_token = login_res.json()["access_token"]

    # 3. Check status as seller
    status_res = await client.get("/sellers/me/status", headers={"Authorization": f"Bearer {seller_token}"})
    assert status_res.status_code == 200
    seller_id = status_res.json()["seller_id"]
    assert status_res.json()["seller_status"] == "PENDING"

    # 4. Admin approves seller
    admin_approval = await client.put(
        f"/admin/sellers/{seller_id}/status",
        headers={"Authorization": f"Bearer {admin_token}"},
        json={"seller_status": "APPROVED"},
    )
    assert admin_approval.status_code == 200
    assert admin_approval.json()["seller_status"] == "APPROVED"


@pytest.mark.asyncio
async def test_admin_fssai_verification_and_rejection(
    client: AsyncClient, seller_token: str, admin_token: str
):
    # 1. Seller submits FSSAI documentation
    fssai_payload = {
        "fssai_number": "11223344556677",
        "document_url": "https://storage.cravexa.com/fssai/cert_1122.pdf",
    }
    sub_res = await client.post(
        "/sellers/me/fssai",
        headers={"Authorization": f"Bearer {seller_token}"},
        json=fssai_payload,
    )
    assert sub_res.status_code == 200
    fssai_record = sub_res.json()
    record_id = fssai_record["id"]
    assert fssai_record["status"] == "PENDING"

    # 2. Appears in Admin pending FSSAI queue
    pending_res = await client.get(
        "/admin/fssai/pending",
        headers={"Authorization": f"Bearer {admin_token}"},
    )
    assert pending_res.status_code == 200
    pending_ids = [rec["id"] for rec in pending_res.json()]
    assert record_id in pending_ids

    # 3. Admin verifies FSSAI
    verify_res = await client.put(
        f"/admin/fssai/{record_id}/verify",
        headers={"Authorization": f"Bearer {admin_token}"},
    )
    assert verify_res.status_code == 200
    assert verify_res.json()["status"] == "VERIFIED"

    # 4. Check seller profile reflected VERIFIED
    seller_status = await client.get(
        "/sellers/me/status",
        headers={"Authorization": f"Bearer {seller_token}"},
    )
    assert seller_status.json()["fssai_status"] == "VERIFIED"

    # 5. Admin can also reject if renewal or audit fails
    reject_res = await client.put(
        f"/admin/fssai/{record_id}/reject",
        headers={"Authorization": f"Bearer {admin_token}"},
        json={"status": "REJECTED", "rejection_reason": "Certificate expired on audit"},
    )
    assert reject_res.status_code == 200
    assert reject_res.json()["status"] == "REJECTED"
    assert reject_res.json()["rejection_reason"] == "Certificate expired on audit"
