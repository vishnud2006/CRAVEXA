package com.cravexa.data.repository

import com.cravexa.core.common.Resource
import com.cravexa.domain.model.Address
import com.cravexa.domain.model.AddressType
import com.cravexa.domain.model.Order
import com.cravexa.domain.model.OrderItem
import com.cravexa.domain.model.OrderStatus
import com.cravexa.domain.model.PaymentStatus
import com.cravexa.domain.repository.SellerOrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SellerOrderRepositoryImpl @Inject constructor() : SellerOrderRepository {

    private val sampleAddress = Address(
        id = "addr_seller_1",
        fullName = "Ananya Sharma",
        phone = "+91 9876543210",
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

    private val sampleAddress2 = Address(
        id = "addr_seller_2",
        fullName = "Rahul Verma",
        phone = "+91 9812345678",
        houseBuilding = "Villa 18, Palm Meadows",
        street = "Airport Varthur Road",
        area = "Whitefield",
        city = "Bengaluru",
        state = "Karnataka",
        pincode = "560066",
        landmark = "Opposite Forum Mall",
        addressType = AddressType.HOME,
        isDefault = false
    )

    private val initialOrders = mutableListOf(
        Order(
            id = "ord_seller_101",
            orderNumber = "CRV-89421",
            orderDate = "31 Aug 2026, 09:15 AM",
            userId = "usr_cust_1",
            sellerId = "sel_1",
            sellerName = "Lakshmi's Home Kitchen",
            items = listOf(
                OrderItem(
                    id = "item_s1",
                    productId = "prod_1",
                    productName = "Grandma's Andhra Avakaya Mango Pickle",
                    unitPrice = 280.0,
                    quantity = 2,
                    packageWeight = "500g Glass Jar",
                    sellerName = "Lakshmi's Home Kitchen"
                ),
                OrderItem(
                    id = "item_s2",
                    productId = "prod_2",
                    productName = "Traditional Sun-Dried Gongura Thokku",
                    unitPrice = 240.0,
                    quantity = 1,
                    packageWeight = "350g Glass Jar",
                    sellerName = "Lakshmi's Home Kitchen"
                )
            ),
            subtotal = 800.0,
            deliveryFee = 49.0,
            discount = 50.0,
            totalAmount = 799.0,
            paymentStatus = PaymentStatus.PAID,
            orderStatus = OrderStatus.PAYMENT_CONFIRMED, // Pending preparation
            deliveryAddress = sampleAddress
        ),
        Order(
            id = "ord_seller_102",
            orderNumber = "CRV-89350",
            orderDate = "31 Aug 2026, 08:30 AM",
            userId = "usr_cust_2",
            sellerId = "sel_1",
            sellerName = "Lakshmi's Home Kitchen",
            items = listOf(
                OrderItem(
                    id = "item_s3",
                    productId = "prod_1",
                    productName = "Grandma's Andhra Avakaya Mango Pickle",
                    unitPrice = 280.0,
                    quantity = 1,
                    packageWeight = "500g Glass Jar",
                    sellerName = "Lakshmi's Home Kitchen"
                )
            ),
            subtotal = 280.0,
            deliveryFee = 49.0,
            discount = 0.0,
            totalAmount = 329.0,
            paymentStatus = PaymentStatus.PAID,
            orderStatus = OrderStatus.PREPARING, // Currently preparing in kitchen
            deliveryAddress = sampleAddress2
        ),
        Order(
            id = "ord_seller_103",
            orderNumber = "CRV-89104",
            orderDate = "30 Aug 2026, 06:45 PM",
            userId = "usr_cust_3",
            sellerId = "sel_1",
            sellerName = "Lakshmi's Home Kitchen",
            items = listOf(
                OrderItem(
                    id = "item_s4",
                    productId = "prod_4",
                    productName = "Grand Festive Gourmet Delicacy Hamper",
                    unitPrice = 850.0,
                    quantity = 1,
                    packageWeight = "800g Combo Pack",
                    sellerName = "Lakshmi's Home Kitchen"
                )
            ),
            subtotal = 850.0,
            deliveryFee = 49.0,
            discount = 50.0,
            totalAmount = 849.0,
            paymentStatus = PaymentStatus.PAID,
            orderStatus = OrderStatus.READY_FOR_PICKUP, // Ready for courier
            deliveryAddress = sampleAddress
        ),
        Order(
            id = "ord_seller_104",
            orderNumber = "CRV-88210",
            orderDate = "28 Aug 2026, 11:20 AM",
            userId = "usr_cust_4",
            sellerId = "sel_1",
            sellerName = "Lakshmi's Home Kitchen",
            items = listOf(
                OrderItem(
                    id = "item_s5",
                    productId = "prod_2",
                    productName = "Traditional Sun-Dried Gongura Thokku",
                    unitPrice = 240.0,
                    quantity = 2,
                    packageWeight = "350g Glass Jar",
                    sellerName = "Lakshmi's Home Kitchen"
                )
            ),
            subtotal = 480.0,
            deliveryFee = 49.0,
            discount = 0.0,
            totalAmount = 529.0,
            paymentStatus = PaymentStatus.PAID,
            orderStatus = OrderStatus.DELIVERED, // Completed
            deliveryAddress = sampleAddress2
        )
    )

    private val _sellerOrdersFlow = MutableStateFlow<List<Order>>(initialOrders)
    override val sellerOrders: Flow<List<Order>> = _sellerOrdersFlow.asStateFlow()

    override fun getOrders(): Flow<Resource<List<Order>>> = flow {
        emit(Resource.Loading())
        emit(Resource.Success(_sellerOrdersFlow.value.toList()))
    }

    override suspend fun getOrderById(orderId: String): Resource<Order> {
        val order = _sellerOrdersFlow.value.find { it.id == orderId || it.orderNumber == orderId }
        return if (order != null) {
            Resource.Success(order)
        } else {
            Resource.Error("Order not found.")
        }
    }

    override suspend fun updateOrderStatus(orderId: String, newStatus: OrderStatus): Resource<Order> {
        return try {
            val currentList = _sellerOrdersFlow.value.toMutableList()
            val index = currentList.indexOfFirst { it.id == orderId || it.orderNumber == orderId }
            if (index != -1) {
                val updated = currentList[index].copy(orderStatus = newStatus)
                currentList[index] = updated
                _sellerOrdersFlow.value = currentList
                Resource.Success(updated)
            } else {
                Resource.Error("Order not found.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to update order status.")
        }
    }

    override suspend fun addOrder(order: Order): Resource<Order> {
        return try {
            val currentList = _sellerOrdersFlow.value.toMutableList()
            currentList.add(0, order)
            _sellerOrdersFlow.value = currentList
            Resource.Success(order)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to add seller order.")
        }
    }
}

