package com.cravexa.data.repository

import com.cravexa.core.common.Resource
import com.cravexa.domain.model.*
import com.cravexa.domain.repository.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdminDashboardRepositoryImpl @Inject constructor(
    private val userRepository: AdminUserRepository,
    private val sellerRepository: AdminSellerRepository,
    private val productRepository: AdminProductModerationRepository,
    private val orderRepository: AdminOrderManagementRepository,
    private val paymentRefundRepository: AdminPaymentRefundRepository,
    private val complaintRepository: AdminComplaintRepository
) : AdminDashboardRepository {

    override fun getDashboardStats(): Flow<Resource<AdminDashboardStats>> = combine(
        combine(userRepository.usersFlow, sellerRepository.sellersFlow, productRepository.productsFlow) { u, s, p ->
            Triple(u, s, p)
        },
        combine(orderRepository.ordersFlow, paymentRefundRepository.refundsFlow, complaintRepository.complaintsFlow) { o, r, c ->
            Triple(o, r, c)
        }
    ) { (users, sellers, products), (orders, refunds, complaints) ->
        val totalRevenue = orders.filter { it.paymentStatus == PaymentStatus.PAID }.sumOf { it.totalAmount }
        val pendingSellerApprovals = sellers.count { it.status == SellerAccountStatus.PENDING }
        val pendingFssai = sellers.count { it.fssaiStatus == FssaiStatus.SUBMITTED || it.fssaiStatus == FssaiStatus.PENDING }
        val pendingProductApprovals = products.count { it.status == AdminProductStatus.PENDING }
        val pendingRefunds = refunds.count { it.status == AdminRefundStatus.REQUESTED || it.status == AdminRefundStatus.UNDER_REVIEW }
        val pendingComplaints = complaints.count { it.status == ComplaintStatus.OPEN || it.status == ComplaintStatus.IN_REVIEW }

        val stats = AdminDashboardStats(
            totalCustomers = users.count { it.role == UserRole.CUSTOMER },
            totalSellers = sellers.size,
            pendingSellerApprovals = pendingSellerApprovals,
            totalProducts = products.size,
            pendingProductApprovals = pendingProductApprovals,
            totalOrders = orders.size,
            totalRevenue = totalRevenue,
            pendingRefunds = pendingRefunds,
            pendingComplaints = pendingComplaints,
            fssaiVerificationRequests = pendingFssai,
            isDevelopmentData = true
        )
        Resource.Success(stats)
    }
}
