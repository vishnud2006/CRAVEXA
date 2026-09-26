package com.cravexa.presentation.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cravexa.core.common.AuthValidator
import com.cravexa.core.common.Resource
import com.cravexa.domain.model.UserRole
import com.cravexa.domain.usecase.GoogleSignInUseCase
import com.cravexa.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val googleSignInUseCase: GoogleSignInUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    var email = MutableStateFlow("")
        private set

    var password = MutableStateFlow("")
        private set

    private val _emailError = MutableStateFlow<String?>(null)
    val emailError: StateFlow<String?> = _emailError.asStateFlow()

    private val _passwordError = MutableStateFlow<String?>(null)
    val passwordError: StateFlow<String?> = _passwordError.asStateFlow()

    fun onEmailChanged(value: String) {
        email.value = value
        if (_emailError.value != null) {
            _emailError.value = null
        }
    }

    fun onPasswordChanged(value: String) {
        password.value = value
        if (_passwordError.value != null) {
            _passwordError.value = null
        }
    }

    fun login() {
        val emailVal = email.value.trim()
        val passwordVal = password.value

        val emailValidation = AuthValidator.validateEmail(emailVal)
        if (!emailValidation.isValid) {
            _emailError.value = emailValidation.errorMessage
            return
        }

        if (passwordVal.isBlank()) {
            _passwordError.value = "Please enter your password."
            return
        }

        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            when (val result = loginUseCase(emailVal, passwordVal)) {
                is Resource.Success -> {
                    val user = result.data
                    _uiState.value = LoginUiState.Success(
                        profileCompleted = user?.profileCompleted == true,
                        role = user?.role ?: UserRole.CUSTOMER
                    )
                }
                is Resource.Error -> {
                    _uiState.value = LoginUiState.Error(result.message ?: "Login failed. Please check your credentials.")
                }
                is Resource.Loading -> {
                    _uiState.value = LoginUiState.Loading
                }
            }
        }
    }

    fun loginWithGoogle(idToken: String, role: UserRole = UserRole.CUSTOMER) {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            when (val result = googleSignInUseCase(idToken, role)) {
                is Resource.Success -> {
                    val user = result.data
                    _uiState.value = LoginUiState.Success(
                        profileCompleted = user?.profileCompleted == true,
                        role = user?.role ?: role
                    )
                }
                is Resource.Error -> {
                    _uiState.value = LoginUiState.Error(result.message ?: "Google Sign-In failed.")
                }
                is Resource.Loading -> {
                    _uiState.value = LoginUiState.Loading
                }
            }
        }
    }

    fun resetError() {
        _uiState.value = LoginUiState.Idle
    }
}

