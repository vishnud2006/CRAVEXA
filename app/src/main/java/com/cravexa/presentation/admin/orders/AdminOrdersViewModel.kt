package com.cravexa.presentation.admin.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cravexa.domain.model.AdminOrder
import com.cravexa.domain.model.OrderStatus
import com.cravexa.domain.usecase.GetAdminOrdersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

sealed interface AdminOrdersUiState {
    data object Loading : AdminOrdersUiState
    data class Success(
        val orders: List<AdminOrder>,
        val filteredOrders: List<AdminOrder>,
        val selectedStatusFilter: OrderStatus?
    ) : AdminOrdersUiState
    data class Error(val message: String) : AdminOrdersUiState
}

@HiltViewModel
class AdminOrdersViewModel @Inject constructor(
    private val getAdminOrdersUseCase: GetAdminOrdersUseCase
) : ViewModel() {

    private val _statusFilter = MutableStateFlow<OrderStatus?>(null)

    val uiState: StateFlow<AdminOrdersUiState> = combine(
        getAdminOrdersUseCase.ordersFlow,
        _statusFilter
    ) { orders, status ->
        val filtered = if (status == null) orders else orders.filter { it.orderStatus == status }
        AdminOrdersUiState.Success(
            orders = orders,
            filteredOrders = filtered,
            selectedStatusFilter = status
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AdminOrdersUiState.Loading
    )

    init {
        getAdminOrdersUseCase().launchIn(viewModelScope)
    }

    fun onStatusFilterSelected(status: OrderStatus?) {
        _statusFilter.value = status
    }
}
