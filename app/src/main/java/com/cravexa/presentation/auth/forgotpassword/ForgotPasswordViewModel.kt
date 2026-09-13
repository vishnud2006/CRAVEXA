package com.cravexa.presentation.auth.forgotpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cravexa.core.common.AuthValidator
import com.cravexa.core.common.Resource
import com.cravexa.domain.usecase.ForgotPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val forgotPasswordUseCase: ForgotPasswordUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ForgotPasswordUiState>(ForgotPasswordUiState.Idle)
    val uiState: StateFlow<ForgotPasswordUiState> = _uiState.asStateFlow()

    var email = MutableStateFlow("")
        private set

    private val _emailError = MutableStateFlow<String?>(null)
    val emailError: StateFlow<String?> = _emailError.asStateFlow()

    fun onEmailChanged(value: String) {
        email.value = value
        if (_emailError.value != null) _emailError.value = null
    }

    fun sendResetEmail() {
        val emailVal = email.value.trim()
        val emailValidation = AuthValidator.validateEmail(emailVal)
        if (!emailValidation.isValid) {
            _emailError.value = emailValidation.errorMessage
            return
        }

        viewModelScope.launch {
            _uiState.value = ForgotPasswordUiState.Loading
            when (val result = forgotPasswordUseCase(emailVal)) {
                is Resource.Success -> {
                    _uiState.value = ForgotPasswordUiState.Success(
                        "If an account exists for $emailVal, we have sent instructions to reset your password. Please check your inbox and spam folder."
                    )
                }
                is Resource.Error -> {
                    _uiState.value = ForgotPasswordUiState.Error(
                        result.message ?: "Unable to send reset email. Please try again."
                    )
                }
                is Resource.Loading -> {
                    _uiState.value = ForgotPasswordUiState.Loading
                }
            }
        }
    }

    fun resetState() {
        _uiState.value = ForgotPasswordUiState.Idle
    }
}

