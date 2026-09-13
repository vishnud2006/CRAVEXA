package com.cravexa.presentation.admin.sellers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cravexa.core.common.Resource
import com.cravexa.domain.model.AdminSeller
import com.cravexa.domain.model.SellerAccountStatus
import com.cravexa.domain.usecase.ApproveSellerUseCase
import com.cravexa.domain.usecase.GetAdminSellersUseCase
import com.cravexa.domain.usecase.RejectSellerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface AdminSellersUiState {
    data object Loading : AdminSellersUiState
    data class Success(
        val sellers: List<AdminSeller>,
        val filteredSellers: List<AdminSeller>,
        val selectedStatus: SellerAccountStatus?
    ) : AdminSellersUiState
    data class Error(val message: String) : AdminSellersUiState
}

@HiltViewModel
class AdminSellersViewModel @Inject constructor(
    private val getAdminSellersUseCase: GetAdminSellersUseCase,
    private val approveSellerUseCase: ApproveSellerUseCase,
    private val rejectSellerUseCase: RejectSellerUseCase
) : ViewModel() {

    private val _statusFilter = MutableStateFlow<SellerAccountStatus?>(null)
    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    val uiState: StateFlow<AdminSellersUiState> = combine(
        getAdminSellersUseCase.sellersFlow,
        _statusFilter
    ) { sellers, status ->
        val filtered = if (status == null) sellers else sellers.filter { it.status == status }
        AdminSellersUiState.Success(
            sellers = sellers,
            filteredSellers = filtered,
            selectedStatus = status
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AdminSellersUiState.Loading
    )

    init {
        getAdminSellersUseCase().launchIn(viewModelScope)
    }

    fun onStatusFilterSelected(status: SellerAccountStatus?) {
        _statusFilter.value = status
    }

    fun approveSeller(sellerId: String) {
        viewModelScope.launch {
            val result = approveSellerUseCase(sellerId)
            if (result is Resource.Success) {
                _toastMessage.value = "Food Creator approved successfully!"
            } else {
                _toastMessage.value = result.message ?: "Failed to approve seller"
            }
        }
    }

    fun rejectSeller(sellerId: String) {
        viewModelScope.launch {
            val result = rejectSellerUseCase(sellerId)
            if (result is Resource.Success) {
                _toastMessage.value = "Seller application rejected."
            } else {
                _toastMessage.value = result.message ?: "Failed to reject seller"
            }
        }
    }

    fun clearToast() {
        _toastMessage.value = null
    }
}
