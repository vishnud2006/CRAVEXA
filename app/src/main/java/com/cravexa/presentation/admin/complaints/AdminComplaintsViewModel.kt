package com.cravexa.presentation.admin.complaints

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cravexa.core.common.Resource
import com.cravexa.domain.model.AdminComplaint
import com.cravexa.domain.model.ComplaintStatus
import com.cravexa.domain.usecase.GetAdminComplaintsUseCase
import com.cravexa.domain.usecase.UpdateComplaintStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface AdminComplaintsUiState {
    data object Loading : AdminComplaintsUiState
    data class Success(
        val complaints: List<AdminComplaint>,
        val filteredComplaints: List<AdminComplaint>,
        val selectedStatusFilter: ComplaintStatus?
    ) : AdminComplaintsUiState
    data class Error(val message: String) : AdminComplaintsUiState
}

@HiltViewModel
class AdminComplaintsViewModel @Inject constructor(
    private val getAdminComplaintsUseCase: GetAdminComplaintsUseCase,
    private val updateComplaintStatusUseCase: UpdateComplaintStatusUseCase
) : ViewModel() {

    private val _statusFilter = MutableStateFlow<ComplaintStatus?>(null)
    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    val uiState: StateFlow<AdminComplaintsUiState> = combine(
        getAdminComplaintsUseCase.complaintsFlow,
        _statusFilter
    ) { complaints, status ->
        val filtered = if (status == null) complaints else complaints.filter { it.status == status }
        AdminComplaintsUiState.Success(
            complaints = complaints,
            filteredComplaints = filtered,
            selectedStatusFilter = status
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AdminComplaintsUiState.Loading
    )

    init {
        getAdminComplaintsUseCase().launchIn(viewModelScope)
    }

    fun onStatusFilterSelected(status: ComplaintStatus?) {
        _statusFilter.value = status
    }

    fun updateComplaint(ticketId: String, newStatus: ComplaintStatus, notes: String) {
        viewModelScope.launch {
            val result = updateComplaintStatusUseCase(ticketId, newStatus, notes)
            if (result is Resource.Success) {
                _toastMessage.value = "Ticket status updated to ${newStatus.displayTitle}"
            } else {
                _toastMessage.value = result.message ?: "Failed to update ticket"
            }
        }
    }

    fun clearToast() {
        _toastMessage.value = null
    }
}
