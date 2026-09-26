package com.cravexa.presentation.admin.payments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cravexa.core.common.Resource
import com.cravexa.domain.model.AdminPayment
import com.cravexa.domain.model.AdminRefund
import com.cravexa.domain.model.AdminRefundStatus
import com.cravexa.domain.usecase.GetAdminPaymentsUseCase
import com.cravexa.domain.usecase.GetAdminRefundsUseCase
import com.cravexa.domain.usecase.UpdateRefundStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface AdminPaymentsUiState {
    data object Loading : AdminPaymentsUiState
    data class Success(
        val payments: List<AdminPayment>,
        val refunds: List<AdminRefund>,
        val selectedSubTab: Int // 0 for Payments, 1 for Refunds
    ) : AdminPaymentsUiState
    data class Error(val message: String) : AdminPaymentsUiState
}

@HiltViewModel
class AdminPaymentsViewModel @Inject constructor(
    private val getAdminPaymentsUseCase: GetAdminPaymentsUseCase,
    private val getAdminRefundsUseCase: GetAdminRefundsUseCase,
    private val updateRefundStatusUseCase: UpdateRefundStatusUseCase
) : ViewModel() {

    private val _selectedSubTab = MutableStateFlow(0)
    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    val uiState: StateFlow<AdminPaymentsUiState> = combine(
        getAdminPaymentsUseCase.paymentsFlow,
        getAdminRefundsUseCase.refundsFlow,
        _selectedSubTab
    ) { payments, refunds, tab ->
        AdminPaymentsUiState.Success(
            payments = payments,
            refunds = refunds,
            selectedSubTab = tab
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AdminPaymentsUiState.Loading
    )

    init {
        getAdminPaymentsUseCase().launchIn(viewModelScope)
        getAdminRefundsUseCase().launchIn(viewModelScope)
    }

    fun onSubTabSelected(tab: Int) {
        _selectedSubTab.value = tab
    }

    fun approveRefund(refundId: String) {
        viewModelScope.launch {
            val result = updateRefundStatusUseCase(refundId, AdminRefundStatus.APPROVED)
            if (result is Resource.Success) {
                _toastMessage.value = "Refund approved! Processing queue initiated."
            } else {
                _toastMessage.value = result.message ?: "Failed to approve refund"
            }
        }
    }

    fun rejectRefund(refundId: String) {
        viewModelScope.launch {
            val result = updateRefundStatusUseCase(refundId, AdminRefundStatus.REJECTED)
            if (result is Resource.Success) {
                _toastMessage.value = "Refund request rejected."
            } else {
                _toastMessage.value = result.message ?: "Failed to reject refund"
            }
        }
    }

    fun clearToast() {
        _toastMessage.value = null
    }
}
