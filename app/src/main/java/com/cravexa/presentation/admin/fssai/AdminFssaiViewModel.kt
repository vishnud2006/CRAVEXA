package com.cravexa.presentation.admin.fssai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cravexa.core.common.Resource
import com.cravexa.domain.model.AdminSeller
import com.cravexa.domain.model.FssaiStatus
import com.cravexa.domain.usecase.GetAdminSellersUseCase
import com.cravexa.domain.usecase.RejectFssaiUseCase
import com.cravexa.domain.usecase.VerifyFssaiUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface AdminFssaiUiState {
    data object Loading : AdminFssaiUiState
    data class Success(
        val sellers: List<AdminSeller>,
        val filteredSellers: List<AdminSeller>,
        val selectedFssaiStatus: FssaiStatus?
    ) : AdminFssaiUiState
    data class Error(val message: String) : AdminFssaiUiState
}

@HiltViewModel
class AdminFssaiViewModel @Inject constructor(
    private val getAdminSellersUseCase: GetAdminSellersUseCase,
    private val verifyFssaiUseCase: VerifyFssaiUseCase,
    private val rejectFssaiUseCase: RejectFssaiUseCase
) : ViewModel() {

    private val _statusFilter = MutableStateFlow<FssaiStatus?>(null)
    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    val uiState: StateFlow<AdminFssaiUiState> = combine(
        getAdminSellersUseCase.sellersFlow,
        _statusFilter
    ) { sellers, filter ->
        val filtered = if (filter == null) {
            sellers.filter { !it.fssaiNumber.isNullOrBlank() }
        } else {
            sellers.filter { it.fssaiStatus == filter }
        }
        AdminFssaiUiState.Success(
            sellers = sellers,
            filteredSellers = filtered,
            selectedFssaiStatus = filter
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AdminFssaiUiState.Loading
    )

    init {
        getAdminSellersUseCase().launchIn(viewModelScope)
    }

    fun onStatusFilterSelected(status: FssaiStatus?) {
        _statusFilter.value = status
    }

    fun verifyFssai(sellerId: String) {
        viewModelScope.launch {
            val result = verifyFssaiUseCase(sellerId)
            if (result is Resource.Success) {
                _toastMessage.value = "FSSAI certification verified and approved!"
            } else {
                _toastMessage.value = result.message ?: "Failed to verify FSSAI"
            }
        }
    }

    fun rejectFssai(sellerId: String) {
        viewModelScope.launch {
            val result = rejectFssaiUseCase(sellerId)
            if (result is Resource.Success) {
                _toastMessage.value = "FSSAI certification rejected."
            } else {
                _toastMessage.value = result.message ?: "Failed to reject FSSAI"
            }
        }
    }

    fun clearToast() {
        _toastMessage.value = null
    }
}
