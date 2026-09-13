package com.cravexa.presentation.auth.profile

import com.cravexa.domain.model.UserProfile

sealed interface ProfileSetupUiState {
    data object Loading : ProfileSetupUiState
    data object Ready : ProfileSetupUiState
    data object Saving : ProfileSetupUiState
    data class Success(val user: UserProfile) : ProfileSetupUiState
    data class Error(val message: String) : ProfileSetupUiState
}

