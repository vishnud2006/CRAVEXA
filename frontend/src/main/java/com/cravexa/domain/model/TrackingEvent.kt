package com.cravexa.domain.model

data class TrackingEvent(
    val status: OrderStatus,
    val title: String,
    val description: String,
    val timestamp: String? = null,
    val isCompleted: Boolean = false,
    val isCurrent: Boolean = false
)

