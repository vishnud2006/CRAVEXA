package com.cravexa.domain.model

enum class PaymentStatus(val displayTitle: String) {
    PENDING("Payment Pending"),
    PAID("Paid Online"),
    FAILED("Payment Failed"),
    REFUNDED("Refunded");

    companion object {
        fun fromString(status: String?): PaymentStatus {
            return entries.find { it.name.equals(status, ignoreCase = true) } ?: PENDING
        }
    }
}

