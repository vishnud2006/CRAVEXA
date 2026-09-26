package com.cravexa.presentation.auth.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cravexa.core.common.AuthValidator
import com.cravexa.core.common.Resource
import com.cravexa.domain.model.SellerStatus
import com.cravexa.domain.model.UserProfile
import com.cravexa.domain.model.UserRole
import com.cravexa.domain.usecase.GetUserProfileUseCase
import com.cravexa.domain.usecase.SaveUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileSetupViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val saveUserProfileUseCase: SaveUserProfileUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileSetupUiState>(ProfileSetupUiState.Loading)
    val uiState: StateFlow<ProfileSetupUiState> = _uiState.asStateFlow()

    private var currentUser: UserProfile? = null

    var name = MutableStateFlow("")
        private set

    var email = MutableStateFlow("")
        private set

    var phone = MutableStateFlow("")
        private set

    var role = MutableStateFlow(UserRole.CUSTOMER)
        private set

    // Seller-specific fields
    var businessName = MutableStateFlow("")
        private set

    var foodCategory = MutableStateFlow("Handmade Pickles & Chutneys")
        private set

    var address = MutableStateFlow("")
        private set

    private val _nameError = MutableStateFlow<String?>(null)
    val nameError: StateFlow<String?> = _nameError.asStateFlow()

    private val _emailError = MutableStateFlow<String?>(null)
    val emailError: StateFlow<String?> = _emailError.asStateFlow()

    private val _phoneError = MutableStateFlow<String?>(null)
    val phoneError: StateFlow<String?> = _phoneError.asStateFlow()

    private val _businessNameError = MutableStateFlow<String?>(null)
    val businessNameError: StateFlow<String?> = _businessNameError.asStateFlow()

    private val _addressError = MutableStateFlow<String?>(null)
    val addressError: StateFlow<String?> = _addressError.asStateFlow()

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            _uiState.value = ProfileSetupUiState.Loading
            val profile = getUserProfileUseCase.observe().firstOrNull()
            currentUser = profile
            if (profile != null) {
                name.value = profile.name
                email.value = profile.email
                phone.value = profile.phone
                role.value = profile.role
                businessName.value = profile.sellerBusinessName ?: ""
                foodCategory.value = profile.sellerFoodCategory ?: "Handmade Pickles & Chutneys"
                address.value = profile.sellerAddress ?: ""
            }
            _uiState.value = ProfileSetupUiState.Ready
        }
    }

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

    fun onBusinessNameChanged(value: String) {
        businessName.value = value
        if (_businessNameError.value != null) _businessNameError.value = null
    }

    fun onFoodCategoryChanged(value: String) {
        foodCategory.value = value
    }

    fun onAddressChanged(value: String) {
        address.value = value
        if (_addressError.value != null) _addressError.value = null
    }

    fun saveProfile() {
        val nameVal = name.value.trim()
        val emailVal = email.value.trim()
        val phoneVal = phone.value.trim()
        val isSeller = role.value == UserRole.SELLER
        val businessNameVal = businessName.value.trim()
        val addressVal = address.value.trim()

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

        val phoneValidation = AuthValidator.validatePhone(phoneVal)
        if (!phoneValidation.isValid) {
            _phoneError.value = phoneValidation.errorMessage
            hasError = true
        }

        if (isSeller) {
            val bizValidation = AuthValidator.validateBusinessName(businessNameVal)
            if (!bizValidation.isValid) {
                _businessNameError.value = bizValidation.errorMessage
                hasError = true
            }

            val addressValidation = AuthValidator.validateAddress(addressVal)
            if (!addressValidation.isValid) {
                _addressError.value = addressValidation.errorMessage
                hasError = true
            }
        }

        if (hasError) return

        viewModelScope.launch {
            _uiState.value = ProfileSetupUiState.Saving

            val existing = currentUser ?: UserProfile()
            val updated = existing.copy(
                name = nameVal,
                email = emailVal,
                phone = phoneVal,
                role = role.value,
                sellerBusinessName = if (isSeller) businessNameVal else null,
                sellerFoodCategory = if (isSeller) foodCategory.value else null,
                sellerAddress = if (isSeller) addressVal else null,
                sellerStatus = if (isSeller) SellerStatus.PENDING else SellerStatus.NONE,
                profileCompleted = true,
                updatedAt = System.currentTimeMillis()
            )

            when (val result = saveUserProfileUseCase(updated)) {
                is Resource.Success -> {
                    result.data?.let { saved ->
                        _uiState.value = ProfileSetupUiState.Success(saved)
                    } ?: run {
                        _uiState.value = ProfileSetupUiState.Error("Profile saved, but session could not be refreshed.")
                    }
                }
                is Resource.Error -> {
                    _uiState.value = ProfileSetupUiState.Error(result.message ?: "Failed to save profile.")
                }
                is Resource.Loading -> {
                    _uiState.value = ProfileSetupUiState.Saving
                }
            }
        }
    }

    fun resetError() {
        _uiState.value = ProfileSetupUiState.Ready
    }
}

