package com.cravexa.domain.usecase

import com.cravexa.domain.model.CartItem
import javax.inject.Inject
import javax.inject.Singleton

data class CartPricing(
    val subtotal: Double = 0.0,
    val deliveryFee: Double = 0.0,
    val platformFee: Double = 0.0,
    val discount: Double = 0.0,
    val total: Double = 0.0,
    val isFreeDelivery: Boolean = false,
    val freeDeliveryThreshold: Double = 499.0,
    val amountNeededForFreeDelivery: Double = 0.0
) {
    val formattedSubtotal: String
        get() = "₹${subtotal.toInt()}"

    val formattedDeliveryFee: String
        get() = if (isFreeDelivery) "FREE" else "₹${deliveryFee.toInt()}"

    val formattedPlatformFee: String
        get() = "₹${platformFee.toInt()}"

    val formattedDiscount: String
        get() = if (discount > 0) "-₹${discount.toInt()}" else "₹0"

    val formattedTotal: String
        get() = "₹${total.toInt()}"
}

@Singleton
class CartCalculator @Inject constructor() {

    companion object {
        const val FREE_DELIVERY_THRESHOLD = 499.0
        const val STANDARD_DELIVERY_FEE = 49.0
        const val STANDARD_PLATFORM_FEE = 5.0
    }

    fun calculate(
        items: List<CartItem>,
        couponDiscount: Double = 0.0
    ): CartPricing {
        if (items.isEmpty()) {
            return CartPricing(
                subtotal = 0.0,
                deliveryFee = 0.0,
                platformFee = 0.0,
                discount = 0.0,
                total = 0.0,
                isFreeDelivery = true,
                freeDeliveryThreshold = FREE_DELIVERY_THRESHOLD,
                amountNeededForFreeDelivery = FREE_DELIVERY_THRESHOLD
            )
        }

        val subtotal = items.sumOf { it.product.price * it.quantity }
        val isFreeDelivery = subtotal >= FREE_DELIVERY_THRESHOLD
        val deliveryFee = if (isFreeDelivery) 0.0 else STANDARD_DELIVERY_FEE
        val platformFee = STANDARD_PLATFORM_FEE
        val discount = couponDiscount.coerceAtMost(subtotal)
        val total = (subtotal + deliveryFee + platformFee - discount).coerceAtLeast(0.0)
        val amountNeeded = (FREE_DELIVERY_THRESHOLD - subtotal).coerceAtLeast(0.0)

        return CartPricing(
            subtotal = subtotal,
            deliveryFee = deliveryFee,
            platformFee = platformFee,
            discount = discount,
            total = total,
            isFreeDelivery = isFreeDelivery,
            freeDeliveryThreshold = FREE_DELIVERY_THRESHOLD,
            amountNeededForFreeDelivery = amountNeeded
        )
    }
}

