package com.cravexa.presentation.auth.signup

import com.cravexa.domain.model.UserProfile
import com.cravexa.domain.model.UserRole

sealed interface SignupUiState {
    data object Idle : SignupUiState
    data object Loading : SignupUiState
    data class Success(val user: UserProfile) : SignupUiState
    data class Error(val message: String) : SignupUiState
}

