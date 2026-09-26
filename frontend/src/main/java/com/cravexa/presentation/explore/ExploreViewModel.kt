package com.cravexa.presentation.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cravexa.core.common.Resource
import com.cravexa.domain.model.Category
import com.cravexa.domain.model.Product
import com.cravexa.domain.usecase.AddToCartUseCase
import com.cravexa.domain.usecase.GetCategoriesUseCase
import com.cravexa.domain.usecase.GetHomeMarketplaceUseCase
import com.cravexa.domain.usecase.GetWishlistUseCase
import com.cravexa.domain.usecase.ToggleWishlistUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    getCategoriesUseCase: GetCategoriesUseCase,
    getHomeMarketplaceUseCase: GetHomeMarketplaceUseCase,
    private val getWishlistUseCase: GetWishlistUseCase,
    private val toggleWishlistUseCase: ToggleWishlistUseCase,
    private val addToCartUseCase: AddToCartUseCase
) : ViewModel() {

    val categories: StateFlow<Resource<List<Category>>> = getCategoriesUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Resource.Loading()
        )

    val regionalSpecialties: StateFlow<Resource<List<Product>>> = getHomeMarketplaceUseCase.getRegionalSpecialties()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Resource.Loading()
        )

    val trendingProducts: StateFlow<Resource<List<Product>>> = getHomeMarketplaceUseCase.getTrendingProducts()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Resource.Loading()
        )

    val wishlistIds: StateFlow<Set<String>> = getWishlistUseCase()
        .map { list -> list.map { it.id }.toSet() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptySet()
        )

    private val _userMessage = MutableSharedFlow<String>()
    val userMessage: SharedFlow<String> = _userMessage.asSharedFlow()

    fun toggleWishlist(product: Product) {
        viewModelScope.launch {
            val result = toggleWishlistUseCase(product)
            if (result is Resource.Success) {
                val added = result.data == true
                _userMessage.emit(if (added) "Saved to your Wishlist ❤️" else "Removed from Wishlist")
            }
        }
    }

    fun addToCart(product: Product) {
        viewModelScope.launch {
            val result = addToCartUseCase(product, 1)
            if (result is Resource.Success) {
                _userMessage.emit("Added ${product.name} to Cart")
            }
        }
    }
}

