from fastapi import APIRouter

from app.routers.admin.banners import router as admin_banners_router
from app.routers.admin.categories import router as admin_categories_router
from app.routers.admin.complaints import router as admin_complaints_router
from app.routers.admin.coupons import router as admin_coupons_router
from app.routers.admin.dashboard import router as admin_dashboard_router
from app.routers.admin.fssai import router as admin_fssai_router
from app.routers.admin.orders import router as admin_orders_router
from app.routers.admin.payments import router as admin_payments_router
from app.routers.admin.products import router as admin_products_router
from app.routers.admin.refunds import router as admin_refunds_router
from app.routers.admin.reports import router as admin_reports_router
from app.routers.admin.sellers import router as admin_sellers_router
from app.routers.admin.users import router as admin_users_router

admin_router = APIRouter(prefix="/admin")

admin_router.include_router(admin_dashboard_router)
admin_router.include_router(admin_users_router)
admin_router.include_router(admin_sellers_router)
admin_router.include_router(admin_products_router)
admin_router.include_router(admin_orders_router)
admin_router.include_router(admin_fssai_router)
admin_router.include_router(admin_payments_router)
admin_router.include_router(admin_refunds_router)
admin_router.include_router(admin_complaints_router)
admin_router.include_router(admin_categories_router)
admin_router.include_router(admin_coupons_router)
admin_router.include_router(admin_banners_router)
admin_router.include_router(admin_reports_router)

__all__ = ["admin_router"]
