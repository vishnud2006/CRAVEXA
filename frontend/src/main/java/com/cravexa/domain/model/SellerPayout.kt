package com.cravexa.domain.model

data class SellerPayout(
    val id: String,
    val amount: Double,
    val date: String,
    val status: PayoutStatus,
    val referenceId: String,
    val accountNumberMasked: String = "•••• 4829"
)
