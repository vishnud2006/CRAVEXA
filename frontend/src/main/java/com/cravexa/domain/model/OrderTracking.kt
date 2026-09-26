package com.cravexa.domain.model

data class OrderTracking(
    val orderId: String = "",
    val orderNumber: String = "",
    val currentStatus: OrderStatus = OrderStatus.ORDER_PLACED,
    val trackingNumber: String? = null,
    val carrierName: String? = null,
    val estimatedDelivery: String? = null,
    val lastUpdated: String? = null,
    val events: List<TrackingEvent> = emptyList()
)

