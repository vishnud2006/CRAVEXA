package com.cravexa.presentation.seller.publicprofile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cravexa.core.common.Resource
import com.cravexa.domain.model.Product
import com.cravexa.domain.usecase.AddToCartUseCase
import com.cravexa.domain.usecase.GetSellerProductsUseCase
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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PublicSellerInfo(
    val id: String,
    val brandName: String,
    val creatorName: String,
    val location: String,
    val story: String,
    val memberSince: String,
    val isVerified: Boolean,
    val fssaiCompliant: Boolean,
    val rating: Double,
    val reviewCount: Int
)

@HiltViewModel
class PublicSellerProfileViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getSellerProductsUseCase: GetSellerProductsUseCase,
    private val getWishlistUseCase: GetWishlistUseCase,
    private val toggleWishlistUseCase: ToggleWishlistUseCase,
    private val addToCartUseCase: AddToCartUseCase
) : ViewModel() {

    val sellerId: String = savedStateHandle["sellerId"] ?: "sel_1"

    private val _sellerInfo = MutableStateFlow<PublicSellerInfo?>(null)
    val sellerInfo: StateFlow<PublicSellerInfo?> = _sellerInfo.asStateFlow()

    private val _productsState = MutableStateFlow<Resource<List<Product>>>(Resource.Loading())
    val productsState: StateFlow<Resource<List<Product>>> = _productsState.asStateFlow()

    val wishlistIds: StateFlow<Set<String>> = getWishlistUseCase()
        .map { list -> list.map { it.id }.toSet() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptySet()
        )

    private val _userMessage = MutableSharedFlow<String>()
    val userMessage: SharedFlow<String> = _userMessage.asSharedFlow()

    init {
        loadSellerProfile()
    }

    fun loadSellerProfile() {
        // Load seller info safely
        val info = when (sellerId) {
            "sel_1" -> PublicSellerInfo(
                id = "sel_1",
                brandName = "Lakshmi's Home Kitchen",
                creatorName = "Lakshmi Devi",
                location = "Guntur, Andhra Pradesh",
                story = "Crafting authentic Andhra avakaya pickles and sun-dried podis since 2012 using traditional clay jars and wood-pressed sesame oil.",
                memberSince = "Creator since 2024",
                isVerified = true,
                fssaiCompliant = true,
                rating = 4.9,
                reviewCount = 173
            )
            "sel_2" -> PublicSellerInfo(
                id = "sel_2",
                brandName = "Srivari Sweets & Delights",
                creatorName = "Srinivasa Rao",
                location = "Mysuru, Karnataka",
                story = "Specializing in traditional royal Mysore Pak and South Indian desi ghee delicacies made with heritage copper vessel slow-roasting techniques.",
                memberSince = "Creator since 2024",
                isVerified = true,
                fssaiCompliant = true,
                rating = 4.8,
                reviewCount = 98
            )
            "sel_4" -> PublicSellerInfo(
                id = "sel_4",
                brandName = "Meenakshi Ammal Podi Studio",
                creatorName = "Meenakshi Sundaram",
                location = "Madurai, Tamil Nadu",
                story = "Heritage idli podis and sambar masalas roasted on low heat using handpicked single-origin spices and cold-pressed gingelly oil.",
                memberSince = "Creator since 2024",
                isVerified = true,
                fssaiCompliant = true,
                rating = 4.9,
                reviewCount = 137
            )
            "sel_7" -> PublicSellerInfo(
                id = "sel_7",
                brandName = "Prakriti Wholesome Bakes",
                creatorName = "Ananya Sharma",
                location = "Bengaluru, Karnataka",
                story = "Slow-fermented artisan sourdough breads, sprouted millet cookies, and refined-sugar-free wholesome bakes prepared in small home batches.",
                memberSince = "Creator since 2025",
                isVerified = true,
                fssaiCompliant = true,
                rating = 4.8,
                reviewCount = 85
            )
            else -> PublicSellerInfo(
                id = sellerId,
                brandName = "Artisanal Home Kitchen",
                creatorName = "Home Food Creator",
                location = "India",
                story = "Passionate homemade food artisan bringing authentic traditional flavors to your doorstep.",
                memberSince = "Creator since 2025",
                isVerified = false,
                fssaiCompliant = true,
                rating = 4.7,
                reviewCount = 42
            )
        }
        _sellerInfo.value = info

        viewModelScope.launch {
            getSellerProductsUseCase(sellerId).collect { res ->
                _productsState.value = res
            }
        }
    }

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

