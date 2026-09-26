package com.cravexa.presentation.auth.phone

import com.cravexa.domain.model.UserProfile

sealed interface PhoneLoginUiState {
    data object EnterPhone : PhoneLoginUiState
    data class OtpSent(val verificationId: String, val phone: String) : PhoneLoginUiState
    data object Loading : PhoneLoginUiState
    data class Success(val user: UserProfile) : PhoneLoginUiState
    data class Error(val message: String) : PhoneLoginUiState
}

