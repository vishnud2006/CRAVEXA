package com.cravexa.domain.repository

import com.cravexa.core.common.Resource
import com.cravexa.domain.model.AdminSeller
import com.cravexa.domain.model.FssaiStatus
import com.cravexa.domain.model.SellerAccountStatus
import kotlinx.coroutines.flow.Flow

interface AdminSellerRepository {
    val sellersFlow: Flow<List<AdminSeller>>
    fun getSellers(): Flow<Resource<List<AdminSeller>>>
    suspend fun updateSellerStatus(sellerId: String, newStatus: SellerAccountStatus): Resource<Unit>
    suspend fun updateFssaiStatus(sellerId: String, newFssaiStatus: FssaiStatus): Resource<Unit>
}
