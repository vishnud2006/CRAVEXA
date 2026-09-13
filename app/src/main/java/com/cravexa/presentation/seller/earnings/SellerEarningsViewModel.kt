package com.cravexa.presentation.seller.earnings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cravexa.core.common.Resource
import com.cravexa.domain.model.SellerDashboardStats
import com.cravexa.domain.model.SellerPayout
import com.cravexa.domain.usecase.GetSellerEarningsUseCase
import com.cravexa.domain.usecase.GetSellerPayoutsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface SellerEarningsUiState {
    data object Loading : SellerEarningsUiState
    data class Success(
        val stats: SellerDashboardStats,
        val payouts: List<SellerPayout>
    ) : SellerEarningsUiState
    data class Error(val message: String) : SellerEarningsUiState
}

@HiltViewModel
class SellerEarningsViewModel @Inject constructor(
    private val getSellerEarningsUseCase: GetSellerEarningsUseCase,
    private val getSellerPayoutsUseCase: GetSellerPayoutsUseCase
) : ViewModel() {

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    val uiState: StateFlow<SellerEarningsUiState> = combine(
        getSellerEarningsUseCase(),
        getSellerPayoutsUseCase()
    ) { statsRes, payoutsRes ->
        val stats = (statsRes as? Resource.Success)?.data ?: SellerDashboardStats()
        val payouts = (payoutsRes as? Resource.Success)?.data ?: emptyList()

        SellerEarningsUiState.Success(
            stats = stats,
            payouts = payouts
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SellerEarningsUiState.Loading
    )

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            kotlinx.coroutines.delay(400)
            _isRefreshing.value = false
        }
    }
}

