package com.cravexa.domain.model

data class SellerProfile(
    val id: String = "",
    val userId: String = "",
    val sellerName: String = "",
    val businessName: String = "",
    val email: String = "",
    val phone: String = "",
    val profileImage: String? = null,
    val about: String = "",
    val foodCategories: List<String> = emptyList(),
    val businessAddress: String = "",
    val city: String = "",
    val state: String = "",
    val pincode: String = "",
    val sellerSince: String = "August 2026",
    val accountStatus: SellerAccountStatus = SellerAccountStatus.PENDING,
    val fssaiNumber: String = "",
    val fssaiStatus: FssaiStatus = FssaiStatus.NOT_PROVIDED,
    val fssaiDocumentUrl: String? = null,
    val isFssaiSubmitted: Boolean = false,
    val rating: Double = 0.0,
    val totalReviews: Int = 0,
    val totalDishes: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    val isVerified: Boolean
        get() = accountStatus == SellerAccountStatus.APPROVED && fssaiStatus == FssaiStatus.VERIFIED
}

