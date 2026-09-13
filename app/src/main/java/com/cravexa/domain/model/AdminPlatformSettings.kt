package com.cravexa.domain.model

data class AdminPlatformSettings(
    val platformFeePercent: Double = 10.0,
    val creatorPayoutPercent: Double = 90.0,
    val baseDeliveryFee: Double = 49.0,
    val freeDeliveryThreshold: Double = 499.0,
    val maintenanceMode: Boolean = false,
    val allowNewSellerSignups: Boolean = true,
    val autoVerifyFssai: Boolean = false,
    val supportEmail: String = "admin@cravexa.com"
)
