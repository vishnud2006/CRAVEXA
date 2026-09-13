package com.cravexa.presentation.seller.orders

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cravexa.core.common.Resource
import com.cravexa.domain.model.Order
import com.cravexa.domain.model.OrderStatus
import com.cravexa.domain.usecase.GetSellerOrderDetailUseCase
import com.cravexa.domain.usecase.UpdateSellerOrderStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface SellerOrderDetailUiState {
    data object Loading : SellerOrderDetailUiState
    data class Success(val order: Order) : SellerOrderDetailUiState
    data class Error(val message: String) : SellerOrderDetailUiState
}

@HiltViewModel
class SellerOrderDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getSellerOrderDetailUseCase: GetSellerOrderDetailUseCase,
    private val updateSellerOrderStatusUseCase: UpdateSellerOrderStatusUseCase
) : ViewModel() {

    val orderId: String = savedStateHandle.get<String>("orderId") ?: ""

    private val _uiState = MutableStateFlow<SellerOrderDetailUiState>(SellerOrderDetailUiState.Loading)
    val uiState: StateFlow<SellerOrderDetailUiState> = _uiState.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    init {
        loadOrder()
    }

    fun loadOrder() {
        if (orderId.isBlank()) {
            _uiState.value = SellerOrderDetailUiState.Error("Invalid order ID.")
            return
        }

        viewModelScope.launch {
            _uiState.value = SellerOrderDetailUiState.Loading
            when (val result = getSellerOrderDetailUseCase(orderId)) {
                is Resource.Success -> {
                    result.data?.let {
                        _uiState.value = SellerOrderDetailUiState.Success(it)
                    } ?: run {
                        _uiState.value = SellerOrderDetailUiState.Error("Order not found.")
                    }
                }
                is Resource.Error -> {
                    _uiState.value = SellerOrderDetailUiState.Error(result.message ?: "Failed to load order.")
                }
                else -> Unit
            }
        }
    }

    fun updateStatus(newStatus: OrderStatus) {
        viewModelScope.launch {
            when (val result = updateSellerOrderStatusUseCase(orderId, newStatus)) {
                is Resource.Success -> {
                    result.data?.let { updated ->
                        _uiState.value = SellerOrderDetailUiState.Success(updated)
                        _toastMessage.value = "Status updated to ${newStatus.displayTitle}."
                    }
                }
                is Resource.Error -> {
                    _toastMessage.value = result.message ?: "Failed to update order status."
                }
                else -> Unit
            }
        }
    }

    fun clearToast() {
        _toastMessage.value = null
    }
}

