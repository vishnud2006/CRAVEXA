package com.cravexa.presentation.seller.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cravexa.core.common.Resource
import com.cravexa.domain.model.Product
import com.cravexa.domain.usecase.DeleteSellerProductUseCase
import com.cravexa.domain.usecase.GetSellerKitchenProductsUseCase
import com.cravexa.domain.usecase.ToggleProductAvailabilityUseCase
import com.cravexa.domain.usecase.UpdateProductStockUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class ProductFilter(val title: String) {
    ALL("All Dishes"),
    ACTIVE("Active"),
    LOW_STOCK("Low Stock"),
    OUT_OF_STOCK("Out of Stock"),
    DISABLED("Disabled")
}

sealed interface SellerProductsUiState {
    data object Loading : SellerProductsUiState
    data class Success(
        val products: List<Product>,
        val filteredProducts: List<Product>,
        val activeFilter: ProductFilter,
        val searchQuery: String
    ) : SellerProductsUiState
    data class Error(val message: String) : SellerProductsUiState
}

@HiltViewModel
class SellerProductsViewModel @Inject constructor(
    private val getSellerKitchenProductsUseCase: GetSellerKitchenProductsUseCase,
    private val updateProductStockUseCase: UpdateProductStockUseCase,
    private val toggleProductAvailabilityUseCase: ToggleProductAvailabilityUseCase,
    private val deleteSellerProductUseCase: DeleteSellerProductUseCase
) : ViewModel() {

    private val _activeFilter = MutableStateFlow(ProductFilter.ALL)
    val activeFilter: StateFlow<ProductFilter> = _activeFilter.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    val uiState: StateFlow<SellerProductsUiState> = combine(
        getSellerKitchenProductsUseCase.productsFlow,
        _activeFilter,
        _searchQuery
    ) { products: List<Product>, filter: ProductFilter, query: String ->
        val filtered = products.filter { product: Product ->
            val matchesFilter = when (filter) {
                ProductFilter.ALL -> true
                ProductFilter.ACTIVE -> product.available && product.stock > 0
                ProductFilter.LOW_STOCK -> product.stock in 1..5
                ProductFilter.OUT_OF_STOCK -> product.stock == 0
                ProductFilter.DISABLED -> !product.available
            }

            val matchesQuery = if (query.isBlank()) {
                true
            } else {
                product.name.contains(query, ignoreCase = true) ||
                product.categoryName.contains(query, ignoreCase = true) ||
                product.region.contains(query, ignoreCase = true)
            }

            matchesFilter && matchesQuery
        }

        SellerProductsUiState.Success(
            products = products,
            filteredProducts = filtered,
            activeFilter = filter,
            searchQuery = query
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SellerProductsUiState.Loading
    )

    fun onFilterSelected(filter: ProductFilter) {
        _activeFilter.value = filter
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun updateStock(productId: String, newStock: Int) {
        viewModelScope.launch {
            when (val result = updateProductStockUseCase(productId, newStock)) {
                is Resource.Success -> {
                    _toastMessage.value = "Stock updated successfully."
                }
                is Resource.Error -> {
                    _toastMessage.value = result.message ?: "Failed to update stock."
                }
                else -> Unit
            }
        }
    }

    fun toggleAvailability(productId: String, isAvailable: Boolean) {
        viewModelScope.launch {
            when (val result = toggleProductAvailabilityUseCase(productId, isAvailable)) {
                is Resource.Success -> {
                    _toastMessage.value = if (isAvailable) "Product enabled." else "Product disabled."
                }
                is Resource.Error -> {
                    _toastMessage.value = result.message ?: "Failed to toggle availability."
                }
                else -> Unit
            }
        }
    }

    fun deleteProduct(productId: String) {
        viewModelScope.launch {
            when (val result = deleteSellerProductUseCase(productId)) {
                is Resource.Success -> {
                    _toastMessage.value = "Product removed from kitchen catalog."
                }
                is Resource.Error -> {
                    _toastMessage.value = result.message ?: "Failed to delete product."
                }
                else -> Unit
            }
        }
    }

    fun clearToast() {
        _toastMessage.value = null
    }
}
