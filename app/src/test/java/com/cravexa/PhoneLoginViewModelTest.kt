package com.cravexa

import com.cravexa.core.common.Resource
import com.cravexa.domain.model.UserProfile
import com.cravexa.domain.model.UserRole
import com.cravexa.domain.usecase.SendPhoneOtpUseCase
import com.cravexa.domain.usecase.VerifyPhoneOtpUseCase
import com.cravexa.presentation.auth.phone.PhoneLoginUiState
import com.cravexa.presentation.auth.phone.PhoneLoginViewModel
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
class PhoneLoginViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val sendPhoneOtpUseCase = mockk<SendPhoneOtpUseCase>()
    private val verifyPhoneOtpUseCase = mockk<VerifyPhoneOtpUseCase>()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `sendOtp with invalid phone number sets phoneError`() = runTest(testDispatcher) {
        val viewModel = PhoneLoginViewModel(sendPhoneOtpUseCase, verifyPhoneOtpUseCase)
        viewModel.onPhoneChanged("123")

        viewModel.sendOtp()

        assertNotNull(viewModel.phoneError.value)
        assertEquals(PhoneLoginUiState.EnterPhone, viewModel.uiState.value)
    }

    @Test
    fun `sendOtp with valid phone sets OtpSent state and starts countdown`() = runTest(testDispatcher) {
        coEvery { sendPhoneOtpUseCase("9876543210", any()) } answers {
            val callback = secondArg<(String) -> Unit>()
            callback.invoke("ver_12345")
            Resource.Success(Unit)
        }

        val viewModel = PhoneLoginViewModel(sendPhoneOtpUseCase, verifyPhoneOtpUseCase)
        viewModel.onPhoneChanged("9876543210")

        viewModel.sendOtp()
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value is PhoneLoginUiState.OtpSent)
        val state = viewModel.uiState.value as PhoneLoginUiState.OtpSent
        assertEquals("ver_12345", state.verificationId)
    }

    @Test
    fun `verifyOtp with valid 6 digit OTP updates state to Success`() = runTest(testDispatcher) {
        val mockUser = UserProfile(
            id = "usr_789",
            phone = "9876543210",
            role = UserRole.CUSTOMER,
            profileCompleted = false
        )
        coEvery { verifyPhoneOtpUseCase(any(), "123456", any()) } returns Resource.Success(mockUser)

        val viewModel = PhoneLoginViewModel(sendPhoneOtpUseCase, verifyPhoneOtpUseCase)
        viewModel.onPhoneChanged("9876543210")
        viewModel.onOtpChanged("123456")

        viewModel.verifyOtp()
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value is PhoneLoginUiState.Success)
    }
}

