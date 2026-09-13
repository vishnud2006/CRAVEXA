package com.cravexa.presentation.seller.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cravexa.core.common.Resource
import com.cravexa.domain.model.FssaiStatus
import com.cravexa.domain.model.Order
import com.cravexa.domain.model.SellerAccountStatus
import com.cravexa.domain.model.SellerDashboardStats
import com.cravexa.domain.model.SellerProfile
import com.cravexa.domain.usecase.GetSellerDashboardUseCase
import com.cravexa.domain.usecase.GetSellerOrdersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface SellerDashboardUiState {
    data object Loading : SellerDashboardUiState
    data class Success(
        val stats: SellerDashboardStats,
        val profile: SellerProfile?,
        val recentOrders: List<Order>
    ) : SellerDashboardUiState
    data class Error(val message: String) : SellerDashboardUiState
}

@HiltViewModel
class SellerDashboardViewModel @Inject constructor(
    private val getDashboardUseCase: GetSellerDashboardUseCase,
    private val getOrdersUseCase: GetSellerOrdersUseCase
) : ViewModel() {

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    val uiState: StateFlow<SellerDashboardUiState> = combine(
        getDashboardUseCase(),
        getDashboardUseCase.getProfile(),
        getOrdersUseCase.ordersFlow
    ) { statsResource, profile, orders ->
        when (statsResource) {
            is Resource.Loading -> SellerDashboardUiState.Loading
            is Resource.Error -> SellerDashboardUiState.Error(statsResource.message ?: "Failed to load dashboard metrics.")
            is Resource.Success -> {
                val stats = statsResource.data ?: SellerDashboardStats()
                val recentOrders = orders.take(3)
                SellerDashboardUiState.Success(
                    stats = stats,
                    profile = profile,
                    recentOrders = recentOrders
                )
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SellerDashboardUiState.Loading
    )

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            // Data flows reactively update
            kotlinx.coroutines.delay(400)
            _isRefreshing.value = false
        }
    }
}

