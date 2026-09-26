package com.cravexa.domain.model

data class AdminCoupon(
    val id: String,
    val code: String,
    val discountPercentage: Int,
    val maxDiscountAmount: Double,
    val minOrderAmount: Double,
    val startDate: String,
    val endDate: String,
    val usageLimit: Int,
    val usedCount: Int = 0,
    val active: Boolean = true
)
