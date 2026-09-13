package com.cravexa.domain.repository

import com.cravexa.core.common.Resource
import com.cravexa.domain.model.AdminUser
import com.cravexa.domain.model.AdminUserStatus
import kotlinx.coroutines.flow.Flow

interface AdminUserRepository {
    val usersFlow: Flow<List<AdminUser>>
    fun getUsers(): Flow<Resource<List<AdminUser>>>
    suspend fun updateUserStatus(userId: String, newStatus: AdminUserStatus): Resource<Unit>
}
