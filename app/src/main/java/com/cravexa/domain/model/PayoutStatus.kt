package com.cravexa.domain.model

enum class PayoutStatus(val displayName: String) {
    COMPLETED("Completed"),
    PROCESSING("Processing"),
    PENDING("Pending"),
    FAILED("Failed")
}
