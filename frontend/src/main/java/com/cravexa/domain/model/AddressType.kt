package com.cravexa.domain.model

enum class AddressType {
    HOME,
    WORK,
    OTHER;

    companion object {
        fun fromString(type: String?): AddressType {
            return entries.find { it.name.equals(type, ignoreCase = true) } ?: HOME
        }
    }
}

