package com.cravexa.domain.model

enum class FssaiStatus(val displayTitle: String) {
    NOT_PROVIDED("FSSAI Not Provided"),
    PENDING("Verification Pending"),
    SUBMITTED("Submitted for Review"),
    VERIFIED("FSSAI Verified"),
    REJECTED("FSSAI Rejected"),
    EXPIRED("FSSAI Expired");

    companion object {
        fun fromString(status: String?): FssaiStatus {
            return entries.find { it.name.equals(status, ignoreCase = true) } ?: NOT_PROVIDED
        }
    }
}

