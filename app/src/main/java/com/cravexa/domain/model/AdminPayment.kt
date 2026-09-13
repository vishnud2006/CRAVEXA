package com.cravexa.domain.model

data class AdminPayment(
    val id: String,
    val orderNumber: String,
    val customerName: String,
    val amount: Double,
    val status: PaymentStatus,
    val paymentMethod: String = "UPI",
    val referenceId: String,
    val date: String
)
