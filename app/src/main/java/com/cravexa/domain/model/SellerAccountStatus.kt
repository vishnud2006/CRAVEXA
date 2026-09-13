package com.cravexa.domain.model

enum class SellerAccountStatus(val displayTitle: String) {
    PENDING("Application Pending"),
    APPROVED("Approved Food Creator"),
    REJECTED("Application Rejected"),
    SUSPENDED("Account Suspended");

    companion object {
        fun fromString(status: String?): SellerAccountStatus {
            return entries.find { it.name.equals(status, ignoreCase = true) } ?: PENDING
        }
    }
}

