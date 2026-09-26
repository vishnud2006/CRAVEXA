package com.cravexa.domain.model

data class SellerDashboardStats(
    val todaySales: Double = 0.0,
    val totalSales: Double = 0.0,
    val totalOrders: Int = 0,
    val pendingOrders: Int = 0,
    val preparingOrders: Int = 0,
    val totalProducts: Int = 0,
    val activeProducts: Int = 0,
    val lowStockCount: Int = 0,
    val outOfStockCount: Int = 0,
    val totalEarnings: Double = 0.0,
    val availableBalance: Double = 0.0,
    val pendingBalance: Double = 0.0,
    val averageRating: Double = 0.0,
    val totalReviews: Int = 0
) {
    val hasSalesData: Boolean
        get() = totalSales > 0.0 || totalOrders > 0

    val hasEarningsData: Boolean
        get() = totalEarnings > 0.0 || availableBalance > 0.0
}

