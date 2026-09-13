package com.cravexa.domain.model

enum class PaymentMethod(val title: String, val subtitle: String) {
    ONLINE(
        title = "Pay Online (UPI / Card / NetBanking)",
        subtitle = "Instant confirmation & safe contactless dispatch"
    ),
    COD(
        title = "Cash on Delivery",
        subtitle = "Pay in cash upon doorstep delivery"
    );

    companion object {
        fun fromString(value: String?): PaymentMethod {
            return entries.find { it.name.equals(value, ignoreCase = true) } ?: ONLINE
        }
    }
}

