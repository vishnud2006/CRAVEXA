package com.cravexa.presentation.seller.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cravexa.core.common.AuthValidator
import com.cravexa.core.common.Resource
import com.cravexa.domain.model.SellerProfile
import com.cravexa.domain.usecase.GetSellerProfileUseCase
import com.cravexa.domain.usecase.LogoutUseCase
import com.cravexa.domain.usecase.SubmitFssaiUseCase
import com.cravexa.domain.usecase.UpdateSellerProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface SellerProfileUiState {
    data object Idle : SellerProfileUiState
    data object Loading : SellerProfileUiState
    data object Saving : SellerProfileUiState
    data class Success(val message: String) : SellerProfileUiState
    data class Error(val message: String) : SellerProfileUiState
}

@HiltViewModel
class SellerProfileViewModel @Inject constructor(
    getSellerProfileUseCase: GetSellerProfileUseCase,
    private val updateSellerProfileUseCase: UpdateSellerProfileUseCase,
    private val submitFssaiUseCase: SubmitFssaiUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    val profile: StateFlow<SellerProfile?> = getSellerProfileUseCase.observe()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    private val _uiState = MutableStateFlow<SellerProfileUiState>(SellerProfileUiState.Idle)
    val uiState: StateFlow<SellerProfileUiState> = _uiState.asStateFlow()

    // FSSAI input state
    var fssaiNumber = MutableStateFlow("")
        private set

    private val _fssaiError = MutableStateFlow<String?>(null)
    val fssaiError: StateFlow<String?> = _fssaiError.asStateFlow()

    // Business details state
    var businessName = MutableStateFlow("")
        private set

    var about = MutableStateFlow("")
        private set

    var businessAddress = MutableStateFlow("")
        private set

    var city = MutableStateFlow("Bengaluru")
        private set

    var state = MutableStateFlow("Karnataka")
        private set

    var pincode = MutableStateFlow("560038")
        private set

    private val _businessNameError = MutableStateFlow<String?>(null)
    val businessNameError: StateFlow<String?> = _businessNameError.asStateFlow()

    private val _addressError = MutableStateFlow<String?>(null)
    val addressError: StateFlow<String?> = _addressError.asStateFlow()

    private val _pincodeError = MutableStateFlow<String?>(null)
    val pincodeError: StateFlow<String?> = _pincodeError.asStateFlow()

    fun populateFromProfile(sellerProfile: SellerProfile) {
        businessName.value = sellerProfile.businessName
        about.value = sellerProfile.about
        businessAddress.value = sellerProfile.businessAddress
        city.value = sellerProfile.city
        state.value = sellerProfile.state
        pincode.value = sellerProfile.pincode
        fssaiNumber.value = sellerProfile.fssaiNumber
    }

    fun onFssaiNumberChanged(v: String) {
        fssaiNumber.value = v
        if (_fssaiError.value != null) _fssaiError.value = null
    }

    fun onBusinessNameChanged(v: String) {
        businessName.value = v
        if (_businessNameError.value != null) _businessNameError.value = null
    }

    fun onAboutChanged(v: String) { about.value = v }
    fun onAddressChanged(v: String) {
        businessAddress.value = v
        if (_addressError.value != null) _addressError.value = null
    }
    fun onCityChanged(v: String) { city.value = v }
    fun onStateChanged(v: String) { state.value = v }
    fun onPincodeChanged(v: String) {
        pincode.value = v
        if (_pincodeError.value != null) _pincodeError.value = null
    }

    fun submitFssai() {
        val fssaiVal = fssaiNumber.value.trim()
        val validation = AuthValidator.validateFssaiNumber(fssaiVal)
        if (!validation.isValid) {
            _fssaiError.value = validation.errorMessage
            return
        }

        viewModelScope.launch {
            _uiState.value = SellerProfileUiState.Saving
            when (val result = submitFssaiUseCase(fssaiVal)) {
                is Resource.Success -> {
                    _uiState.value = SellerProfileUiState.Success("FSSAI details submitted for verification!")
                }
                is Resource.Error -> {
                    _uiState.value = SellerProfileUiState.Error(result.message ?: "Failed to submit FSSAI details.")
                }
                is Resource.Loading -> {
                    _uiState.value = SellerProfileUiState.Saving
                }
            }
        }
    }

    fun saveBusinessDetails() {
        var hasError = false
        val bName = businessName.value.trim()
        val addr = businessAddress.value.trim()
        val pin = pincode.value.trim()

        val bValid = AuthValidator.validateBusinessName(bName)
        if (!bValid.isValid) { _businessNameError.value = bValid.errorMessage; hasError = true }

        val aValid = AuthValidator.validateAddress(addr)
        if (!aValid.isValid) { _addressError.value = aValid.errorMessage; hasError = true }

        val pValid = AuthValidator.validatePincode(pin)
        if (!pValid.isValid) { _pincodeError.value = pValid.errorMessage; hasError = true }

        if (hasError) return

        viewModelScope.launch {
            _uiState.value = SellerProfileUiState.Saving
            val current = profile.value ?: SellerProfile()
            val updated = current.copy(
                businessName = bName,
                about = about.value.trim(),
                businessAddress = addr,
                city = city.value.trim(),
                state = state.value.trim(),
                pincode = pin
            )

            when (val result = updateSellerProfileUseCase(updated)) {
                is Resource.Success -> {
                    _uiState.value = SellerProfileUiState.Success("Kitchen profile updated successfully!")
                }
                is Resource.Error -> {
                    _uiState.value = SellerProfileUiState.Error(result.message ?: "Failed to update profile.")
                }
                is Resource.Loading -> {
                    _uiState.value = SellerProfileUiState.Saving
                }
            }
        }
    }

    fun logout(onComplete: () -> Unit) {
        viewModelScope.launch {
            logoutUseCase()
            onComplete()
        }
    }

    fun resetState() {
        _uiState.value = SellerProfileUiState.Idle
    }
}

