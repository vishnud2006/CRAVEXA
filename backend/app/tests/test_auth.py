import pytest
from httpx import AsyncClient
from app.core.config import settings


@pytest.mark.asyncio
async def test_customer_registration_and_login(client: AsyncClient):
    # 1. Register customer
    reg_payload = {
        "email": "fresh_cust@cravexa.com",
        "password": "Password123!",
        "name": "Fresh Customer",
        "phone": "9998887771",
        "role": "CUSTOMER",
    }
    reg_res = await client.post("/auth/register", json=reg_payload)
    assert reg_res.status_code == 201
    resp_data = reg_res.json()
    assert "access_token" in resp_data
    assert resp_data["user"]["email"] == "fresh_cust@cravexa.com"
    assert resp_data["user"]["role"] == "CUSTOMER"

    # 2. Duplicate registration returns 409
    dup_res = await client.post("/auth/register", json=reg_payload)
    assert dup_res.status_code == 409

    # 3. Login
    login_res = await client.post(
        "/auth/login",
        json={"email": "fresh_cust@cravexa.com", "password": "Password123!"},
    )
    assert login_res.status_code == 200
    token_data = login_res.json()
    assert "access_token" in token_data
    token = token_data["access_token"]

    # 4. Verify me endpoint
    me_res = await client.get("/auth/me", headers={"Authorization": f"Bearer {token}"})
    assert me_res.status_code == 200
    assert me_res.json()["email"] == "fresh_cust@cravexa.com"


@pytest.mark.asyncio
async def test_prevent_public_admin_registration(client: AsyncClient):
    # Attempting to register with role ADMIN must be strictly rejected
    payload = {
        "email": "fake_admin@cravexa.com",
        "password": "HackerPass123!",
        "name": "Fake Admin",
        "phone": "9998887772",
        "role": "ADMIN",
    }
    res = await client.post("/auth/register", json=payload)
    assert res.status_code == 422
    assert "strictly prohibited" in res.text


@pytest.mark.asyncio
async def test_admin_bootstrap_and_password_change(client: AsyncClient):
    # 1. Login with initial bootstrap password
    login_res = await client.post(
        "/auth/login",
        json={
            "email": settings.ADMIN_EMAIL,
            "password": settings.ADMIN_INITIAL_PASSWORD,
        },
    )
    assert login_res.status_code == 200
    token_data = login_res.json()
    token = token_data["access_token"]
    assert token_data["user"]["must_change_password"] is True

    # 2. Change password
    new_pw = "SuperSecureAdminPassword2026!"
    change_res = await client.post(
        "/auth/change-password",
        headers={"Authorization": f"Bearer {token}"},
        json={
            "current_password": settings.ADMIN_INITIAL_PASSWORD,
            "new_password": new_pw,
        },
    )
    assert change_res.status_code == 200

    # 3. Old password must no longer work
    old_login_res = await client.post(
        "/auth/login",
        json={
            "email": settings.ADMIN_EMAIL,
            "password": settings.ADMIN_INITIAL_PASSWORD,
        },
    )
    assert old_login_res.status_code == 401

    # 4. New password works and must_change_password is now False
    new_login_res = await client.post(
        "/auth/login",
        json={
            "email": settings.ADMIN_EMAIL,
            "password": new_pw,
        },
    )
    assert new_login_res.status_code == 200
    assert new_login_res.json()["user"]["must_change_password"] is False
