package com.cravexa.domain.repository

import com.cravexa.core.common.Resource
import com.cravexa.domain.model.AdminProduct
import com.cravexa.domain.model.AdminProductStatus
import kotlinx.coroutines.flow.Flow

interface AdminProductModerationRepository {
    val productsFlow: Flow<List<AdminProduct>>
    fun getProducts(): Flow<Resource<List<AdminProduct>>>
    suspend fun updateProductStatus(productId: String, newStatus: AdminProductStatus): Resource<Unit>
}
