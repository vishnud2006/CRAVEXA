from app.routers.addresses import router as addresses_router
from app.routers.auth import router as auth_router
from app.routers.banners import router as banners_router
from app.routers.cart import router as cart_router
from app.routers.categories import router as categories_router
from app.routers.complaints import router as complaints_router
from app.routers.coupons import router as coupons_router
from app.routers.notifications import router as notifications_router
from app.routers.orders import router as orders_router
from app.routers.payments import router as payments_router
from app.routers.products import router as products_router
from app.routers.refunds import router as refunds_router
from app.routers.reviews import router as reviews_router
from app.routers.sellers import router as sellers_router
from app.routers.users import router as users_router
from app.routers.wishlist import router as wishlist_router

__all__ = [
    "auth_router",
    "users_router",
    "addresses_router",
    "sellers_router",
    "products_router",
    "categories_router",
    "cart_router",
    "orders_router",
    "wishlist_router",
    "reviews_router",
    "notifications_router",
    "payments_router",
    "refunds_router",
    "coupons_router",
    "banners_router",
    "complaints_router",
]
