package com.cravexa.presentation.customer.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cravexa.domain.model.CustomerProfile
import com.cravexa.domain.usecase.GetCustomerProfileUseCase
import com.cravexa.domain.usecase.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomerProfileViewModel @Inject constructor(
    getCustomerProfileUseCase: GetCustomerProfileUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    val profile: StateFlow<CustomerProfile?> = getCustomerProfileUseCase.observe()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun logout(onLogoutComplete: () -> Unit) {
        viewModelScope.launch {
            logoutUseCase()
            onLogoutComplete()
        }
    }
}

