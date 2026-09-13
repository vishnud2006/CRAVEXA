package com.cravexa.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cravexa.core.constants.AppConstants
import com.cravexa.core.navigation.Screen
import com.cravexa.data.local.PreferenceManager
import com.cravexa.domain.model.AuthState
import com.cravexa.domain.model.UserRole
import com.cravexa.domain.usecase.GetAuthStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val preferenceManager: PreferenceManager,
    private val getAuthStateUseCase: GetAuthStateUseCase
) : ViewModel() {

    private val _targetScreen = MutableStateFlow<Screen?>(null)
    val targetScreen: StateFlow<Screen?> = _targetScreen.asStateFlow()

    init {
        determineStartDestination()
    }

    private fun determineStartDestination() {
        viewModelScope.launch {
            val isOnboardingCompleted = preferenceManager.isOnboardingCompleted.first()
            val currentAuthState = getAuthStateUseCase().first()

            delay(AppConstants.SPLASH_DELAY_MS)

            if (!isOnboardingCompleted) {
                _targetScreen.value = Screen.Welcome
            } else {
                when (currentAuthState) {
                    is AuthState.Authenticated -> {
                        _targetScreen.value = when (currentAuthState.user.role) {
                            UserRole.SELLER -> Screen.SellerHome
                            UserRole.ADMIN -> Screen.AdminMain
                            UserRole.CUSTOMER -> Screen.CustomerMain
                        }
                    }
                    is AuthState.NeedsProfileSetup -> {
                        _targetScreen.value = Screen.ProfileSetup
                    }
                    is AuthState.Unauthenticated, is AuthState.AuthenticationError, is AuthState.Authenticating -> {
                        _targetScreen.value = Screen.Login
                    }
                }
            }
        }
    }
}
