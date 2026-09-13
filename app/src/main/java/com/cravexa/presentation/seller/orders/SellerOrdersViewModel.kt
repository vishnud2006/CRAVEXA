package com.cravexa.presentation.seller.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cravexa.core.common.Resource
import com.cravexa.domain.model.Order
import com.cravexa.domain.model.OrderStatus
import com.cravexa.domain.usecase.GetSellerOrdersUseCase
import com.cravexa.domain.usecase.UpdateSellerOrderStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class SellerOrderFilter(val title: String) {
    ALL("All Orders"),
    PENDING("New / Pending"),
    PREPARING("In Kitchen"),
    READY_FOR_PICKUP("Ready for Courier"),
    COMPLETED("Delivered"),
    CANCELLED("Cancelled")
}

sealed interface SellerOrdersUiState {
    data object Loading : SellerOrdersUiState
    data class Success(
        val orders: List<Order>,
        val filteredOrders: List<Order>,
        val activeFilter: SellerOrderFilter
    ) : SellerOrdersUiState
    data class Error(val message: String) : SellerOrdersUiState
}

@HiltViewModel
class SellerOrdersViewModel @Inject constructor(
    private val getSellerOrdersUseCase: GetSellerOrdersUseCase,
    private val updateSellerOrderStatusUseCase: UpdateSellerOrderStatusUseCase
) : ViewModel() {

    private val _activeFilter = MutableStateFlow(SellerOrderFilter.ALL)
    val activeFilter: StateFlow<SellerOrderFilter> = _activeFilter.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    val uiState: StateFlow<SellerOrdersUiState> = combine(
        getSellerOrdersUseCase.ordersFlow,
        _activeFilter
    ) { orders, filter ->
        val filtered = orders.filter { order ->
            when (filter) {
                SellerOrderFilter.ALL -> true
                SellerOrderFilter.PENDING -> order.orderStatus == OrderStatus.ORDER_PLACED ||
                        order.orderStatus == OrderStatus.PAYMENT_CONFIRMED
                SellerOrderFilter.PREPARING -> order.orderStatus == OrderStatus.PREPARING
                SellerOrderFilter.READY_FOR_PICKUP -> order.orderStatus == OrderStatus.READY_FOR_PICKUP
                SellerOrderFilter.COMPLETED -> order.orderStatus == OrderStatus.DELIVERED
                SellerOrderFilter.CANCELLED -> order.orderStatus == OrderStatus.CANCELLED ||
                        order.orderStatus == OrderStatus.REFUNDED
            }
        }

        SellerOrdersUiState.Success(
            orders = orders,
            filteredOrders = filtered,
            activeFilter = filter
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SellerOrdersUiState.Loading
    )

    fun onFilterSelected(filter: SellerOrderFilter) {
        _activeFilter.value = filter
    }

    fun updateStatus(orderId: String, newStatus: OrderStatus) {
        viewModelScope.launch {
            when (val result = updateSellerOrderStatusUseCase(orderId, newStatus)) {
                is Resource.Success -> {
                    _toastMessage.value = "Order status updated to ${newStatus.displayTitle}."
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

