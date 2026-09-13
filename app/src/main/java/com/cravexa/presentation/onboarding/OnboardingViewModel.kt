package com.cravexa.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cravexa.data.local.PreferenceManager
import com.cravexa.domain.model.OnboardingItem
import com.cravexa.domain.model.OnboardingPages
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    val pages: List<OnboardingItem> = OnboardingPages.items

    private val _isCompleted = MutableStateFlow(false)
    val isCompleted: StateFlow<Boolean> = _isCompleted.asStateFlow()

    fun completeOnboarding(onFinish: () -> Unit) {
        viewModelScope.launch {
            preferenceManager.setOnboardingCompleted(true)
            _isCompleted.value = true
            onFinish()
        }
    }
}

