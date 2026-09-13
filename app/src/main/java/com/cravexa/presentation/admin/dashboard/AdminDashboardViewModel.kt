package com.cravexa.presentation.admin.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cravexa.core.common.Resource
import com.cravexa.domain.model.AdminDashboardStats
import com.cravexa.domain.usecase.GetAdminDashboardStatsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

sealed interface AdminDashboardUiState {
    data object Loading : AdminDashboardUiState
    data class Success(val stats: AdminDashboardStats) : AdminDashboardUiState
    data class Error(val message: String) : AdminDashboardUiState
}

@HiltViewModel
class AdminDashboardViewModel @Inject constructor(
    private val getAdminDashboardStatsUseCase: GetAdminDashboardStatsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<AdminDashboardUiState>(AdminDashboardUiState.Loading)
    val uiState: StateFlow<AdminDashboardUiState> = _uiState.asStateFlow()

    init {
        loadDashboardStats()
    }

    fun loadDashboardStats() {
        getAdminDashboardStatsUseCase().onEach { result ->
            _uiState.value = when (result) {
                is Resource.Loading -> AdminDashboardUiState.Loading
                is Resource.Success -> AdminDashboardUiState.Success(result.data ?: AdminDashboardStats())
                is Resource.Error -> AdminDashboardUiState.Error(result.message ?: "Failed to load admin stats")
            }
        }.launchIn(viewModelScope)
    }
}
