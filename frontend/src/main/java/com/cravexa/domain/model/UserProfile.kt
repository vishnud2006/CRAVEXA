package com.cravexa.domain.model

data class UserProfile(
    val id: String = "",
    val firebaseUid: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val role: UserRole = UserRole.CUSTOMER,
    val sellerBusinessName: String? = null,
    val sellerFoodCategory: String? = null,
    val sellerAddress: String? = null,
    val sellerStatus: SellerStatus = SellerStatus.NONE,
    val profileImage: String? = null,
    val profileCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

