package com.cravexa.presentation.auth.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cravexa.core.common.AuthValidator
import com.cravexa.core.common.Resource
import com.cravexa.domain.model.UserRole
import com.cravexa.domain.usecase.GoogleSignInUseCase
import com.cravexa.domain.usecase.SignupUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val signupUseCase: SignupUseCase,
    private val googleSignInUseCase: GoogleSignInUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<SignupUiState>(SignupUiState.Idle)
    val uiState: StateFlow<SignupUiState> = _uiState.asStateFlow()

    var name = MutableStateFlow("")
        private set

    var email = MutableStateFlow("")
        private set

    var phone = MutableStateFlow("")
        private set

    var password = MutableStateFlow("")
        private set

    var confirmPassword = MutableStateFlow("")
        private set

    var selectedRole = MutableStateFlow(UserRole.CUSTOMER)
        private set

    private val _nameError = MutableStateFlow<String?>(null)
    val nameError: StateFlow<String?> = _nameError.asStateFlow()

    private val _emailError = MutableStateFlow<String?>(null)
    val emailError: StateFlow<String?> = _emailError.asStateFlow()

    private val _phoneError = MutableStateFlow<String?>(null)
    val phoneError: StateFlow<String?> = _phoneError.asStateFlow()

    private val _passwordError = MutableStateFlow<String?>(null)
    val passwordError: StateFlow<String?> = _passwordError.asStateFlow()

    private val _confirmPasswordError = MutableStateFlow<String?>(null)
    val confirmPasswordError: StateFlow<String?> = _confirmPasswordError.asStateFlow()

    fun onNameChanged(value: String) {
        name.value = value
        if (_nameError.value != null) _nameError.value = null
    }

    fun onEmailChanged(value: String) {
        email.value = value
        if (_emailError.value != null) _emailError.value = null
    }

    fun onPhoneChanged(value: String) {
        phone.value = value
        if (_phoneError.value != null) _phoneError.value = null
    }

    fun onPasswordChanged(value: String) {
        password.value = value
        if (_passwordError.value != null) _passwordError.value = null
    }

    fun onConfirmPasswordChanged(value: String) {
        confirmPassword.value = value
        if (_confirmPasswordError.value != null) _confirmPasswordError.value = null
    }

    fun onRoleSelected(role: UserRole) {
        // Enforce security rule: normal users cannot select ADMIN
        if (role == UserRole.ADMIN) {
            selectedRole.value = UserRole.CUSTOMER
        } else {
            selectedRole.value = role
        }
    }

    fun signup() {
        val nameVal = name.value.trim()
        val emailVal = email.value.trim()
        val phoneVal = phone.value.trim()
        val passwordVal = password.value
        val confirmPasswordVal = confirmPassword.value

        var hasError = false

        val nameValidation = AuthValidator.validateName(nameVal)
        if (!nameValidation.isValid) {
            _nameError.value = nameValidation.errorMessage
            hasError = true
        }

        val emailValidation = AuthValidator.validateEmail(emailVal)
        if (!emailValidation.isValid) {
            _emailError.value = emailValidation.errorMessage
            hasError = true
        }

        if (phoneVal.isNotBlank()) {
            val phoneValidation = AuthValidator.validatePhone(phoneVal)
            if (!phoneValidation.isValid) {
                _phoneError.value = phoneValidation.errorMessage
                hasError = true
            }
        }

        val passwordValidation = AuthValidator.validatePassword(passwordVal)
        if (!passwordValidation.isValid) {
            _passwordError.value = passwordValidation.errorMessage
            hasError = true
        }

        val confirmValidation = AuthValidator.validateConfirmPassword(passwordVal, confirmPasswordVal)
        if (!confirmValidation.isValid) {
            _confirmPasswordError.value = confirmValidation.errorMessage
            hasError = true
        }

        if (hasError) return

        viewModelScope.launch {
            _uiState.value = SignupUiState.Loading
            when (val result = signupUseCase(nameVal, emailVal, passwordVal, phoneVal, selectedRole.value)) {
                is Resource.Success -> {
                    result.data?.let { user ->
                        _uiState.value = SignupUiState.Success(user)
                    } ?: run {
                        _uiState.value = SignupUiState.Error("Account created, but user profile could not be initialized.")
                    }
                }
                is Resource.Error -> {
                    _uiState.value = SignupUiState.Error(result.message ?: "Sign up failed. Please try again.")
                }
                is Resource.Loading -> {
                    _uiState.value = SignupUiState.Loading
                }
            }
        }
    }

    fun signupWithGoogle(idToken: String) {
        viewModelScope.launch {
            _uiState.value = SignupUiState.Loading
            when (val result = googleSignInUseCase(idToken, selectedRole.value)) {
                is Resource.Success -> {
                    result.data?.let { user ->
                        _uiState.value = SignupUiState.Success(user)
                    } ?: run {
                        _uiState.value = SignupUiState.Error("Google signup succeeded, but profile could not be loaded.")
                    }
                }
                is Resource.Error -> {
                    _uiState.value = SignupUiState.Error(result.message ?: "Google Sign-Up failed.")
                }
                is Resource.Loading -> {
                    _uiState.value = SignupUiState.Loading
                }
            }
        }
    }

    fun resetError() {
        _uiState.value = SignupUiState.Idle
    }
}

