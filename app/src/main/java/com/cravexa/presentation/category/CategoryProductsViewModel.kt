package com.cravexa.presentation.category

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cravexa.core.common.Resource
import com.cravexa.domain.model.Category
import com.cravexa.domain.model.Product
import com.cravexa.domain.model.SortOption
import com.cravexa.domain.usecase.AddToCartUseCase
import com.cravexa.domain.usecase.GetCategoriesUseCase
import com.cravexa.domain.usecase.GetCategoryProductsUseCase
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

@HiltViewModel
class CategoryProductsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getCategoryProductsUseCase: GetCategoryProductsUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getWishlistUseCase: GetWishlistUseCase,
    private val toggleWishlistUseCase: ToggleWishlistUseCase,
    private val addToCartUseCase: AddToCartUseCase
) : ViewModel() {

    val categoryId: String = savedStateHandle["categoryId"] ?: ""
    val categoryName: String = savedStateHandle["categoryName"] ?: "Products"

    private val _productsState = MutableStateFlow<Resource<List<Product>>>(Resource.Loading())
    val productsState: StateFlow<Resource<List<Product>>> = _productsState.asStateFlow()

    private val _categoryInfo = MutableStateFlow<Category?>(null)
    val categoryInfo: StateFlow<Category?> = _categoryInfo.asStateFlow()

    private val _selectedSort = MutableStateFlow(SortOption.RELEVANCE)
    val selectedSort: StateFlow<SortOption> = _selectedSort.asStateFlow()

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
        loadCategoryData()
    }

    fun loadCategoryData() {
        viewModelScope.launch {
            getCategoriesUseCase.getCategoryById(categoryId).collect { res ->
                if (res is Resource.Success) {
                    _categoryInfo.value = res.data
                }
            }
        }
        viewModelScope.launch {
            getCategoryProductsUseCase(categoryId).collect { res ->
                _productsState.value = res
            }
        }
    }

    fun setSortOption(sort: SortOption) {
        _selectedSort.value = sort
        val current = _productsState.value
        if (current is Resource.Success && current.data != null) {
            val sorted = when (sort) {
                SortOption.RELEVANCE -> current.data
                SortOption.PRICE_LOW_TO_HIGH -> current.data.sortedBy { it.price }
                SortOption.PRICE_HIGH_TO_LOW -> current.data.sortedByDescending { it.price }
                SortOption.RATING -> current.data.sortedByDescending { it.rating }
                SortOption.NEWEST -> current.data.reversed()
            }
            _productsState.value = Resource.Success(sorted)
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

