package com.cravexa

import com.cravexa.data.local.PreferenceManager
import com.cravexa.presentation.onboarding.OnboardingViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class OnboardingViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val preferenceManager = mockk<PreferenceManager>(relaxed = true)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onboarding pages list contains 4 valid pages`() {
        val viewModel = OnboardingViewModel(preferenceManager)
        assertEquals(4, viewModel.pages.size)
        assertEquals(1, viewModel.pages[0].id)
        assertEquals(2, viewModel.pages[1].id)
        assertEquals(3, viewModel.pages[2].id)
        assertEquals(4, viewModel.pages[3].id)
    }

    @Test
    fun `completeOnboarding marks preference and invokes callback`() = runTest(testDispatcher) {
        coEvery { preferenceManager.setOnboardingCompleted(true) } returns Unit

        val viewModel = OnboardingViewModel(preferenceManager)
        var callbackInvoked = false

        viewModel.completeOnboarding {
            callbackInvoked = true
        }

        advanceUntilIdle()

        coVerify(exactly = 1) { preferenceManager.setOnboardingCompleted(true) }
        assertTrue(viewModel.isCompleted.value)
        assertTrue(callbackInvoked)
    }
}

