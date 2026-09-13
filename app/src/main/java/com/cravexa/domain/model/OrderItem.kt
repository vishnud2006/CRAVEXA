package com.cravexa.domain.model

data class OrderItem(
    val id: String = "",
    val productId: String = "",
    val productName: String = "",
    val productImage: String? = null,
    val unitPrice: Double = 0.0,
    val quantity: Int = 1,
    val packageWeight: String = "500g",
    val sellerName: String = ""
) {
    val totalPrice: Double
        get() = unitPrice * quantity
}

