package com.cravexa.domain.usecase

import com.cravexa.core.common.Resource
import com.cravexa.domain.model.Address
import com.cravexa.domain.model.CartItem
import com.cravexa.domain.model.Order
import com.cravexa.domain.model.OrderItem
import com.cravexa.domain.model.OrderStatus
import com.cravexa.domain.model.PaymentMethod
import com.cravexa.domain.model.PaymentStatus
import com.cravexa.domain.repository.AuthRepository
import com.cravexa.domain.repository.CartRepository
import com.cravexa.domain.repository.OrderRepository
import java.util.UUID
import javax.inject.Inject

class CreateOrderUseCase @Inject constructor(
    private val orderRepository: OrderRepository,
    private val authRepository: AuthRepository,
    private val cartRepository: CartRepository,
    private val cartCalculator: CartCalculator
) {

    suspend operator fun invoke(
        items: List<CartItem>,
        deliveryAddress: Address?,
        paymentMethod: PaymentMethod,
        appliedCoupon: String? = null,
        couponDiscount: Double = 0.0
    ): Resource<Order> {
        // 1. Validate Authentication
        val currentUser = authRepository.getCurrentUser()
        if (currentUser == null) {
            return Resource.Error("Please log in to complete your purchase.")
        }

        // 2. Validate Cart
        if (items.isEmpty()) {
            return Resource.Error("Your cart is empty. Please add delicacies before checkout.")
        }

        // 3. Validate Delivery Address
        if (deliveryAddress == null || deliveryAddress.fullName.isBlank() || deliveryAddress.houseBuilding.isBlank()) {
            return Resource.Error("Please select a complete and valid delivery address.")
        }

        // 4. Validate Stock
        for (item in items) {
            if (!item.product.available) {
                return Resource.Error("${item.product.name} is currently unavailable.")
            }
            if (item.quantity > item.product.stock) {
                return Resource.Error("Only ${item.product.stock} units of ${item.product.name} are available in stock.")
            }
        }

        // 5. Authoritative Price Calculation
        val pricing = cartCalculator.calculate(items, couponDiscount)

        // 6. Build Order Items
        val orderItems = items.map { item ->
            OrderItem(
                id = "item_${UUID.randomUUID().toString().take(8)}",
                productId = item.product.id,
                productName = item.product.name,
                productImage = item.product.imageUrl,
                unitPrice = item.product.price,
                quantity = item.quantity,
                packageWeight = item.product.weight,
                sellerName = item.product.sellerName
            )
        }

        val primarySellerId = items.firstOrNull()?.product?.sellerId ?: "sel_1"
        val primarySellerName = items.firstOrNull()?.product?.sellerName ?: "CRAVEXA Kitchen Creator"

        val initialPaymentStatus = when (paymentMethod) {
            PaymentMethod.ONLINE -> PaymentStatus.PAID
            PaymentMethod.COD -> PaymentStatus.PENDING
        }

        val order = Order(
            userId = currentUser.id.ifBlank { currentUser.firebaseUid },
            sellerId = primarySellerId,
            sellerName = primarySellerName,
            items = orderItems,
            subtotal = pricing.subtotal,
            deliveryFee = pricing.deliveryFee,
            discount = pricing.discount,
            totalAmount = pricing.total,
            paymentStatus = initialPaymentStatus,
            orderStatus = OrderStatus.ORDER_PLACED,
            deliveryAddress = deliveryAddress
        )

        // 7. Execute Order Creation in Repository
        val result = orderRepository.createOrder(order)

        // 8. On Success, safely clear cart
        if (result is Resource.Success) {
            cartRepository.clearCart()
        }

        return result
    }
}

