package com.cravexa.domain.model

enum class SellerStatus {
    NONE,
    PENDING,
    APPROVED,
    REJECTED;

    companion object {
        fun fromString(status: String?): SellerStatus {
            return entries.find { it.name.equals(status, ignoreCase = true) } ?: NONE
        }
    }
}

