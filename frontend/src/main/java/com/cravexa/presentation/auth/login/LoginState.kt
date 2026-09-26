package com.cravexa.presentation.auth.login

import com.cravexa.domain.model.UserRole

sealed interface LoginUiState {
    data object Idle : LoginUiState
    data object Loading : LoginUiState
    data class Success(
        val profileCompleted: Boolean,
        val role: UserRole = UserRole.CUSTOMER
    ) : LoginUiState
    data class Error(val message: String) : LoginUiState
}
