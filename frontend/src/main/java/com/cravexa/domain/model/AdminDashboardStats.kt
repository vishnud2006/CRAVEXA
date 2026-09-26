package com.cravexa.domain.model

data class AdminDashboardStats(
    val totalCustomers: Int = 0,
    val totalSellers: Int = 0,
    val pendingSellerApprovals: Int = 0,
    val totalProducts: Int = 0,
    val pendingProductApprovals: Int = 0,
    val totalOrders: Int = 0,
    val totalRevenue: Double = 0.0,
    val pendingRefunds: Int = 0,
    val pendingComplaints: Int = 0,
    val fssaiVerificationRequests: Int = 0,
    val isDevelopmentData: Boolean = true
)
