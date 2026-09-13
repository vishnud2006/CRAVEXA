package com.cravexa.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cravexa.core.common.Resource
import com.cravexa.domain.model.Category
import com.cravexa.domain.model.HeroBanner
import com.cravexa.domain.model.Product
import com.cravexa.domain.model.UserProfile
import com.cravexa.domain.usecase.AddToCartUseCase
import com.cravexa.domain.usecase.GetCartUseCase
import com.cravexa.domain.usecase.GetHomeMarketplaceUseCase
import com.cravexa.domain.usecase.GetUserProfileUseCase
import com.cravexa.domain.usecase.GetWishlistUseCase
import com.cravexa.domain.usecase.LogoutUseCase
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
class HomeViewModel @Inject constructor(
    getUserProfileUseCase: GetUserProfileUseCase,
    private val getHomeMarketplaceUseCase: GetHomeMarketplaceUseCase,
    private val getWishlistUseCase: GetWishlistUseCase,
    private val toggleWishlistUseCase: ToggleWishlistUseCase,
    private val getCartUseCase: GetCartUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    val currentUser: StateFlow<UserProfile?> = getUserProfileUseCase.observe()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    val heroBanners: StateFlow<List<HeroBanner>> = getHomeMarketplaceUseCase.getHeroBanners()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val categories: StateFlow<Resource<List<Category>>> = getHomeMarketplaceUseCase.getCategories()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Resource.Loading()
        )

    val featuredProducts: StateFlow<Resource<List<Product>>> = getHomeMarketplaceUseCase.getFeaturedProducts()
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

    val recommendedProducts: StateFlow<Resource<List<Product>>> = getHomeMarketplaceUseCase.getRecommendedProducts()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Resource.Loading()
        )

    val regionalProducts: StateFlow<Resource<List<Product>>> = getHomeMarketplaceUseCase.getRegionalSpecialties()
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

    val cartItemCount: StateFlow<Int> = getCartUseCase.getCartItemCount()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
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
            } else if (result is Resource.Error) {
                _userMessage.emit(result.message ?: "Failed to add to cart")
            }
        }
    }

    fun logout(onLogoutComplete: () -> Unit) {
        viewModelScope.launch {
            logoutUseCase()
            onLogoutComplete()
        }
    }
}
