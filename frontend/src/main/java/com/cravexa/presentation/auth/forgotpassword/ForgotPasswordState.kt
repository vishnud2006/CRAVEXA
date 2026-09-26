package com.cravexa.presentation.auth.forgotpassword

sealed interface ForgotPasswordUiState {
    data object Idle : ForgotPasswordUiState
    data object Loading : ForgotPasswordUiState
    data class Success(val message: String) : ForgotPasswordUiState
    data class Error(val message: String) : ForgotPasswordUiState
}

