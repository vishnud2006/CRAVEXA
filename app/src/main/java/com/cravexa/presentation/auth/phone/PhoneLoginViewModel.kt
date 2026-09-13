package com.cravexa.presentation.auth.phone

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cravexa.core.common.AuthValidator
import com.cravexa.core.common.Resource
import com.cravexa.domain.model.UserRole
import com.cravexa.domain.usecase.SendPhoneOtpUseCase
import com.cravexa.domain.usecase.VerifyPhoneOtpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhoneLoginViewModel @Inject constructor(
    private val sendPhoneOtpUseCase: SendPhoneOtpUseCase,
    private val verifyPhoneOtpUseCase: VerifyPhoneOtpUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<PhoneLoginUiState>(PhoneLoginUiState.EnterPhone)
    val uiState: StateFlow<PhoneLoginUiState> = _uiState.asStateFlow()

    var phone = MutableStateFlow("")
        private set

    var otp = MutableStateFlow("")
        private set

    var verificationId = MutableStateFlow("")
        private set

    private val _phoneError = MutableStateFlow<String?>(null)
    val phoneError: StateFlow<String?> = _phoneError.asStateFlow()

    private val _otpError = MutableStateFlow<String?>(null)
    val otpError: StateFlow<String?> = _otpError.asStateFlow()

    private val _resendCountdown = MutableStateFlow(0)
    val resendCountdown: StateFlow<Int> = _resendCountdown.asStateFlow()

    private var countdownJob: Job? = null

    fun onPhoneChanged(value: String) {
        val digits = value.filter { it.isDigit() }.take(10)
        phone.value = digits
        if (_phoneError.value != null) _phoneError.value = null
    }

    fun onOtpChanged(value: String) {
        val digits = value.filter { it.isDigit() }.take(6)
        otp.value = digits
        if (_otpError.value != null) _otpError.value = null
    }

    fun sendOtp() {
        val phoneVal = phone.value.trim()
        val validation = AuthValidator.validatePhone(phoneVal)
        if (!validation.isValid) {
            _phoneError.value = validation.errorMessage
            return
        }

        viewModelScope.launch {
            _uiState.value = PhoneLoginUiState.Loading
            when (val result = sendPhoneOtpUseCase(phoneVal) { id ->
                verificationId.value = id
            }) {
                is Resource.Success -> {
                    _uiState.value = PhoneLoginUiState.OtpSent(
                        verificationId = verificationId.value,
                        phone = phoneVal
                    )
                    startCountdown()
                }
                is Resource.Error -> {
                    _uiState.value = PhoneLoginUiState.Error(
                        result.message ?: "Failed to send OTP. Please try again."
                    )
                }
                is Resource.Loading -> {
                    _uiState.value = PhoneLoginUiState.Loading
                }
            }
        }
    }

    fun verifyOtp(role: UserRole = UserRole.CUSTOMER) {
        val otpVal = otp.value.trim()
        val validation = AuthValidator.validateOtp(otpVal)
        if (!validation.isValid) {
            _otpError.value = validation.errorMessage
            return
        }

        viewModelScope.launch {
            _uiState.value = PhoneLoginUiState.Loading
            when (val result = verifyPhoneOtpUseCase(verificationId.value, otpVal, role)) {
                is Resource.Success -> {
                    result.data?.let { user ->
                        _uiState.value = PhoneLoginUiState.Success(user)
                    } ?: run {
                        _uiState.value = PhoneLoginUiState.Error("OTP verified but user session could not be established.")
                    }
                }
                is Resource.Error -> {
                    _uiState.value = PhoneLoginUiState.Error(
                        result.message ?: "OTP verification failed. Please try again."
                    )
                }
                is Resource.Loading -> {
                    _uiState.value = PhoneLoginUiState.Loading
                }
            }
        }
    }

    fun resendOtp() {
        if (_resendCountdown.value > 0) return
        sendOtp()
    }

    private fun startCountdown() {
        countdownJob?.cancel()
        _resendCountdown.value = 30
        countdownJob = viewModelScope.launch {
            while (_resendCountdown.value > 0) {
                delay(1000)
                _resendCountdown.value -= 1
            }
        }
    }

    fun resetToPhoneInput() {
        _uiState.value = PhoneLoginUiState.EnterPhone
        otp.value = ""
        _otpError.value = null
    }

    fun resetError() {
        if (verificationId.value.isNotBlank()) {
            _uiState.value = PhoneLoginUiState.OtpSent(verificationId.value, phone.value)
        } else {
            _uiState.value = PhoneLoginUiState.EnterPhone
        }
    }

    override fun onCleared() {
        super.onCleared()
        countdownJob?.cancel()
    }
}

