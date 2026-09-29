import asyncio
from app.database.session import AsyncSessionLocal
from app.services.auth_service import AuthService


async def run_bootstrap():
    async with AsyncSessionLocal() as session:
        admin_user = await AuthService.bootstrap_admin(session)
        print(f"Admin provisioned/verified: {admin_user.email} (must_change_password={admin_user.must_change_password})")


if __name__ == "__main__":
    asyncio.run(run_bootstrap())
