package com.cravexa.data.repository

import com.cravexa.core.common.Resource
import com.cravexa.data.local.PreferenceManager
import com.cravexa.domain.model.Address
import com.cravexa.domain.model.AddressType
import com.cravexa.domain.model.AdminOrder
import com.cravexa.domain.model.Order
import com.cravexa.domain.model.OrderItem
import com.cravexa.domain.model.OrderStatus
import com.cravexa.domain.model.OrderTracking
import com.cravexa.domain.model.PaymentStatus
import com.cravexa.domain.model.TrackingEvent
import com.cravexa.domain.repository.AdminOrderManagementRepository
import com.cravexa.domain.repository.OrderRepository
import com.cravexa.domain.repository.SellerOrderRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepositoryImpl @Inject constructor(
    private val preferenceManager: PreferenceManager,
    private val sellerOrderRepository: SellerOrderRepository,
    private val adminOrderManagementRepository: AdminOrderManagementRepository
) : OrderRepository {

    private val sampleAddress = Address(
        id = "addr_default_1",
        fullName = "Ananya Sharma",
        phone = "9876543210",
        houseBuilding = "Flat 402, Shanti Nilayam",
        street = "12th Main Road, HAL 2nd Stage",
        area = "Indiranagar",
        city = "Bengaluru",
        state = "Karnataka",
        pincode = "560038",
        landmark = "Near Metro Pillar 84",
        addressType = AddressType.HOME,
        isDefault = true
    )

    private val sampleOrders = listOf(
        Order(
            id = "ord_101",
            orderNumber = "CRV-89421",
            orderDate = "26 Aug 2026, 02:30 PM",
            userId = "usr_current",
            sellerId = "sel_1",
            sellerName = "Lakshmi's Andhra Kitchen",
            items = listOf(
                OrderItem(
                    id = "item_1",
                    productId = "prod_1",
                    productName = "Grandma's Andhra Avakaya Mango Pickle",
                    unitPrice = 280.0,
                    quantity = 2,
                    packageWeight = "500g Glass Jar",
                    sellerName = "Lakshmi's Andhra Kitchen"
                ),
                OrderItem(
                    id = "item_2",
                    productId = "prod_2",
                    productName = "Spicy Gongura Thokku Pickle",
                    unitPrice = 240.0,
                    quantity = 1,
                    packageWeight = "350g Glass Jar",
                    sellerName = "Lakshmi's Andhra Kitchen"
                )
            ),
            subtotal = 800.0,
            deliveryFee = 49.0,
            discount = 50.0,
            totalAmount = 799.0,
            paymentStatus = PaymentStatus.PAID,
            orderStatus = OrderStatus.IN_TRANSIT,
            deliveryAddress = sampleAddress
        ),
        Order(
            id = "ord_102",
            orderNumber = "CRV-88210",
            orderDate = "22 Aug 2026, 11:15 AM",
            userId = "usr_current",
            sellerId = "sel_2",
            sellerName = "Malnad Spices & Heritage Foods",
            items = listOf(
                OrderItem(
                    id = "item_3",
                    productId = "prod_3",
                    productName = "Stone-Ground Coorg Black Pepper Powder",
                    unitPrice = 320.0,
                    quantity = 1,
                    packageWeight = "250g Pouch",
                    sellerName = "Malnad Spices & Heritage Foods"
                ),
                OrderItem(
                    id = "item_4",
                    productId = "prod_4",
                    productName = "Pure Filter Coffee Powder (80:20 Chicory)",
                    unitPrice = 290.0,
                    quantity = 2,
                    packageWeight = "500g Vacuum Pack",
                    sellerName = "Malnad Spices & Heritage Foods"
                )
            ),
            subtotal = 900.0,
            deliveryFee = 49.0,
            discount = 0.0,
            totalAmount = 949.0,
            paymentStatus = PaymentStatus.PAID,
            orderStatus = OrderStatus.DELIVERED,
            deliveryAddress = sampleAddress
        ),
        Order(
            id = "ord_103",
            orderNumber = "CRV-87104",
            orderDate = "15 Aug 2026, 06:45 PM",
            userId = "usr_current",
            sellerId = "sel_3",
            sellerName = "Annapoorna Sweets & Snacks",
            items = listOf(
                OrderItem(
                    id = "item_5",
                    productId = "prod_5",
                    productName = "Pure Desi Ghee Mysore Pak",
                    unitPrice = 450.0,
                    quantity = 1,
                    packageWeight = "500g Box",
                    sellerName = "Annapoorna Sweets & Snacks"
                )
            ),
            subtotal = 450.0,
            deliveryFee = 49.0,
            discount = 30.0,
            totalAmount = 469.0,
            paymentStatus = PaymentStatus.PAID,
            orderStatus = OrderStatus.DELIVERED,
            deliveryAddress = sampleAddress
        )
    )

    private val _ordersFlow = MutableStateFlow<List<Order>>(sampleOrders)
    override val orders: Flow<List<Order>> = _ordersFlow.asStateFlow()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            val savedOrders = preferenceManager.orders.firstOrNull() ?: emptyList()
            if (savedOrders.isNotEmpty()) {
                val combined = savedOrders + sampleOrders.filterNot { sample ->
                    savedOrders.any { it.id == sample.id }
                }
                _ordersFlow.value = combined
            }
        }
    }

    override suspend fun getOrders(): Resource<List<Order>> {
        return Resource.Success(_ordersFlow.value)
    }

    override suspend fun getOrderById(orderId: String): Resource<Order> {
        val order = _ordersFlow.value.find { it.id == orderId || it.orderNumber == orderId }
        return if (order != null) {
            Resource.Success(order)
        } else {
            Resource.Error("Order not found.")
        }
    }

    override suspend fun getOrderTracking(orderId: String): Resource<OrderTracking> {
        val order = _ordersFlow.value.find { it.id == orderId || it.orderNumber == orderId }
            ?: return Resource.Error("Order not found.")

        val currentStep = order.orderStatus.stepIndex

        val events = OrderStatus.trackingSteps.mapIndexed { index, status ->
            val isCompleted = index < currentStep
            val isCurrent = index == currentStep

            val (title, description) = when (status) {
                OrderStatus.ORDER_PLACED -> "Order Placed" to "Your order has been received by CRAVEXA"
                OrderStatus.PAYMENT_CONFIRMED -> "Payment Confirmed" to "Payment verified via secure gateway"
                OrderStatus.PREPARING -> "Kitchen Preparing" to "${order.sellerName} is handcrafting your delicacies"
                OrderStatus.READY_FOR_PICKUP -> "Packed & Ready" to "Freshly packed in hygienic sealed containers"
                OrderStatus.PICKED_UP -> "Picked Up by Courier" to "Courier partner collected the package from kitchen"
                OrderStatus.IN_TRANSIT -> "In Transit" to "Package is moving towards your city delivery hub"
                OrderStatus.OUT_FOR_DELIVERY -> "Out for Delivery" to "Delivery partner is on the way to your address"
                OrderStatus.DELIVERED -> "Delivered" to "Delicacies delivered with warmth and safety"
                else -> status.displayTitle to ""
            }

            TrackingEvent(
                status = status,
                title = title,
                description = description,
                timestamp = if (index <= currentStep) order.orderDate else null,
                isCompleted = isCompleted,
                isCurrent = isCurrent
            )
        }

        val tracking = OrderTracking(
            orderId = order.id,
            orderNumber = order.orderNumber,
            currentStatus = order.orderStatus,
            trackingNumber = "CRX-TRK-${order.orderNumber.takeLast(5)}",
            carrierName = "CRAVEXA Express Fresh Logistics",
            estimatedDelivery = "Tomorrow by 6:00 PM",
            lastUpdated = order.orderDate,
            events = events
        )

        return Resource.Success(tracking)
    }

    override suspend fun createOrder(order: Order): Resource<Order> {
        return try {
            val orderId = if (order.id.isNotBlank()) order.id else "ord_${System.currentTimeMillis()}"
            val orderNum = if (order.orderNumber.isNotBlank()) {
                order.orderNumber
            } else {
                "CRV-${(10000..99999).random()}"
            }
            val orderDate = if (order.orderDate.isNotBlank()) {
                order.orderDate
            } else {
                SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date())
            }

            val finalOrder = order.copy(
                id = orderId,
                orderNumber = orderNum,
                orderDate = orderDate,
                orderStatus = OrderStatus.ORDER_PLACED,
                createdAt = System.currentTimeMillis()
            )

            // Update local in-memory flow
            val currentList = _ordersFlow.value.toMutableList()
            currentList.add(0, finalOrder)
            _ordersFlow.value = currentList

            // Persist to preference store
            val userSavedOrders = preferenceManager.orders.firstOrNull() ?: emptyList()
            val updatedSaved = listOf(finalOrder) + userSavedOrders.filterNot { it.id == finalOrder.id }
            preferenceManager.saveOrders(updatedSaved)

            // Sync with Seller Order stream
            sellerOrderRepository.addOrder(finalOrder)

            // Sync with Admin Order stream
            val adminOrder = AdminOrder(
                id = finalOrder.id,
                orderNumber = finalOrder.orderNumber,
                customerName = finalOrder.deliveryAddress?.fullName ?: "Customer",
                customerPhone = finalOrder.deliveryAddress?.phone ?: "",
                sellerName = finalOrder.sellerName,
                totalAmount = finalOrder.totalAmount,
                paymentStatus = finalOrder.paymentStatus,
                orderStatus = finalOrder.orderStatus,
                date = finalOrder.orderDate,
                itemCount = finalOrder.totalItemCount,
                deliveryAddressSummary = finalOrder.deliveryAddress?.formattedAddress ?: ""
            )
            adminOrderManagementRepository.addOrder(adminOrder)

            Resource.Success(finalOrder)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to create order.")
        }
    }
}
