package com.cravexa.presentation.wishlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cravexa.core.common.Resource
import com.cravexa.domain.model.Product
import com.cravexa.domain.usecase.AddToCartUseCase
import com.cravexa.domain.usecase.GetWishlistUseCase
import com.cravexa.domain.usecase.ToggleWishlistUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WishlistViewModel @Inject constructor(
    getWishlistUseCase: GetWishlistUseCase,
    private val toggleWishlistUseCase: ToggleWishlistUseCase,
    private val addToCartUseCase: AddToCartUseCase
) : ViewModel() {

    val wishlistProducts: StateFlow<List<Product>> = getWishlistUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _userMessage = MutableSharedFlow<String>()
    val userMessage: SharedFlow<String> = _userMessage.asSharedFlow()

    fun removeFromWishlist(product: Product) {
        viewModelScope.launch {
            toggleWishlistUseCase(product)
            _userMessage.emit("Removed ${product.name} from Wishlist")
        }
    }

    fun moveToCart(product: Product) {
        viewModelScope.launch {
            val result = addToCartUseCase(product, 1)
            if (result is Resource.Success) {
                toggleWishlistUseCase(product)
                _userMessage.emit("Moved ${product.name} to Cart")
            }
        }
    }
}

