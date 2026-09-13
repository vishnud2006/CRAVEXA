package com.cravexa.domain.usecase

import com.cravexa.core.common.Resource
import com.cravexa.domain.model.Order
import com.cravexa.domain.model.OrderTracking
import com.cravexa.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetOrdersUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {
    suspend operator fun invoke(): Resource<List<Order>> {
        return orderRepository.getOrders()
    }

    fun observe(): Flow<List<Order>> {
        return orderRepository.orders
    }
}

class GetOrderDetailUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {
    suspend operator fun invoke(orderId: String): Resource<Order> {
        return orderRepository.getOrderById(orderId)
    }
}

class GetOrderTrackingUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {
    suspend operator fun invoke(orderId: String): Resource<OrderTracking> {
        return orderRepository.getOrderTracking(orderId)
    }
}

