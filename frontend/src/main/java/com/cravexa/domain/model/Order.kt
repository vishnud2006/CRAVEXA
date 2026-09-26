package com.cravexa.domain.model

data class Order(
    val id: String = "",
    val orderNumber: String = "",
    val orderDate: String = "",
    val userId: String = "",
    val sellerId: String = "",
    val sellerName: String = "",
    val items: List<OrderItem> = emptyList(),
    val subtotal: Double = 0.0,
    val deliveryFee: Double = 0.0,
    val discount: Double = 0.0,
    val totalAmount: Double = 0.0,
    val paymentStatus: PaymentStatus = PaymentStatus.PENDING,
    val orderStatus: OrderStatus = OrderStatus.ORDER_PLACED,
    val deliveryAddress: Address? = null,
    val createdAt: Long = System.currentTimeMillis()
) {
    val totalItemCount: Int
        get() = items.sumOf { it.quantity }

    val itemsSummary: String
        get() = when {
            items.isEmpty() -> "No items"
            items.size == 1 -> "${items[0].productName} (${items[0].packageWeight})"
            else -> "${items[0].productName} + ${items.size - 1} more items"
        }
}

