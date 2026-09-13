package com.cravexa.data.repository

import com.cravexa.core.common.Resource
import com.cravexa.domain.model.AdminPlatformSettings
import com.cravexa.domain.repository.AdminSettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdminSettingsRepositoryImpl @Inject constructor() : AdminSettingsRepository {

    private val _settingsFlow = MutableStateFlow(AdminPlatformSettings())
    override val settingsFlow: Flow<AdminPlatformSettings> = _settingsFlow.asStateFlow()

    // State variable tracking whether initial administrator must change password
    private val _mustChangePasswordMap = mutableMapOf<String, Boolean>(
        "cravexa10@gmail.com" to true
    )

    override fun getSettings(): Flow<Resource<AdminPlatformSettings>> = flow {
        emit(Resource.Loading())
        emit(Resource.Success(_settingsFlow.value))
    }

    override suspend fun updateSettings(settings: AdminPlatformSettings): Resource<Unit> {
        _settingsFlow.value = settings
        return Resource.Success(Unit)
    }

    override suspend fun checkMustChangePassword(adminEmail: String): Boolean {
        return _mustChangePasswordMap[adminEmail.trim().lowercase()] ?: false
    }

    override suspend fun changeAdminPassword(
        adminEmail: String,
        currentPass: String,
        newPass: String
    ): Resource<Unit> {
        val emailKey = adminEmail.trim().lowercase()
        if (newPass.length < 8) {
            return Resource.Error("New password must be at least 8 characters long.")
        }
        if (newPass == currentPass) {
            return Resource.Error("New password cannot be the same as the current password.")
        }
        // In production, FastAPI securely hashes and updates password in PostgreSQL
        _mustChangePasswordMap[emailKey] = false
        return Resource.Success(Unit)
    }
}
