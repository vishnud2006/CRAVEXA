package com.cravexa.presentation.customer.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cravexa.core.common.AuthValidator
import com.cravexa.core.common.Resource
import com.cravexa.domain.model.CustomerProfile
import com.cravexa.domain.usecase.GetCustomerProfileUseCase
import com.cravexa.domain.usecase.UpdateCustomerProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface EditProfileUiState {
    data object Idle : EditProfileUiState
    data object Loading : EditProfileUiState
    data object Saving : EditProfileUiState
    data class Success(val profile: CustomerProfile) : EditProfileUiState
    data class Error(val message: String) : EditProfileUiState
}

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val getCustomerProfileUseCase: GetCustomerProfileUseCase,
    private val updateCustomerProfileUseCase: UpdateCustomerProfileUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<EditProfileUiState>(EditProfileUiState.Idle)
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()

    private var currentProfile: CustomerProfile? = null

    var name = MutableStateFlow("")
        private set

    var email = MutableStateFlow("")
        private set

    var phone = MutableStateFlow("")
        private set

    private val _nameError = MutableStateFlow<String?>(null)
    val nameError: StateFlow<String?> = _nameError.asStateFlow()

    private val _phoneError = MutableStateFlow<String?>(null)
    val phoneError: StateFlow<String?> = _phoneError.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _uiState.value = EditProfileUiState.Loading
            val profile = getCustomerProfileUseCase.observe().firstOrNull()
            currentProfile = profile
            if (profile != null) {
                name.value = profile.name
                email.value = profile.email
                phone.value = profile.phone
            }
            _uiState.value = EditProfileUiState.Idle
        }
    }

    fun onNameChanged(value: String) {
        name.value = value
        if (_nameError.value != null) _nameError.value = null
    }

    fun onPhoneChanged(value: String) {
        phone.value = value
        if (_phoneError.value != null) _phoneError.value = null
    }

    fun saveProfile() {
        val nameVal = name.value.trim()
        val phoneVal = phone.value.trim()

        var hasError = false

        val nameValidation = AuthValidator.validateName(nameVal)
        if (!nameValidation.isValid) {
            _nameError.value = nameValidation.errorMessage
            hasError = true
        }

        val phoneValidation = AuthValidator.validatePhone(phoneVal)
        if (!phoneValidation.isValid) {
            _phoneError.value = phoneValidation.errorMessage
            hasError = true
        }

        if (hasError) return

        viewModelScope.launch {
            _uiState.value = EditProfileUiState.Saving
            val existing = currentProfile ?: CustomerProfile()
            val updated = existing.copy(
                name = nameVal,
                phone = phoneVal,
                updatedAt = System.currentTimeMillis()
            )

            when (val result = updateCustomerProfileUseCase(updated)) {
                is Resource.Success -> {
                    result.data?.let { saved ->
                        _uiState.value = EditProfileUiState.Success(saved)
                    } ?: run {
                        _uiState.value = EditProfileUiState.Error("Profile updated, but data could not be refreshed.")
                    }
                }
                is Resource.Error -> {
                    _uiState.value = EditProfileUiState.Error(result.message ?: "Failed to update profile.")
                }
                is Resource.Loading -> {
                    _uiState.value = EditProfileUiState.Saving
                }
            }
        }
    }

    fun resetState() {
        _uiState.value = EditProfileUiState.Idle
    }
}

