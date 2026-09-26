package com.cravexa.domain.model

enum class UserRole {
    CUSTOMER,
    SELLER,
    ADMIN;

    companion object {
        fun fromString(role: String?): UserRole {
            return entries.find { it.name.equals(role, ignoreCase = true) } ?: CUSTOMER
        }
    }
}

