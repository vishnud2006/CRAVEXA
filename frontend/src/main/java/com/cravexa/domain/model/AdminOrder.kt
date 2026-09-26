package com.cravexa.domain.model

data class AdminOrder(
    val id: String,
    val orderNumber: String,
    val customerName: String,
    val customerPhone: String,
    val sellerName: String,
    val totalAmount: Double,
    val paymentStatus: PaymentStatus,
    val orderStatus: OrderStatus,
    val date: String,
    val itemCount: Int,
    val deliveryAddressSummary: String
)
