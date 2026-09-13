package com.cravexa.domain.model

enum class OrderStatus(val displayTitle: String) {
    ORDER_PLACED("Order Placed"),
    PAYMENT_CONFIRMED("Payment Confirmed"),
    PREPARING("Preparing in Kitchen"),
    READY_FOR_PICKUP("Ready for Pickup"),
    PICKED_UP("Picked Up by Courier"),
    IN_TRANSIT("In Transit"),
    OUT_FOR_DELIVERY("Out for Delivery"),
    DELIVERED("Delivered"),
    CANCELLED("Cancelled"),
    REFUNDED("Refunded");

    val stepIndex: Int
        get() = when (this) {
            ORDER_PLACED -> 0
            PAYMENT_CONFIRMED -> 1
            PREPARING -> 2
            READY_FOR_PICKUP -> 3
            PICKED_UP -> 4
            IN_TRANSIT -> 5
            OUT_FOR_DELIVERY -> 6
            DELIVERED -> 7
            CANCELLED, REFUNDED -> -1
        }

    val isTerminal: Boolean
        get() = this == DELIVERED || this == CANCELLED || this == REFUNDED

    companion object {
        val trackingSteps = listOf(
            ORDER_PLACED,
            PAYMENT_CONFIRMED,
            PREPARING,
            READY_FOR_PICKUP,
            PICKED_UP,
            IN_TRANSIT,
            OUT_FOR_DELIVERY,
            DELIVERED
        )

        fun fromString(status: String?): OrderStatus {
            return entries.find { it.name.equals(status, ignoreCase = true) } ?: ORDER_PLACED
        }
    }
}

