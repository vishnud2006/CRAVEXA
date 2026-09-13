package com.cravexa.domain.repository

import com.cravexa.core.common.Resource
import com.cravexa.domain.model.AdminPlatformSettings
import kotlinx.coroutines.flow.Flow

interface AdminSettingsRepository {
    val settingsFlow: Flow<AdminPlatformSettings>
    fun getSettings(): Flow<Resource<AdminPlatformSettings>>
    suspend fun updateSettings(settings: AdminPlatformSettings): Resource<Unit>
    suspend fun checkMustChangePassword(adminEmail: String): Boolean
    suspend fun changeAdminPassword(adminEmail: String, currentPass: String, newPass: String): Resource<Unit>
}
