package com.cravexa.domain.model

data class CustomerProfile(
    val id: String = "",
    val firebaseUid: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val profileImageUrl: String? = null,
    val memberSince: String = "August 2026",
    val isPhoneVerified: Boolean = false,
    val isEmailVerified: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

