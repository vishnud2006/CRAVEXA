package com.cravexa.domain.repository

import com.cravexa.core.common.Resource
import com.cravexa.domain.model.SellerDashboardStats
import com.cravexa.domain.model.SellerPayout
import kotlinx.coroutines.flow.Flow

interface SellerEarningsRepository {
    fun getDashboardStats(): Flow<Resource<SellerDashboardStats>>
    fun getPayoutHistory(): Flow<Resource<List<SellerPayout>>>
}
