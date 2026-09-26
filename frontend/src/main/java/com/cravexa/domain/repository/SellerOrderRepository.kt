package com.cravexa.domain.repository

import com.cravexa.core.common.Resource
import com.cravexa.domain.model.Order
import com.cravexa.domain.model.OrderStatus
import kotlinx.coroutines.flow.Flow

interface SellerOrderRepository {
    val sellerOrders: Flow<List<Order>>
    fun getOrders(): Flow<Resource<List<Order>>>
    suspend fun getOrderById(orderId: String): Resource<Order>
    suspend fun updateOrderStatus(orderId: String, newStatus: OrderStatus): Resource<Order>
    suspend fun addOrder(order: Order): Resource<Order>
}
