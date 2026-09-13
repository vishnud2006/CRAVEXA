package com.cravexa.domain.model

data class SellerReview(
    val id: String,
    val orderId: String = "",
    val productId: String = "",
    val productName: String,
    val customerName: String,
    val rating: Double,
    val comment: String,
    val date: String,
    val sellerReply: String? = null,
    val sellerReplyDate: String? = null
) {
    val isReplied: Boolean
        get() = !sellerReply.isNullOrBlank()
}
