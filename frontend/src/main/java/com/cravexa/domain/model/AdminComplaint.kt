package com.cravexa.domain.model

enum class ComplaintCategory(val displayTitle: String) {
    ORDER_ISSUE("Order Delivery Delay"),
    PRODUCT_QUALITY("Food Quality / Taste"),
    PACKAGING_ISSUE("Packaging Damage"),
    SELLER_ISSUE("Seller Communication"),
    REFUND_ISSUE("Refund Delay"),
    OTHER("General Dispute")
}

enum class ComplaintStatus(val displayTitle: String) {
    OPEN("Open Ticket"),
    IN_REVIEW("Under Investigation"),
    RESOLVED("Resolved"),
    CLOSED("Closed")
}

data class AdminComplaint(
    val id: String,
    val ticketNumber: String,
    val customerName: String,
    val sellerName: String,
    val category: ComplaintCategory,
    val description: String,
    val status: ComplaintStatus,
    val internalNotes: String = "",
    val date: String
)
