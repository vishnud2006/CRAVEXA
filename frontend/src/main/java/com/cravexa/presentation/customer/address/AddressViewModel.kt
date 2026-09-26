package com.cravexa.presentation.customer.address

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cravexa.core.common.AuthValidator
import com.cravexa.core.common.Resource
import com.cravexa.domain.model.Address
import com.cravexa.domain.model.AddressType
import com.cravexa.domain.usecase.AddAddressUseCase
import com.cravexa.domain.usecase.DeleteAddressUseCase
import com.cravexa.domain.usecase.GetAddressesUseCase
import com.cravexa.domain.usecase.SetDefaultAddressUseCase
import com.cravexa.domain.usecase.UpdateAddressUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface AddressUiState {
    data object Idle : AddressUiState
    data object Saving : AddressUiState
    data class Success(val message: String) : AddressUiState
    data class Error(val message: String) : AddressUiState
}

@HiltViewModel
class AddressViewModel @Inject constructor(
    getAddressesUseCase: GetAddressesUseCase,
    private val addAddressUseCase: AddAddressUseCase,
    private val updateAddressUseCase: UpdateAddressUseCase,
    private val deleteAddressUseCase: DeleteAddressUseCase,
    private val setDefaultAddressUseCase: SetDefaultAddressUseCase
) : ViewModel() {

    val addresses: StateFlow<List<Address>> = getAddressesUseCase.observe()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _uiState = MutableStateFlow<AddressUiState>(AddressUiState.Idle)
    val uiState: StateFlow<AddressUiState> = _uiState.asStateFlow()

    // Form fields for Add/Edit
    var editingAddressId = MutableStateFlow<String?>(null)
        private set

    var fullName = MutableStateFlow("")
        private set

    var phone = MutableStateFlow("")
        private set

    var houseBuilding = MutableStateFlow("")
        private set

    var street = MutableStateFlow("")
        private set

    var area = MutableStateFlow("")
        private set

    var city = MutableStateFlow("Bengaluru")
        private set

    var state = MutableStateFlow("Karnataka")
        private set

    var pincode = MutableStateFlow("")
        private set

    var landmark = MutableStateFlow("")
        private set

    var addressType = MutableStateFlow(AddressType.HOME)
        private set

    var isDefault = MutableStateFlow(false)
        private set

    // Form validation errors
    private val _nameError = MutableStateFlow<String?>(null)
    val nameError: StateFlow<String?> = _nameError.asStateFlow()

    private val _phoneError = MutableStateFlow<String?>(null)
    val phoneError: StateFlow<String?> = _phoneError.asStateFlow()

    private val _houseError = MutableStateFlow<String?>(null)
    val houseError: StateFlow<String?> = _houseError.asStateFlow()

    private val _streetError = MutableStateFlow<String?>(null)
    val streetError: StateFlow<String?> = _streetError.asStateFlow()

    private val _areaError = MutableStateFlow<String?>(null)
    val areaError: StateFlow<String?> = _areaError.asStateFlow()

    private val _pincodeError = MutableStateFlow<String?>(null)
    val pincodeError: StateFlow<String?> = _pincodeError.asStateFlow()

    fun setupForAdd() {
        editingAddressId.value = null
        fullName.value = ""
        phone.value = ""
        houseBuilding.value = ""
        street.value = ""
        area.value = ""
        city.value = "Bengaluru"
        state.value = "Karnataka"
        pincode.value = ""
        landmark.value = ""
        addressType.value = AddressType.HOME
        isDefault.value = addresses.value.isEmpty()
        clearErrors()
        _uiState.value = AddressUiState.Idle
    }

    fun setupForEdit(address: Address) {
        editingAddressId.value = address.id
        fullName.value = address.fullName
        phone.value = address.phone
        houseBuilding.value = address.houseBuilding
        street.value = address.street
        area.value = address.area
        city.value = address.city
        state.value = address.state
        pincode.value = address.pincode
        landmark.value = address.landmark ?: ""
        addressType.value = address.addressType
        isDefault.value = address.isDefault
        clearErrors()
        _uiState.value = AddressUiState.Idle
    }

    fun onFullNameChanged(v: String) { fullName.value = v; if (_nameError.value != null) _nameError.value = null }
    fun onPhoneChanged(v: String) { phone.value = v; if (_phoneError.value != null) _phoneError.value = null }
    fun onHouseBuildingChanged(v: String) { houseBuilding.value = v; if (_houseError.value != null) _houseError.value = null }
    fun onStreetChanged(v: String) { street.value = v; if (_streetError.value != null) _streetError.value = null }
    fun onAreaChanged(v: String) { area.value = v; if (_areaError.value != null) _areaError.value = null }
    fun onCityChanged(v: String) { city.value = v }
    fun onStateChanged(v: String) { state.value = v }
    fun onPincodeChanged(v: String) { pincode.value = v; if (_pincodeError.value != null) _pincodeError.value = null }
    fun onLandmarkChanged(v: String) { landmark.value = v }
    fun onAddressTypeChanged(v: AddressType) { addressType.value = v }
    fun onIsDefaultChanged(v: Boolean) { isDefault.value = v }

    private fun clearErrors() {
        _nameError.value = null
        _phoneError.value = null
        _houseError.value = null
        _streetError.value = null
        _areaError.value = null
        _pincodeError.value = null
    }

    fun saveAddress() {
        var hasError = false

        val nameVal = fullName.value.trim()
        val phoneVal = phone.value.trim()
        val houseVal = houseBuilding.value.trim()
        val streetVal = street.value.trim()
        val areaVal = area.value.trim()
        val pinVal = pincode.value.trim()

        val nameValid = AuthValidator.validateName(nameVal)
        if (!nameValid.isValid) { _nameError.value = nameValid.errorMessage; hasError = true }

        val phoneValid = AuthValidator.validatePhone(phoneVal)
        if (!phoneValid.isValid) { _phoneError.value = phoneValid.errorMessage; hasError = true }

        if (houseVal.isBlank()) { _houseError.value = "House/Flat number is required."; hasError = true }
        if (streetVal.isBlank()) { _streetError.value = "Street name is required."; hasError = true }
        if (areaVal.isBlank()) { _areaError.value = "Area / Locality is required."; hasError = true }

        val pinValid = AuthValidator.validatePincode(pinVal)
        if (!pinValid.isValid) { _pincodeError.value = pinValid.errorMessage; hasError = true }

        if (hasError) return

        viewModelScope.launch {
            _uiState.value = AddressUiState.Saving
            val address = Address(
                id = editingAddressId.value ?: "",
                fullName = nameVal,
                phone = phoneVal,
                houseBuilding = houseVal,
                street = streetVal,
                area = areaVal,
                city = city.value.trim(),
                state = state.value.trim(),
                pincode = pinVal,
                landmark = landmark.value.trim().takeIf { it.isNotBlank() },
                addressType = addressType.value,
                isDefault = isDefault.value
            )

            val result = if (editingAddressId.value == null) {
                addAddressUseCase(address)
            } else {
                updateAddressUseCase(address)
            }

            when (result) {
                is Resource.Success -> {
                    _uiState.value = AddressUiState.Success(
                        if (editingAddressId.value == null) "Address added successfully!" else "Address updated successfully!"
                    )
                }
                is Resource.Error -> {
                    _uiState.value = AddressUiState.Error(result.message ?: "Failed to save address.")
                }
                is Resource.Loading -> {
                    _uiState.value = AddressUiState.Saving
                }
            }
        }
    }

    fun deleteAddress(addressId: String) {
        viewModelScope.launch {
            deleteAddressUseCase(addressId)
        }
    }

    fun setDefault(addressId: String) {
        viewModelScope.launch {
            setDefaultAddressUseCase(addressId)
        }
    }

    fun resetState() {
        _uiState.value = AddressUiState.Idle
    }
}

