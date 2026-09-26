package com.cravexa.presentation.admin.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cravexa.core.common.Resource
import com.cravexa.domain.model.AdminPlatformSettings
import com.cravexa.domain.model.UserProfile
import com.cravexa.domain.usecase.GetAdminSettingsUseCase
import com.cravexa.domain.usecase.GetCurrentUserUseCase
import com.cravexa.domain.usecase.LogoutUseCase
import com.cravexa.domain.usecase.UpdateAdminSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface AdminSettingsUiState {
    data object Loading : AdminSettingsUiState
    data class Success(
        val settings: AdminPlatformSettings,
        val adminUser: UserProfile?
    ) : AdminSettingsUiState
    data class Error(val message: String) : AdminSettingsUiState
}

@HiltViewModel
class AdminSettingsViewModel @Inject constructor(
    private val getAdminSettingsUseCase: GetAdminSettingsUseCase,
    private val updateAdminSettingsUseCase: UpdateAdminSettingsUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _adminUser = MutableStateFlow<UserProfile?>(null)
    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    val uiState: StateFlow<AdminSettingsUiState> = combine(
        getAdminSettingsUseCase.settingsFlow,
        _adminUser
    ) { settings, user ->
        AdminSettingsUiState.Success(
            settings = settings,
            adminUser = user
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AdminSettingsUiState.Loading
    )

    init {
        getAdminSettingsUseCase().launchIn(viewModelScope)
        loadAdminUser()
    }

    private fun loadAdminUser() {
        viewModelScope.launch {
            _adminUser.value = getCurrentUserUseCase()
        }
    }

    fun updateMaintenanceMode(enabled: Boolean) {
        val current = (uiState.value as? AdminSettingsUiState.Success)?.settings ?: return
        viewModelScope.launch {
            updateAdminSettingsUseCase(current.copy(maintenanceMode = enabled))
            _toastMessage.value = if (enabled) "Maintenance mode enabled" else "Maintenance mode disabled"
        }
    }

    fun logout(onSuccess: () -> Unit) {
        viewModelScope.launch {
            logoutUseCase()
            onSuccess()
        }
    }

    fun clearToast() {
        _toastMessage.value = null
    }
}
