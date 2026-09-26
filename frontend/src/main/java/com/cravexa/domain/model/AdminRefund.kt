package com.cravexa.domain.model

enum class AdminRefundStatus(val displayTitle: String) {
    REQUESTED("Requested"),
    UNDER_REVIEW("Under Review"),
    APPROVED("Approved"),
    PROCESSING("Processing"),
    COMPLETED("Completed"),
    REJECTED("Rejected")
}

data class AdminRefund(
    val id: String,
    val orderNumber: String,
    val customerName: String,
    val sellerName: String,
    val amount: Double,
    val reason: String,
    val status: AdminRefundStatus,
    val date: String
)
