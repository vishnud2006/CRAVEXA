package com.cravexa.presentation.customer.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cravexa.core.common.Resource
import com.cravexa.domain.model.OrderTracking
import com.cravexa.domain.usecase.GetOrderTrackingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface OrderTrackingUiState {
    data object Loading : OrderTrackingUiState
    data class Success(val tracking: OrderTracking) : OrderTrackingUiState
    data class Error(val message: String) : OrderTrackingUiState
}

@HiltViewModel
class OrderTrackingViewModel @Inject constructor(
    private val getOrderTrackingUseCase: GetOrderTrackingUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<OrderTrackingUiState>(OrderTrackingUiState.Loading)
    val uiState: StateFlow<OrderTrackingUiState> = _uiState.asStateFlow()

    fun loadTracking(orderId: String) {
        viewModelScope.launch {
            _uiState.value = OrderTrackingUiState.Loading
            when (val result = getOrderTrackingUseCase(orderId)) {
                is Resource.Success -> {
                    result.data?.let { tracking ->
                        _uiState.value = OrderTrackingUiState.Success(tracking)
                    } ?: run {
                        _uiState.value = OrderTrackingUiState.Error("Tracking information unavailable.")
                    }
                }
                is Resource.Error -> {
                    _uiState.value = OrderTrackingUiState.Error(result.message ?: "Unable to load tracking details.")
                }
                is Resource.Loading -> {
                    _uiState.value = OrderTrackingUiState.Loading
                }
            }
        }
    }
}

