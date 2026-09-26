package com.cravexa.presentation.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cravexa.core.common.Resource
import com.cravexa.domain.model.Address
import com.cravexa.domain.model.PaymentMethod
import com.cravexa.domain.repository.AuthRepository
import com.cravexa.domain.usecase.CartCalculator
import com.cravexa.domain.usecase.CreateOrderUseCase
import com.cravexa.domain.usecase.GetAddressesUseCase
import com.cravexa.domain.usecase.GetCartUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface CheckoutEvent {
    data class OrderPlacedSuccess(val orderId: String, val orderNumber: String) : CheckoutEvent
    data class ShowMessage(val message: String) : CheckoutEvent
    data object RequireLogin : CheckoutEvent
}

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    getCartUseCase: GetCartUseCase,
    getAddressesUseCase: GetAddressesUseCase,
    private val cartCalculator: CartCalculator,
    private val createOrderUseCase: CreateOrderUseCase,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _selectedAddress = MutableStateFlow<Address?>(null)
    private val _selectedPaymentMethod = MutableStateFlow(PaymentMethod.ONLINE)
    private val _appliedCoupon = MutableStateFlow<String?>(null)
    private val _promoDiscount = MutableStateFlow(0.0)
    private val _isPlacingOrder = MutableStateFlow(false)
    private val _errorMessage = MutableStateFlow<String?>(null)

    private val _events = MutableSharedFlow<CheckoutEvent>()
    val events: SharedFlow<CheckoutEvent> = _events.asSharedFlow()

    private data class CheckoutOptions(
        val selectedAddress: Address? = null,
        val selectedPaymentMethod: PaymentMethod = PaymentMethod.ONLINE,
        val appliedCoupon: String? = null,
        val promoDiscount: Double = 0.0,
        val isPlacingOrder: Boolean = false
    )

    private val _checkoutOptions = combine(
        _selectedAddress,
        _selectedPaymentMethod,
        _appliedCoupon,
        _promoDiscount,
        _isPlacingOrder
    ) { selectedAddress, paymentMethod, coupon, discount, isPlacing ->
        CheckoutOptions(
            selectedAddress = selectedAddress,
            selectedPaymentMethod = paymentMethod,
            appliedCoupon = coupon,
            promoDiscount = discount,
            isPlacingOrder = isPlacing
        )
    }

    val uiState: StateFlow<CheckoutUiState> = combine(
        getCartUseCase.getCartItems(),
        getAddressesUseCase.observe(),
        _checkoutOptions,
        _errorMessage
    ) { items, addresses, options, error ->
        val resolvedAddress = options.selectedAddress
            ?: addresses.find { it.isDefault }
            ?: addresses.firstOrNull()

        val pricing = cartCalculator.calculate(items, options.promoDiscount)

        CheckoutUiState(
            items = items,
            pricing = pricing,
            addresses = addresses,
            selectedAddress = resolvedAddress,
            selectedPaymentMethod = options.selectedPaymentMethod,
            appliedCoupon = options.appliedCoupon,
            promoDiscount = options.promoDiscount,
            isLoading = false,
            isPlacingOrder = options.isPlacingOrder,
            errorMessage = error
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CheckoutUiState(isLoading = true)
    )

    fun selectAddress(address: Address) {
        _selectedAddress.value = address
    }

    fun selectPaymentMethod(method: PaymentMethod) {
        _selectedPaymentMethod.value = method
    }

    fun applyCoupon(code: String) {
        val trimmed = code.trim().uppercase()
        when (trimmed) {
            "CRAVE10" -> {
                _appliedCoupon.value = "CRAVE10"
                _promoDiscount.value = 50.0
                viewModelScope.launch {
                    _events.emit(CheckoutEvent.ShowMessage("Coupon CRAVE10 applied! ₹50 saved."))
                }
            }
            "HOMEMADE20" -> {
                _appliedCoupon.value = "HOMEMADE20"
                _promoDiscount.value = 100.0
                viewModelScope.launch {
                    _events.emit(CheckoutEvent.ShowMessage("Coupon HOMEMADE20 applied! ₹100 saved."))
                }
            }
            else -> {
                viewModelScope.launch {
                    _events.emit(CheckoutEvent.ShowMessage("Invalid coupon code."))
                }
            }
        }
    }

    fun removeCoupon() {
        _appliedCoupon.value = null
        _promoDiscount.value = 0.0
    }

    fun placeOrder() {
        val currentState = uiState.value
        if (_isPlacingOrder.value) return // Prevent accidental double taps

        if (currentState.items.isEmpty()) {
            viewModelScope.launch {
                _events.emit(CheckoutEvent.ShowMessage("Your cart is empty."))
            }
            return
        }

        if (currentState.selectedAddress == null) {
            viewModelScope.launch {
                _events.emit(CheckoutEvent.ShowMessage("Please add or select a delivery address."))
            }
            return
        }

        viewModelScope.launch {
            val user = authRepository.getCurrentUser()
            if (user == null) {
                _events.emit(CheckoutEvent.RequireLogin)
                return@launch
            }

            _isPlacingOrder.value = true
            _errorMessage.value = null

            val result = createOrderUseCase(
                items = currentState.items,
                deliveryAddress = currentState.selectedAddress,
                paymentMethod = currentState.selectedPaymentMethod,
                appliedCoupon = currentState.appliedCoupon,
                couponDiscount = currentState.promoDiscount
            )

            when (result) {
                is Resource.Success -> {
                    _isPlacingOrder.value = false
                    val createdOrder = result.data
                    if (createdOrder != null) {
                        _events.emit(
                            CheckoutEvent.OrderPlacedSuccess(
                                orderId = createdOrder.id,
                                orderNumber = createdOrder.orderNumber
                            )
                        )
                    }
                }
                is Resource.Error -> {
                    _isPlacingOrder.value = false
                    val error = result.message ?: "Failed to place order. Please try again."
                    _errorMessage.value = error
                    _events.emit(CheckoutEvent.ShowMessage(error))
                }
                is Resource.Loading -> {
                    _isPlacingOrder.value = true
                }
            }
        }
    }
}

