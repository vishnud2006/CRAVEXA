import logging
from contextlib import asynccontextmanager
from fastapi import FastAPI, HTTPException, Request, status
from fastapi.middleware.cors import CORSMiddleware
from fastapi.responses import JSONResponse

from app.core.config import settings
from app.database.session import AsyncSessionLocal
from app.routers import (
    addresses_router,
    auth_router,
    banners_router,
    cart_router,
    categories_router,
    complaints_router,
    coupons_router,
    notifications_router,
    orders_router,
    payments_router,
    products_router,
    refunds_router,
    reviews_router,
    sellers_router,
    users_router,
    wishlist_router,
)
from app.routers.admin import admin_router
from app.services.auth_service import AuthService

logger = logging.getLogger("cravexa")
logging.basicConfig(level=logging.INFO)


@asynccontextmanager
async def lifespan(app: FastAPI):
    # Startup: ensure initial admin account is provisioned securely
    async with AsyncSessionLocal() as db:
        await AuthService.bootstrap_admin(db)
        logger.info("CRAVEXA backend initialized. Admin account checked/bootstrapped.")
    yield
    # Shutdown logic
    logger.info("CRAVEXA backend shutting down.")


app = FastAPI(
    title=settings.PROJECT_NAME,
    version="1.0.0",
    description="CRAVEXA Hyperlocal Homemade Food Platform - Authoritative Backend API",
    lifespan=lifespan,
)

# CORS Middleware Configuration
origins = settings.cors_origins_list
if not origins:
    origins = ["*"]

app.add_middleware(
    CORSMiddleware,
    allow_origins=origins,
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)


# Centralized Safe Exception Handlers
@app.exception_handler(HTTPException)
async def http_exception_handler(request: Request, exc: HTTPException):
    return JSONResponse(
        status_code=exc.status_code,
        content={"detail": exc.detail},
        headers=exc.headers,
    )


@app.exception_handler(Exception)
async def general_exception_handler(request: Request, exc: Exception):
    logger.error(f"Unhandled exception during request {request.url.path}: {str(exc)}", exc_info=True)
    return JSONResponse(
        status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
        content={"detail": "An internal server error occurred."},
    )


# Health Check
@app.get("/health", tags=["Health"])
async def health_check():
    """Service health verification endpoint."""
    return {
        "status": "ok",
        "app": settings.PROJECT_NAME,
        "version": "1.0.0",
    }


# Include Routers
app.include_router(auth_router)
app.include_router(users_router)
app.include_router(addresses_router)
app.include_router(sellers_router)
app.include_router(products_router)
app.include_router(categories_router)
app.include_router(cart_router)
app.include_router(orders_router)
app.include_router(wishlist_router)
app.include_router(reviews_router)
app.include_router(notifications_router)
app.include_router(payments_router)
app.include_router(refunds_router)
app.include_router(coupons_router)
app.include_router(banners_router)
app.include_router(complaints_router)
app.include_router(admin_router)
