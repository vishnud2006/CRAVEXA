package com.cravexa.presentation.cart

import com.cravexa.domain.model.CartItem

data class CartUiState(
    val items: List<CartItem> = emptyList(),
    val itemCount: Int = 0,
    val subtotal: Double = 0.0,
    val deliveryFee: Double = 49.0,
    val platformFee: Double = 5.0,
    val isFreeDelivery: Boolean = false,
    val total: Double = 0.0,
    val isLoading: Boolean = false,
    val promoDiscount: Double = 0.0,
    val appliedCoupon: String? = null
)

