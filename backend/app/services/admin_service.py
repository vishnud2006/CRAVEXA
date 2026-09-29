from sqlalchemy import func, select
from sqlalchemy.ext.asyncio import AsyncSession

from app.models.complaint import Complaint, ComplaintStatus
from app.models.fssai import FSSAIRecord
from app.models.order import Order, OrderStatus
from app.models.product import Product, ProductStatus
from app.models.refund import Refund, RefundStatus
from app.models.seller import FssaiStatus, SellerProfile, SellerStatus
from app.models.user import User, UserRole
from app.schemas.admin import AdminDashboardStats


class AdminService:
    @staticmethod
    async def get_dashboard_stats(db: AsyncSession) -> AdminDashboardStats:
        # Customers
        cust_cnt = await db.scalar(
            select(func.count(User.id)).where(User.role == UserRole.CUSTOMER)
        ) or 0

        # Sellers
        seller_cnt = await db.scalar(select(func.count(SellerProfile.id))) or 0
        pending_sellers = await db.scalar(
            select(func.count(SellerProfile.id)).where(SellerProfile.seller_status == SellerStatus.PENDING)
        ) or 0

        # Products
        prod_cnt = await db.scalar(select(func.count(Product.id))) or 0
        pending_products = await db.scalar(
            select(func.count(Product.id)).where(Product.status == ProductStatus.PENDING)
        ) or 0

        # Orders & Revenue
        order_cnt = await db.scalar(select(func.count(Order.id))) or 0
        rev_sum = await db.scalar(
            select(func.coalesce(func.sum(Order.total), 0.0)).where(
                Order.order_status.notin_([OrderStatus.CANCELLED, OrderStatus.REFUNDED])
            )
        ) or 0.0

        # Pending refunds
        pending_refunds = await db.scalar(
            select(func.count(Refund.id)).where(Refund.status == RefundStatus.REQUESTED)
        ) or 0

        # Pending complaints
        pending_complaints = await db.scalar(
            select(func.count(Complaint.id)).where(
                Complaint.status.in_([ComplaintStatus.OPEN, ComplaintStatus.IN_REVIEW])
            )
        ) or 0

        # FSSAI requests pending verification
        fssai_requests = await db.scalar(
            select(func.count(FSSAIRecord.id)).where(FSSAIRecord.status == FssaiStatus.SUBMITTED)
        ) or 0

        return AdminDashboardStats(
            total_customers=cust_cnt,
            total_sellers=seller_cnt,
            pending_sellers=pending_sellers,
            total_products=prod_cnt,
            pending_products=pending_products,
            total_orders=order_cnt,
            total_revenue=round(float(rev_sum), 2),
            pending_refunds=pending_refunds,
            pending_complaints=pending_complaints,
            fssai_requests=fssai_requests,
        )
