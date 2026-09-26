package com.cravexa.domain.model

data class AdminSeller(
    val id: String,
    val sellerName: String,
    val businessName: String,
    val email: String,
    val phone: String,
    val status: SellerAccountStatus,
    val fssaiStatus: FssaiStatus,
    val fssaiNumber: String? = null,
    val fssaiDocumentUrl: String? = null,
    val location: String,
    val categoryName: String,
    val bio: String = "",
    val createdAt: String,
    val totalProducts: Int = 0,
    val totalOrders: Int = 0,
    val rating: Double = 5.0
)
