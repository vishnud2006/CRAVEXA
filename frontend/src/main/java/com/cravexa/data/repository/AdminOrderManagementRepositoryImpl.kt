package com.cravexa.data.repository

import com.cravexa.core.common.Resource
import com.cravexa.domain.model.AdminOrder
import com.cravexa.domain.model.OrderStatus
import com.cravexa.domain.model.PaymentStatus
import com.cravexa.domain.repository.AdminOrderManagementRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdminOrderManagementRepositoryImpl @Inject constructor() : AdminOrderManagementRepository {

    private val initialOrders = mutableListOf(
        AdminOrder(
            id = "ord_101",
            orderNumber = "CRV-89421",
            customerName = "Ananya Sharma",
            customerPhone = "+91 9876543210",
            sellerName = "Lakshmi's Home Kitchen",
            totalAmount = 799.0,
            paymentStatus = PaymentStatus.PAID,
            orderStatus = OrderStatus.PAYMENT_CONFIRMED,
            date = "31 Aug 2026, 09:15 AM",
            itemCount = 3,
            deliveryAddressSummary = "Indiranagar, Bengaluru, 560038"
        ),
        AdminOrder(
            id = "ord_102",
            orderNumber = "CRV-89350",
            customerName = "Rahul Verma",
            customerPhone = "+91 9812345678",
            sellerName = "Lakshmi's Home Kitchen",
            totalAmount = 329.0,
            paymentStatus = PaymentStatus.PAID,
            orderStatus = OrderStatus.PREPARING,
            date = "31 Aug 2026, 08:30 AM",
            itemCount = 1,
            deliveryAddressSummary = "Whitefield, Bengaluru, 560066"
        ),
        AdminOrder(
            id = "ord_103",
            orderNumber = "CRV-89104",
            customerName = "Sneha Reddy",
            customerPhone = "+91 9723456789",
            sellerName = "Lakshmi's Home Kitchen",
            totalAmount = 849.0,
            paymentStatus = PaymentStatus.PAID,
            orderStatus = OrderStatus.READY_FOR_PICKUP,
            date = "30 Aug 2026, 06:45 PM",
            itemCount = 1,
            deliveryAddressSummary = "Indiranagar, Bengaluru, 560038"
        ),
        AdminOrder(
            id = "ord_104",
            orderNumber = "CRV-88210",
            customerName = "Vikram Aditya",
            customerPhone = "+91 9634567890",
            sellerName = "Malwa Heritage Sweets",
            totalAmount = 529.0,
            paymentStatus = PaymentStatus.PAID,
            orderStatus = OrderStatus.DELIVERED,
            date = "28 Aug 2026, 11:20 AM",
            itemCount = 2,
            deliveryAddressSummary = "Koramangala, Bengaluru, 560034"
        )
    )

    private val _ordersFlow = MutableStateFlow<List<AdminOrder>>(initialOrders)
    override val ordersFlow: Flow<List<AdminOrder>> = _ordersFlow.asStateFlow()

    override fun getOrders(): Flow<Resource<List<AdminOrder>>> = flow {
        emit(Resource.Loading())
        emit(Resource.Success(_ordersFlow.value.toList()))
    }

    override suspend fun addOrder(order: AdminOrder): Resource<AdminOrder> {
        return try {
            val currentList = _ordersFlow.value.toMutableList()
            currentList.add(0, order)
            _ordersFlow.value = currentList
            Resource.Success(order)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to add admin order.")
        }
    }
}
