package com.cravexa.presentation.admin.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cravexa.core.common.Resource
import com.cravexa.domain.model.AdminProduct
import com.cravexa.domain.model.AdminProductStatus
import com.cravexa.domain.usecase.ApproveProductUseCase
import com.cravexa.domain.usecase.GetAdminProductsUseCase
import com.cravexa.domain.usecase.RejectProductUseCase
import com.cravexa.domain.usecase.ToggleProductStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface AdminProductsUiState {
    data object Loading : AdminProductsUiState
    data class Success(
        val products: List<AdminProduct>,
        val filteredProducts: List<AdminProduct>,
        val selectedStatusFilter: AdminProductStatus?
    ) : AdminProductsUiState
    data class Error(val message: String) : AdminProductsUiState
}

@HiltViewModel
class AdminProductsViewModel @Inject constructor(
    private val getAdminProductsUseCase: GetAdminProductsUseCase,
    private val approveProductUseCase: ApproveProductUseCase,
    private val rejectProductUseCase: RejectProductUseCase,
    private val toggleProductStatusUseCase: ToggleProductStatusUseCase
) : ViewModel() {

    private val _statusFilter = MutableStateFlow<AdminProductStatus?>(null)
    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    val uiState: StateFlow<AdminProductsUiState> = combine(
        getAdminProductsUseCase.productsFlow,
        _statusFilter
    ) { products, status ->
        val filtered = if (status == null) products else products.filter { it.status == status }
        AdminProductsUiState.Success(
            products = products,
            filteredProducts = filtered,
            selectedStatusFilter = status
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AdminProductsUiState.Loading
    )

    init {
        getAdminProductsUseCase().launchIn(viewModelScope)
    }

    fun onStatusFilterSelected(status: AdminProductStatus?) {
        _statusFilter.value = status
    }

    fun approveProduct(productId: String) {
        viewModelScope.launch {
            val result = approveProductUseCase(productId)
            if (result is Resource.Success) {
                _toastMessage.value = "Dish approved and published to marketplace!"
            } else {
                _toastMessage.value = result.message ?: "Failed to approve product"
            }
        }
    }

    fun rejectProduct(productId: String) {
        viewModelScope.launch {
            val result = rejectProductUseCase(productId)
            if (result is Resource.Success) {
                _toastMessage.value = "Product rejected."
            } else {
                _toastMessage.value = result.message ?: "Failed to reject product"
            }
        }
    }

    fun toggleProductDisable(productId: String, currentlyDisabled: Boolean) {
        viewModelScope.launch {
            val result = toggleProductStatusUseCase(productId, disable = !currentlyDisabled)
            if (result is Resource.Success) {
                _toastMessage.value = if (!currentlyDisabled) "Product disabled from public view" else "Product re-enabled"
            } else {
                _toastMessage.value = result.message ?: "Failed to update product status"
            }
        }
    }

    fun clearToast() {
        _toastMessage.value = null
    }
}
