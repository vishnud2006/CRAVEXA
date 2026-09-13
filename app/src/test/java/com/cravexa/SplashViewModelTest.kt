package com.cravexa

import com.cravexa.core.navigation.Screen
import com.cravexa.data.local.PreferenceManager
import com.cravexa.domain.model.AuthState
import com.cravexa.domain.model.UserProfile
import com.cravexa.domain.model.UserRole
import com.cravexa.domain.usecase.GetAuthStateUseCase
import com.cravexa.presentation.splash.SplashViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SplashViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val preferenceManager = mockk<PreferenceManager>(relaxed = true)
    private val getAuthStateUseCase = mockk<GetAuthStateUseCase>()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when onboarding is not completed, navigate to welcome screen`() = runTest(testDispatcher) {
        every { preferenceManager.isOnboardingCompleted } returns flowOf(false)
        every { getAuthStateUseCase() } returns flowOf(AuthState.Unauthenticated)

        val viewModel = SplashViewModel(preferenceManager, getAuthStateUseCase)

        advanceTimeBy(2500)

        assertEquals(Screen.Welcome, viewModel.targetScreen.value)
    }

    @Test
    fun `when onboarding completed but unauthenticated, navigate to login screen`() = runTest(testDispatcher) {
        every { preferenceManager.isOnboardingCompleted } returns flowOf(true)
        every { getAuthStateUseCase() } returns flowOf(AuthState.Unauthenticated)

        val viewModel = SplashViewModel(preferenceManager, getAuthStateUseCase)

        advanceTimeBy(2500)

        assertEquals(Screen.Login, viewModel.targetScreen.value)
    }

    @Test
    fun `when onboarding completed and authenticated as CUSTOMER, navigate to CustomerMain container`() = runTest(testDispatcher) {
        val mockUser = UserProfile(
            id = "usr_1",
            name = "Customer User",
            email = "customer@cravexa.com",
            role = UserRole.CUSTOMER,
            profileCompleted = true
        )
        every { preferenceManager.isOnboardingCompleted } returns flowOf(true)
        every { getAuthStateUseCase() } returns flowOf(AuthState.Authenticated(mockUser))

        val viewModel = SplashViewModel(preferenceManager, getAuthStateUseCase)

        advanceTimeBy(2500)

        assertEquals(Screen.CustomerMain, viewModel.targetScreen.value)
    }

    @Test
    fun `when onboarding completed and authenticated as SELLER, navigate to SellerHome`() = runTest(testDispatcher) {
        val mockUser = UserProfile(
            id = "usr_2",
            name = "Seller User",
            email = "seller@cravexa.com",
            role = UserRole.SELLER,
            profileCompleted = true
        )
        every { preferenceManager.isOnboardingCompleted } returns flowOf(true)
        every { getAuthStateUseCase() } returns flowOf(AuthState.Authenticated(mockUser))

        val viewModel = SplashViewModel(preferenceManager, getAuthStateUseCase)

        advanceTimeBy(2500)

        assertEquals(Screen.SellerHome, viewModel.targetScreen.value)
    }

    @Test
    fun `when onboarding completed and authenticated as ADMIN, navigate to AdminHome`() = runTest(testDispatcher) {
        val mockUser = UserProfile(
            id = "usr_3",
            name = "Admin User",
            email = "admin@cravexa.com",
            role = UserRole.ADMIN,
            profileCompleted = true
        )
        every { preferenceManager.isOnboardingCompleted } returns flowOf(true)
        every { getAuthStateUseCase() } returns flowOf(AuthState.Authenticated(mockUser))

        val viewModel = SplashViewModel(preferenceManager, getAuthStateUseCase)

        advanceTimeBy(2500)

        assertEquals(Screen.AdminMain, viewModel.targetScreen.value)
    }

    @Test
    fun `when onboarding completed but needs profile setup, navigate to profile setup screen`() = runTest(testDispatcher) {
        every { preferenceManager.isOnboardingCompleted } returns flowOf(true)
        every { getAuthStateUseCase() } returns flowOf(AuthState.NeedsProfileSetup("uid_123"))

        val viewModel = SplashViewModel(preferenceManager, getAuthStateUseCase)

        advanceTimeBy(2500)

        assertEquals(Screen.ProfileSetup, viewModel.targetScreen.value)
    }
}
