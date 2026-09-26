package com.cravexa.domain.usecase

import com.cravexa.core.common.Resource
import com.cravexa.domain.model.*
import com.cravexa.domain.repository.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// Dashboard
class GetAdminDashboardStatsUseCase @Inject constructor(
    private val repository: AdminDashboardRepository
) {
    operator fun invoke(): Flow<Resource<AdminDashboardStats>> = repository.getDashboardStats()
}

// User Management
class GetAdminUsersUseCase @Inject constructor(
    private val repository: AdminUserRepository
) {
    operator fun invoke(): Flow<Resource<List<AdminUser>>> = repository.getUsers()
    val usersFlow: Flow<List<AdminUser>> = repository.usersFlow
}

class UpdateUserStatusUseCase @Inject constructor(
    private val repository: AdminUserRepository
) {
    suspend operator fun invoke(userId: String, newStatus: AdminUserStatus): Resource<Unit> =
        repository.updateUserStatus(userId, newStatus)
}

// Seller & FSSAI Verification
class GetAdminSellersUseCase @Inject constructor(
    private val repository: AdminSellerRepository
) {
    operator fun invoke(): Flow<Resource<List<AdminSeller>>> = repository.getSellers()
    val sellersFlow: Flow<List<AdminSeller>> = repository.sellersFlow
}

class ApproveSellerUseCase @Inject constructor(
    private val repository: AdminSellerRepository
) {
    suspend operator fun invoke(sellerId: String): Resource<Unit> =
        repository.updateSellerStatus(sellerId, SellerAccountStatus.APPROVED)
}

class RejectSellerUseCase @Inject constructor(
    private val repository: AdminSellerRepository
) {
    suspend operator fun invoke(sellerId: String): Resource<Unit> =
        repository.updateSellerStatus(sellerId, SellerAccountStatus.REJECTED)
}

class VerifyFssaiUseCase @Inject constructor(
    private val repository: AdminSellerRepository
) {
    suspend operator fun invoke(sellerId: String): Resource<Unit> =
        repository.updateFssaiStatus(sellerId, FssaiStatus.VERIFIED)
}

class RejectFssaiUseCase @Inject constructor(
    private val repository: AdminSellerRepository
) {
    suspend operator fun invoke(sellerId: String): Resource<Unit> =
        repository.updateFssaiStatus(sellerId, FssaiStatus.REJECTED)
}

// Product Moderation
class GetAdminProductsUseCase @Inject constructor(
    private val repository: AdminProductModerationRepository
) {
    operator fun invoke(): Flow<Resource<List<AdminProduct>>> = repository.getProducts()
    val productsFlow: Flow<List<AdminProduct>> = repository.productsFlow
}

class ApproveProductUseCase @Inject constructor(
    private val repository: AdminProductModerationRepository
) {
    suspend operator fun invoke(productId: String): Resource<Unit> =
        repository.updateProductStatus(productId, AdminProductStatus.APPROVED)
}

class RejectProductUseCase @Inject constructor(
    private val repository: AdminProductModerationRepository
) {
    suspend operator fun invoke(productId: String): Resource<Unit> =
        repository.updateProductStatus(productId, AdminProductStatus.REJECTED)
}

class ToggleProductStatusUseCase @Inject constructor(
    private val repository: AdminProductModerationRepository
) {
    suspend operator fun invoke(productId: String, disable: Boolean): Resource<Unit> =
        repository.updateProductStatus(
            productId,
            if (disable) AdminProductStatus.DISABLED else AdminProductStatus.APPROVED
        )
}

// Orders & Payments & Refunds
class GetAdminOrdersUseCase @Inject constructor(
    private val repository: AdminOrderManagementRepository
) {
    operator fun invoke(): Flow<Resource<List<AdminOrder>>> = repository.getOrders()
    val ordersFlow: Flow<List<AdminOrder>> = repository.ordersFlow
}

class GetAdminPaymentsUseCase @Inject constructor(
    private val repository: AdminPaymentRefundRepository
) {
    operator fun invoke(): Flow<Resource<List<AdminPayment>>> = repository.getPayments()
    val paymentsFlow: Flow<List<AdminPayment>> = repository.paymentsFlow
}

class GetAdminRefundsUseCase @Inject constructor(
    private val repository: AdminPaymentRefundRepository
) {
    operator fun invoke(): Flow<Resource<List<AdminRefund>>> = repository.getRefunds()
    val refundsFlow: Flow<List<AdminRefund>> = repository.refundsFlow
}

class UpdateRefundStatusUseCase @Inject constructor(
    private val repository: AdminPaymentRefundRepository
) {
    suspend operator fun invoke(refundId: String, newStatus: AdminRefundStatus): Resource<Unit> =
        repository.updateRefundStatus(refundId, newStatus)
}

// Marketplace Controls (Categories, Coupons, Banners)
class GetAdminCategoriesUseCase @Inject constructor(
    private val repository: AdminMarketplaceRepository
) {
    operator fun invoke(): Flow<Resource<List<AdminCategory>>> = repository.getCategories()
    val categoriesFlow: Flow<List<AdminCategory>> = repository.categoriesFlow
}

class AddAdminCategoryUseCase @Inject constructor(
    private val repository: AdminMarketplaceRepository
) {
    suspend operator fun invoke(category: AdminCategory): Resource<Unit> =
        repository.addCategory(category)
}

class ToggleAdminCategoryUseCase @Inject constructor(
    private val repository: AdminMarketplaceRepository
) {
    suspend operator fun invoke(categoryId: String, enabled: Boolean): Resource<Unit> =
        repository.toggleCategory(categoryId, enabled)
}

class GetAdminCouponsUseCase @Inject constructor(
    private val repository: AdminMarketplaceRepository
) {
    operator fun invoke(): Flow<Resource<List<AdminCoupon>>> = repository.getCoupons()
    val couponsFlow: Flow<List<AdminCoupon>> = repository.couponsFlow
}

class AddAdminCouponUseCase @Inject constructor(
    private val repository: AdminMarketplaceRepository
) {
    suspend operator fun invoke(coupon: AdminCoupon): Resource<Unit> =
        repository.addCoupon(coupon)
}

class ToggleAdminCouponUseCase @Inject constructor(
    private val repository: AdminMarketplaceRepository
) {
    suspend operator fun invoke(couponId: String, active: Boolean): Resource<Unit> =
        repository.toggleCoupon(couponId, active)
}

class GetAdminBannersUseCase @Inject constructor(
    private val repository: AdminMarketplaceRepository
) {
    operator fun invoke(): Flow<Resource<List<AdminBanner>>> = repository.getBanners()
    val bannersFlow: Flow<List<AdminBanner>> = repository.bannersFlow
}

class ToggleAdminBannerUseCase @Inject constructor(
    private val repository: AdminMarketplaceRepository
) {
    suspend operator fun invoke(bannerId: String, active: Boolean): Resource<Unit> =
        repository.toggleBanner(bannerId, active)
}

// Complaints
class GetAdminComplaintsUseCase @Inject constructor(
    private val repository: AdminComplaintRepository
) {
    operator fun invoke(): Flow<Resource<List<AdminComplaint>>> = repository.getComplaints()
    val complaintsFlow: Flow<List<AdminComplaint>> = repository.complaintsFlow
}

class UpdateComplaintStatusUseCase @Inject constructor(
    private val repository: AdminComplaintRepository
) {
    suspend operator fun invoke(ticketId: String, newStatus: ComplaintStatus, internalNotes: String): Resource<Unit> =
        repository.updateComplaintStatus(ticketId, newStatus, internalNotes)
}

// Settings & Password Rotation
class GetAdminSettingsUseCase @Inject constructor(
    private val repository: AdminSettingsRepository
) {
    operator fun invoke(): Flow<Resource<AdminPlatformSettings>> = repository.getSettings()
    val settingsFlow: Flow<AdminPlatformSettings> = repository.settingsFlow
}

class UpdateAdminSettingsUseCase @Inject constructor(
    private val repository: AdminSettingsRepository
) {
    suspend operator fun invoke(settings: AdminPlatformSettings): Resource<Unit> =
        repository.updateSettings(settings)
}

class CheckAdminMustChangePasswordUseCase @Inject constructor(
    private val repository: AdminSettingsRepository
) {
    suspend operator fun invoke(email: String): Boolean =
        repository.checkMustChangePassword(email)
}

class AdminChangePasswordUseCase @Inject constructor(
    private val repository: AdminSettingsRepository
) {
    suspend operator fun invoke(email: String, currentPass: String, newPass: String): Resource<Unit> =
        repository.changeAdminPassword(email, currentPass, newPass)
}
