package com.cravexa.presentation.admin.password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cravexa.core.common.Resource
import com.cravexa.domain.usecase.AdminChangePasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface AdminChangePasswordUiState {
    data object Idle : AdminChangePasswordUiState
    data object Loading : AdminChangePasswordUiState
    data object Success : AdminChangePasswordUiState
    data class Error(val message: String) : AdminChangePasswordUiState
}

@HiltViewModel
class AdminChangePasswordViewModel @Inject constructor(
    private val adminChangePasswordUseCase: AdminChangePasswordUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<AdminChangePasswordUiState>(AdminChangePasswordUiState.Idle)
    val uiState: StateFlow<AdminChangePasswordUiState> = _uiState.asStateFlow()

    private val _currentPassword = MutableStateFlow("")
    val currentPassword: StateFlow<String> = _currentPassword.asStateFlow()

    private val _newPassword = MutableStateFlow("")
    val newPassword: StateFlow<String> = _newPassword.asStateFlow()

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword: StateFlow<String> = _confirmPassword.asStateFlow()

    fun onCurrentPasswordChange(value: String) {
        _currentPassword.value = value
    }

    fun onNewPasswordChange(value: String) {
        _newPassword.value = value
    }

    fun onConfirmPasswordChange(value: String) {
        _confirmPassword.value = value
    }

    fun submitChangePassword(adminEmail: String = "cravexa10@gmail.com") {
        val curr = _currentPassword.value.trim()
        val newP = _newPassword.value.trim()
        val conf = _confirmPassword.value.trim()

        if (curr.isBlank()) {
            _uiState.value = AdminChangePasswordUiState.Error("Please enter your current password.")
            return
        }
        if (newP.length < 8) {
            _uiState.value = AdminChangePasswordUiState.Error("New password must be at least 8 characters long.")
            return
        }
        if (newP != conf) {
            _uiState.value = AdminChangePasswordUiState.Error("New password and confirmation do not match.")
            return
        }
        if (newP == curr) {
            _uiState.value = AdminChangePasswordUiState.Error("New password must be different from current password.")
            return
        }

        viewModelScope.launch {
            _uiState.value = AdminChangePasswordUiState.Loading
            val result = adminChangePasswordUseCase(adminEmail, curr, newP)
            if (result is Resource.Success) {
                _uiState.value = AdminChangePasswordUiState.Success
            } else {
                _uiState.value = AdminChangePasswordUiState.Error(result.message ?: "Failed to change password")
            }
        }
    }

    fun resetState() {
        _uiState.value = AdminChangePasswordUiState.Idle
    }
}
