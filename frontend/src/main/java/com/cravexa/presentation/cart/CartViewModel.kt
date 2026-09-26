package com.cravexa.presentation.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cravexa.core.common.Resource
import com.cravexa.domain.model.CartItem
import com.cravexa.domain.usecase.CartCalculator
import com.cravexa.domain.usecase.ClearCartUseCase
import com.cravexa.domain.usecase.GetCartUseCase
import com.cravexa.domain.usecase.RemoveFromCartUseCase
import com.cravexa.domain.usecase.SaveForLaterUseCase
import com.cravexa.domain.usecase.UpdateCartQuantityUseCase
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

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getCartUseCase: GetCartUseCase,
    private val updateCartQuantityUseCase: UpdateCartQuantityUseCase,
    private val removeFromCartUseCase: RemoveFromCartUseCase,
    private val clearCartUseCase: ClearCartUseCase,
    private val saveForLaterUseCase: SaveForLaterUseCase,
    private val cartCalculator: CartCalculator
) : ViewModel() {

    private val _couponCode = MutableStateFlow<String?>(null)
    private val _couponDiscount = MutableStateFlow(0.0)

    val uiState: StateFlow<CartUiState> = combine(
        getCartUseCase.getCartItems(),
        _couponCode,
        _couponDiscount
    ) { items, coupon, discount ->
        val pricing = cartCalculator.calculate(items, discount)
        val count = items.sumOf { it.quantity }

        CartUiState(
            items = items,
            itemCount = count,
            subtotal = pricing.subtotal,
            deliveryFee = pricing.deliveryFee,
            platformFee = pricing.platformFee,
            isFreeDelivery = pricing.isFreeDelivery,
            total = pricing.total,
            appliedCoupon = coupon,
            promoDiscount = discount
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CartUiState(isLoading = true)
    )

    private val _userMessage = MutableSharedFlow<String>()
    val userMessage: SharedFlow<String> = _userMessage.asSharedFlow()

    fun incrementQuantity(item: CartItem) {
        val newQty = item.quantity + 1
        if (newQty <= item.product.stock) {
            viewModelScope.launch {
                updateCartQuantityUseCase(item.product.id, newQty)
            }
        } else {
            viewModelScope.launch {
                _userMessage.emit("Only ${item.product.stock} items are available in stock.")
            }
        }
    }

    fun decrementQuantity(item: CartItem) {
        val newQty = item.quantity - 1
        viewModelScope.launch {
            if (newQty <= 0) {
                removeFromCartUseCase(item.product.id)
                _userMessage.emit("Removed ${item.product.name} from Cart")
            } else {
                updateCartQuantityUseCase(item.product.id, newQty)
            }
        }
    }

    fun removeItem(item: CartItem) {
        viewModelScope.launch {
            val result = removeFromCartUseCase(item.product.id)
            if (result is Resource.Success) {
                _userMessage.emit("Removed ${item.product.name} from Cart")
            }
        }
    }

    fun saveForLater(item: CartItem) {
        viewModelScope.launch {
            val result = saveForLaterUseCase(item)
            if (result is Resource.Success) {
                _userMessage.emit("Saved ${item.product.name} to Wishlist ❤️")
            } else {
                _userMessage.emit(result.message ?: "Failed to save for later.")
            }
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            clearCartUseCase()
            _userMessage.emit("Cart cleared")
        }
    }

    fun applyCoupon(code: String) {
        val trimmed = code.trim().uppercase()
        viewModelScope.launch {
            when (trimmed) {
                "CRAVE10" -> {
                    _couponCode.value = "CRAVE10"
                    _couponDiscount.value = 50.0
                    _userMessage.emit("Coupon CRAVE10 applied! ₹50 saved.")
                }
                "HOMEMADE20" -> {
                    _couponCode.value = "HOMEMADE20"
                    _couponDiscount.value = 100.0
                    _userMessage.emit("Coupon HOMEMADE20 applied! ₹100 saved.")
                }
                else -> {
                    _userMessage.emit("Invalid coupon code.")
                }
            }
        }
    }

    fun removeCoupon() {
        _couponCode.value = null
        _couponDiscount.value = 0.0
    }
}
