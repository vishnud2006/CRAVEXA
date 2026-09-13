package com.cravexa.presentation.checkout

import com.cravexa.domain.model.Address
import com.cravexa.domain.model.CartItem
import com.cravexa.domain.model.PaymentMethod
import com.cravexa.domain.usecase.CartPricing

data class CheckoutUiState(
    val items: List<CartItem> = emptyList(),
    val pricing: CartPricing = CartPricing(),
    val addresses: List<Address> = emptyList(),
    val selectedAddress: Address? = null,
    val selectedPaymentMethod: PaymentMethod = PaymentMethod.ONLINE,
    val appliedCoupon: String? = null,
    val promoDiscount: Double = 0.0,
    val isLoading: Boolean = false,
    val isPlacingOrder: Boolean = false,
    val errorMessage: String? = null
)

