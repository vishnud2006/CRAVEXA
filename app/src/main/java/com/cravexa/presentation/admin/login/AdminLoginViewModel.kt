package com.cravexa.presentation.admin.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cravexa.core.common.Resource
import com.cravexa.domain.model.UserRole
import com.cravexa.domain.usecase.CheckAdminMustChangePasswordUseCase
import com.cravexa.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface AdminLoginUiState {
    data object Idle : AdminLoginUiState
    data object Loading : AdminLoginUiState
    data class Success(val mustChangePassword: Boolean) : AdminLoginUiState
    data class Error(val message: String) : AdminLoginUiState
}

@HiltViewModel
class AdminLoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val checkAdminMustChangePasswordUseCase: CheckAdminMustChangePasswordUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<AdminLoginUiState>(AdminLoginUiState.Idle)
    val uiState: StateFlow<AdminLoginUiState> = _uiState.asStateFlow()

    private val _email = MutableStateFlow("cravexa10@gmail.com")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    fun onEmailChange(value: String) {
        _email.value = value
    }

    fun onPasswordChange(value: String) {
        _password.value = value
    }

    fun login() {
        val trimmedEmail = _email.value.trim().lowercase()
        val enteredPassword = _password.value.trim()

        if (trimmedEmail.isBlank() || enteredPassword.isBlank()) {
            _uiState.value = AdminLoginUiState.Error("Please enter administrator email and password.")
            return
        }

        viewModelScope.launch {
            _uiState.value = AdminLoginUiState.Loading
            val result = loginUseCase(trimmedEmail, enteredPassword)
            when (result) {
                is Resource.Success -> {
                    val user = result.data
                    if (user?.role == UserRole.ADMIN) {
                        val mustRotate = checkAdminMustChangePasswordUseCase(trimmedEmail)
                        _uiState.value = AdminLoginUiState.Success(mustRotate)
                    } else {
                        _uiState.value = AdminLoginUiState.Error("Access Denied: This portal is reserved for CRAVEXA Administrators.")
                    }
                }
                is Resource.Error -> {
                    _uiState.value = AdminLoginUiState.Error(result.message ?: "Authentication failed")
                }
                else -> Unit
            }
        }
    }

    fun resetState() {
        _uiState.value = AdminLoginUiState.Idle
    }
}
