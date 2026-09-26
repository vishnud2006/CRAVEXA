package com.cravexa.domain.repository

import com.cravexa.core.common.Resource
import com.cravexa.domain.model.Order
import com.cravexa.domain.model.OrderTracking
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    val orders: Flow<List<Order>>

    suspend fun getOrders(): Resource<List<Order>>

    suspend fun getOrderById(orderId: String): Resource<Order>

    suspend fun getOrderTracking(orderId: String): Resource<OrderTracking>

    suspend fun createOrder(order: Order): Resource<Order>
}

