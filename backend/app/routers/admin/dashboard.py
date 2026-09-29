from fastapi import APIRouter, Depends
from sqlalchemy.ext.asyncio import AsyncSession

from app.core.dependencies import require_admin
from app.database.session import get_db
from app.models.user import User
from app.schemas.admin import AdminDashboardStats
from app.services.admin_service import AdminService

router = APIRouter(prefix="/dashboard", tags=["Admin - Dashboard"])


@router.get("/stats", response_model=AdminDashboardStats)
async def get_dashboard_stats(
    admin: User = Depends(require_admin),
    db: AsyncSession = Depends(get_db),
):
    """
    Retrieve real aggregate metrics for the CRAVEXA Admin Dashboard:
    - User counts (customers, sellers, pending sellers)
    - Product counts (total, pending approval)
    - Order counts & total platform revenue
    - Pending FSSAI verifications & unresolved complaints
    """
    return await AdminService.get_dashboard_stats(db)
