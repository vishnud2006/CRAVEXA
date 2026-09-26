package com.cravexa.presentation.product

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cravexa.core.common.Resource
import com.cravexa.domain.model.Product
import com.cravexa.domain.usecase.AddToCartUseCase
import com.cravexa.domain.usecase.GetProductDetailUseCase
import com.cravexa.domain.usecase.GetWishlistUseCase
import com.cravexa.domain.usecase.ToggleWishlistUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getProductDetailUseCase: GetProductDetailUseCase,
    private val getWishlistUseCase: GetWishlistUseCase,
    private val toggleWishlistUseCase: ToggleWishlistUseCase,
    private val addToCartUseCase: AddToCartUseCase
) : ViewModel() {

    val productId: String = savedStateHandle["productId"] ?: ""

    private val _productState = MutableStateFlow<Resource<Product>>(Resource.Loading())
    val productState: StateFlow<Resource<Product>> = _productState.asStateFlow()

    private val _relatedProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Loading())
    val relatedProducts: StateFlow<Resource<List<Product>>> = _relatedProducts.asStateFlow()

    private val _quantity = MutableStateFlow(1)
    val quantity: StateFlow<Int> = _quantity.asStateFlow()

    val isWishlisted: StateFlow<Boolean> = getWishlistUseCase.isWishlisted(productId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    private val _userMessage = MutableSharedFlow<String>()
    val userMessage: SharedFlow<String> = _userMessage.asSharedFlow()

    init {
        loadProduct()
    }

    fun loadProduct() {
        viewModelScope.launch {
            getProductDetailUseCase(productId).collect { res ->
                _productState.value = res
                if (res is Resource.Success && res.data != null) {
                    loadRelated(res.data.categoryId)
                }
            }
        }
    }

    private fun loadRelated(categoryId: String) {
        viewModelScope.launch {
            getProductDetailUseCase.getRelatedProducts(productId, categoryId).collect { res ->
                _relatedProducts.value = res
            }
        }
    }

    fun incrementQuantity() {
        val current = _quantity.value
        val maxStock = (_productState.value as? Resource.Success)?.data?.stock ?: 10
        if (current < maxStock) {
            _quantity.value = current + 1
        }
    }

    fun decrementQuantity() {
        val current = _quantity.value
        if (current > 1) {
            _quantity.value = current - 1
        }
    }

    fun toggleWishlist() {
        val current = (_productState.value as? Resource.Success)?.data ?: return
        viewModelScope.launch {
            val result = toggleWishlistUseCase(current)
            if (result is Resource.Success) {
                val added = result.data == true
                _userMessage.emit(if (added) "Saved to your Wishlist ❤️" else "Removed from Wishlist")
            }
        }
    }

    fun addToCart() {
        val product = (_productState.value as? Resource.Success)?.data ?: return
        addToCartProduct(product, _quantity.value)
    }

    fun addToCartProduct(product: Product, quantity: Int = 1) {
        viewModelScope.launch {
            val result = addToCartUseCase(product, quantity)
            if (result is Resource.Success) {
                _userMessage.emit("Added ${if (quantity > 1) "${quantity}x " else ""}${product.name} to Cart")
            } else if (result is Resource.Error) {
                _userMessage.emit(result.message ?: "Failed to add to cart")
            }
        }
    }

    fun toggleWishlistProduct(product: Product) {
        viewModelScope.launch {
            val result = toggleWishlistUseCase(product)
            if (result is Resource.Success) {
                val added = result.data == true
                _userMessage.emit(if (added) "Saved ${product.name} to Wishlist ❤️" else "Removed from Wishlist")
            }
        }
    }
}

