package com.cravexa.presentation.admin.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cravexa.core.common.Resource
import com.cravexa.domain.model.AdminUser
import com.cravexa.domain.model.AdminUserStatus
import com.cravexa.domain.model.UserRole
import com.cravexa.domain.usecase.GetAdminUsersUseCase
import com.cravexa.domain.usecase.UpdateUserStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface AdminUsersUiState {
    data object Loading : AdminUsersUiState
    data class Success(
        val users: List<AdminUser>,
        val filteredUsers: List<AdminUser>,
        val searchQuery: String,
        val selectedRoleFilter: UserRole?,
        val selectedStatusFilter: AdminUserStatus?
    ) : AdminUsersUiState
    data class Error(val message: String) : AdminUsersUiState
}

@HiltViewModel
class AdminUsersViewModel @Inject constructor(
    private val getAdminUsersUseCase: GetAdminUsersUseCase,
    private val updateUserStatusUseCase: UpdateUserStatusUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _roleFilter = MutableStateFlow<UserRole?>(null)
    private val _statusFilter = MutableStateFlow<AdminUserStatus?>(null)
    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    val uiState: StateFlow<AdminUsersUiState> = combine(
        getAdminUsersUseCase.usersFlow,
        _searchQuery,
        _roleFilter,
        _statusFilter
    ) { users, query, role, status ->
        val filtered = users.filter { user ->
            val matchesQuery = query.isBlank() ||
                user.name.contains(query, ignoreCase = true) ||
                user.email.contains(query, ignoreCase = true) ||
                user.phone.contains(query, ignoreCase = true)
            val matchesRole = role == null || user.role == role
            val matchesStatus = status == null || user.status == status
            matchesQuery && matchesRole && matchesStatus
        }
        AdminUsersUiState.Success(
            users = users,
            filteredUsers = filtered,
            searchQuery = query,
            selectedRoleFilter = role,
            selectedStatusFilter = status
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AdminUsersUiState.Loading
    )

    init {
        getAdminUsersUseCase().launchIn(viewModelScope)
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onRoleFilterSelected(role: UserRole?) {
        _roleFilter.value = role
    }

    fun onStatusFilterSelected(status: AdminUserStatus?) {
        _statusFilter.value = status
    }

    fun toggleUserStatus(user: AdminUser) {
        viewModelScope.launch {
            val newStatus = if (user.status == AdminUserStatus.ACTIVE) AdminUserStatus.SUSPENDED else AdminUserStatus.ACTIVE
            val result = updateUserStatusUseCase(user.id, newStatus)
            if (result is Resource.Success) {
                _toastMessage.value = "User ${user.name} is now ${newStatus.displayTitle}"
            } else {
                _toastMessage.value = result.message ?: "Failed to update status"
            }
        }
    }

    fun clearToast() {
        _toastMessage.value = null
    }
}
