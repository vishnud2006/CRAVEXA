package com.cravexa.domain.repository

import com.cravexa.core.common.Resource
import com.cravexa.domain.model.AdminOrder
import kotlinx.coroutines.flow.Flow

interface AdminOrderManagementRepository {
    val ordersFlow: Flow<List<AdminOrder>>
    fun getOrders(): Flow<Resource<List<AdminOrder>>>
    suspend fun addOrder(order: AdminOrder): Resource<AdminOrder>
}
