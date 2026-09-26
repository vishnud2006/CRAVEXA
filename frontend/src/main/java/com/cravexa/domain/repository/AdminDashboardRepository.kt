package com.cravexa.domain.repository

import com.cravexa.core.common.Resource
import com.cravexa.domain.model.AdminDashboardStats
import kotlinx.coroutines.flow.Flow

interface AdminDashboardRepository {
    fun getDashboardStats(): Flow<Resource<AdminDashboardStats>>
}
