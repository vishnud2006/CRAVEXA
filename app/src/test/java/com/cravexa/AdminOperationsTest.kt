package com.cravexa

import com.cravexa.core.common.Resource
import com.cravexa.data.repository.*
import com.cravexa.domain.model.*
import com.cravexa.domain.usecase.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class AdminOperationsTest {

    private lateinit var userRepository: AdminUserRepositoryImpl
    private lateinit var sellerRepository: AdminSellerRepositoryImpl
    private lateinit var productRepository: AdminProductModerationRepositoryImpl
    private lateinit var orderRepository: AdminOrderManagementRepositoryImpl
    private lateinit var paymentRefundRepository: AdminPaymentRefundRepositoryImpl
    private lateinit var marketplaceRepository: AdminMarketplaceRepositoryImpl
    private lateinit var complaintRepository: AdminComplaintRepositoryImpl
    private lateinit var dashboardRepository: AdminDashboardRepositoryImpl

    @Before
    fun setup() {
        userRepository = AdminUserRepositoryImpl()
        sellerRepository = AdminSellerRepositoryImpl()
        productRepository = AdminProductModerationRepositoryImpl()
        orderRepository = AdminOrderManagementRepositoryImpl()
        paymentRefundRepository = AdminPaymentRefundRepositoryImpl()
        marketplaceRepository = AdminMarketplaceRepositoryImpl()
        complaintRepository = AdminComplaintRepositoryImpl()

        dashboardRepository = AdminDashboardRepositoryImpl(
            userRepository = userRepository,
            sellerRepository = sellerRepository,
            productRepository = productRepository,
            orderRepository = orderRepository,
            paymentRefundRepository = paymentRefundRepository,
            complaintRepository = complaintRepository
        )
    }

    @Test
    fun `dashboard stats aggregates all platform metrics reactively`() = runTest {
        val result = dashboardRepository.getDashboardStats().first()
        assertTrue(result is Resource.Success)
        val stats = (result as Resource.Success).data
        assertNotNull(stats)
        assertTrue(stats!!.totalCustomers > 0)
        assertTrue(stats.totalSellers > 0)
        assertTrue(stats.totalProducts > 0)
        assertTrue(stats.totalOrders > 0)
        assertTrue(stats.totalRevenue > 0.0)
    }

    @Test
    fun `user suspension and reactivation updates state`() = runTest {
        val users = userRepository.usersFlow.first()
        val targetUser = users.first()

        val suspendResult = userRepository.updateUserStatus(targetUser.id, AdminUserStatus.SUSPENDED)
        assertTrue(suspendResult is Resource.Success)

        val updatedUsers = userRepository.usersFlow.first()
        assertEquals(AdminUserStatus.SUSPENDED, updatedUsers.first { it.id == targetUser.id }.status)

        val reactivateResult = userRepository.updateUserStatus(targetUser.id, AdminUserStatus.ACTIVE)
        assertTrue(reactivateResult is Resource.Success)
        assertEquals(AdminUserStatus.ACTIVE, userRepository.usersFlow.first().first { it.id == targetUser.id }.status)
    }

    @Test
    fun `seller approval and rejection transitions status`() = runTest {
        val sellers = sellerRepository.sellersFlow.first()
        val pendingSeller = sellers.first { it.status == SellerAccountStatus.PENDING }

        val approveResult = sellerRepository.updateSellerStatus(pendingSeller.id, SellerAccountStatus.APPROVED)
        assertTrue(approveResult is Resource.Success)
        assertEquals(SellerAccountStatus.APPROVED, sellerRepository.sellersFlow.first().first { it.id == pendingSeller.id }.status)
    }

    @Test
    fun `fssai verification workflow updates verification status`() = runTest {
        val sellers = sellerRepository.sellersFlow.first()
        val targetSeller = sellers.first()

        val verifyResult = sellerRepository.updateFssaiStatus(targetSeller.id, FssaiStatus.VERIFIED)
        assertTrue(verifyResult is Resource.Success)
        assertEquals(FssaiStatus.VERIFIED, sellerRepository.sellersFlow.first().first { it.id == targetSeller.id }.fssaiStatus)
    }

    @Test
    fun `product moderation approves and disables delicacies`() = runTest {
        val products = productRepository.productsFlow.first()
        val pendingProd = products.first { it.status == AdminProductStatus.PENDING }

        val approveResult = productRepository.updateProductStatus(pendingProd.id, AdminProductStatus.APPROVED)
        assertTrue(approveResult is Resource.Success)
        assertEquals(AdminProductStatus.APPROVED, productRepository.productsFlow.first().first { it.id == pendingProd.id }.status)

        val disableResult = productRepository.updateProductStatus(pendingProd.id, AdminProductStatus.DISABLED)
        assertTrue(disableResult is Resource.Success)
        assertEquals(AdminProductStatus.DISABLED, productRepository.productsFlow.first().first { it.id == pendingProd.id }.status)
    }

    @Test
    fun `refund request status can be approved`() = runTest {
        val refunds = paymentRefundRepository.refundsFlow.first()
        val requestedRefund = refunds.first { it.status == AdminRefundStatus.REQUESTED }

        val approveResult = paymentRefundRepository.updateRefundStatus(requestedRefund.id, AdminRefundStatus.APPROVED)
        assertTrue(approveResult is Resource.Success)
        assertEquals(AdminRefundStatus.APPROVED, paymentRefundRepository.refundsFlow.first().first { it.id == requestedRefund.id }.status)
    }

    @Test
    fun `complaint resolution records internal admin notes`() = runTest {
        val complaints = complaintRepository.complaintsFlow.first()
        val openTicket = complaints.first { it.status == ComplaintStatus.OPEN }

        val resolveResult = complaintRepository.updateComplaintStatus(
            ticketId = openTicket.id,
            newStatus = ComplaintStatus.RESOLVED,
            internalNotes = "Resolved with seller coupon credit"
        )
        assertTrue(resolveResult is Resource.Success)

        val updatedTicket = complaintRepository.complaintsFlow.first().first { it.id == openTicket.id }
        assertEquals(ComplaintStatus.RESOLVED, updatedTicket.status)
        assertEquals("Resolved with seller coupon credit", updatedTicket.internalNotes)
    }

    @Test
    fun `marketplace category and coupon creation and toggles`() = runTest {
        val addCatResult = marketplaceRepository.addCategory(
            AdminCategory("cat_test", "Handmade Podis", "Traditional spice powders", productCount = 2, enabled = true)
        )
        assertTrue(addCatResult is Resource.Success)
        assertTrue(marketplaceRepository.categoriesFlow.first().any { it.name == "Handmade Podis" })

        val addCouponResult = marketplaceRepository.addCoupon(
            AdminCoupon("c_test", "WELCOME50", 50, 200.0, 399.0, "Today", "31 Dec 2026", 100, 0, true)
        )
        assertTrue(addCouponResult is Resource.Success)
        assertTrue(marketplaceRepository.couponsFlow.first().any { it.code == "WELCOME50" })
    }
}
