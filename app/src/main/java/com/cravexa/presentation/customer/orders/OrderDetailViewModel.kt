package com.cravexa.presentation.customer.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cravexa.core.common.Resource
import com.cravexa.domain.model.Order
import com.cravexa.domain.usecase.GetOrderDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface OrderDetailUiState {
    data object Loading : OrderDetailUiState
    data class Success(val order: Order) : OrderDetailUiState
    data class Error(val message: String) : OrderDetailUiState
}

@HiltViewModel
class OrderDetailViewModel @Inject constructor(
    private val getOrderDetailUseCase: GetOrderDetailUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<OrderDetailUiState>(OrderDetailUiState.Loading)
    val uiState: StateFlow<OrderDetailUiState> = _uiState.asStateFlow()

    fun loadOrder(orderId: String) {
        viewModelScope.launch {
            _uiState.value = OrderDetailUiState.Loading
            when (val result = getOrderDetailUseCase(orderId)) {
                is Resource.Success -> {
                    result.data?.let { order ->
                        _uiState.value = OrderDetailUiState.Success(order)
                    } ?: run {
                        _uiState.value = OrderDetailUiState.Error("Order details could not be found.")
                    }
                }
                is Resource.Error -> {
                    _uiState.value = OrderDetailUiState.Error(result.message ?: "Unable to load order details.")
                }
                is Resource.Loading -> {
                    _uiState.value = OrderDetailUiState.Loading
                }
            }
        }
    }
}

