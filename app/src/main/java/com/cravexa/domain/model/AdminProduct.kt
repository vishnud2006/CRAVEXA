package com.cravexa.domain.model

enum class AdminProductStatus(val displayTitle: String) {
    PENDING("Pending Moderation"),
    APPROVED("Approved & Live"),
    REJECTED("Rejected"),
    DISABLED("Disabled by Admin")
}

data class AdminProduct(
    val id: String,
    val name: String,
    val description: String,
    val sellerId: String,
    val sellerName: String,
    val categoryName: String,
    val price: Double,
    val stock: Int,
    val weight: String,
    val shelfLife: String,
    val ingredients: List<String> = emptyList(),
    val storageInstructions: String = "",
    val status: AdminProductStatus = AdminProductStatus.PENDING,
    val imageUrl: String? = null,
    val createdAt: String
)
