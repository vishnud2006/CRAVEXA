package com.cravexa.data.repository

import com.cravexa.core.common.Resource
import com.cravexa.domain.model.PayoutStatus
import com.cravexa.domain.model.SellerDashboardStats
import com.cravexa.domain.model.SellerPayout
import com.cravexa.domain.repository.SellerEarningsRepository
import com.cravexa.domain.repository.SellerOrderRepository
import com.cravexa.domain.repository.SellerProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SellerEarningsRepositoryImpl @Inject constructor(
    private val productRepository: SellerProductRepository,
    private val orderRepository: SellerOrderRepository
) : SellerEarningsRepository {

    private val samplePayouts = listOf(
        SellerPayout(
            id = "pay_101",
            amount = 4520.0,
            date = "28 Aug 2026",
            status = PayoutStatus.COMPLETED,
            referenceId = "CRX-BNK-98124501",
            accountNumberMasked = "•••• 4829"
        ),
        SellerPayout(
            id = "pay_102",
            amount = 3280.0,
            date = "21 Aug 2026",
            status = PayoutStatus.COMPLETED,
            referenceId = "CRX-BNK-87391024",
            accountNumberMasked = "•••• 4829"
        ),
        SellerPayout(
            id = "pay_103",
            amount = 1128.0,
            date = "31 Aug 2026",
            status = PayoutStatus.PROCESSING,
            referenceId = "CRX-BNK-99014522",
            accountNumberMasked = "•••• 4829"
        )
    )

    override fun getDashboardStats(): Flow<Resource<SellerDashboardStats>> = combine(
        productRepository.sellerProducts,
        orderRepository.sellerOrders
    ) { products, orders ->
        val totalSales = orders.sumOf { it.totalAmount }
        val todaySales = orders.filter { it.orderDate.startsWith("31 Aug") }.sumOf { it.totalAmount }
        val pendingOrders = orders.count {
            it.orderStatus == com.cravexa.domain.model.OrderStatus.ORDER_PLACED ||
            it.orderStatus == com.cravexa.domain.model.OrderStatus.PAYMENT_CONFIRMED
        }
        val preparingOrders = orders.count {
            it.orderStatus == com.cravexa.domain.model.OrderStatus.PREPARING
        }
        val activeProducts = products.count { it.available && it.stock > 0 }
        val lowStockCount = products.count { it.stock in 1..5 }
        val outOfStockCount = products.count { it.stock == 0 }

        val totalEarnings = totalSales * 0.90 // 90% creator payout (transparent platform model)
        val availableBalance = 1128.0
        val pendingBalance = 799.0

        val stats = SellerDashboardStats(
            todaySales = todaySales,
            totalSales = totalSales,
            totalOrders = orders.size,
            pendingOrders = pendingOrders,
            preparingOrders = preparingOrders,
            totalProducts = products.size,
            activeProducts = activeProducts,
            lowStockCount = lowStockCount,
            outOfStockCount = outOfStockCount,
            totalEarnings = totalEarnings,
            availableBalance = availableBalance,
            pendingBalance = pendingBalance,
            averageRating = 4.9,
            totalReviews = 173
        )
        Resource.Success(stats)
    }

    override fun getPayoutHistory(): Flow<Resource<List<SellerPayout>>> = flow {
        emit(Resource.Loading())
        emit(Resource.Success(samplePayouts))
    }
}

