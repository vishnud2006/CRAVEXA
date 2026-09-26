package com.cravexa

import com.cravexa.core.common.Resource
import com.cravexa.domain.usecase.ForgotPasswordUseCase
import com.cravexa.presentation.auth.forgotpassword.ForgotPasswordUiState
import com.cravexa.presentation.auth.forgotpassword.ForgotPasswordViewModel
import io.mockk.coEvery
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
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ForgotPasswordViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val forgotPasswordUseCase = mockk<ForgotPasswordUseCase>()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `sendResetEmail with invalid email sets emailError`() = runTest(testDispatcher) {
        val viewModel = ForgotPasswordViewModel(forgotPasswordUseCase)
        viewModel.onEmailChanged("invalid-email")

        viewModel.sendResetEmail()

        assertNotNull(viewModel.emailError.value)
        assertEquals(ForgotPasswordUiState.Idle, viewModel.uiState.value)
    }

    @Test
    fun `sendResetEmail with valid email triggers usecase and sets Success state`() = runTest(testDispatcher) {
        coEvery { forgotPasswordUseCase("user@cravexa.com") } returns Resource.Success(Unit)

        val viewModel = ForgotPasswordViewModel(forgotPasswordUseCase)
        viewModel.onEmailChanged("user@cravexa.com")

        viewModel.sendResetEmail()
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value is ForgotPasswordUiState.Success)
    }
}

