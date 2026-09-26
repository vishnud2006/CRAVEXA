package com.cravexa

import com.cravexa.core.common.Resource
import com.cravexa.domain.model.UserProfile
import com.cravexa.domain.model.UserRole
import com.cravexa.domain.usecase.GoogleSignInUseCase
import com.cravexa.domain.usecase.SignupUseCase
import com.cravexa.presentation.auth.signup.SignupUiState
import com.cravexa.presentation.auth.signup.SignupViewModel
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
class SignupViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val signupUseCase = mockk<SignupUseCase>()
    private val googleSignInUseCase = mockk<GoogleSignInUseCase>()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onRoleSelected ignores ADMIN and defaults to CUSTOMER`() {
        val viewModel = SignupViewModel(signupUseCase, googleSignInUseCase)
        viewModel.onRoleSelected(UserRole.ADMIN)

        assertEquals(UserRole.CUSTOMER, viewModel.selectedRole.value)
    }

    @Test
    fun `onRoleSelected selects SELLER correctly`() {
        val viewModel = SignupViewModel(signupUseCase, googleSignInUseCase)
        viewModel.onRoleSelected(UserRole.SELLER)

        assertEquals(UserRole.SELLER, viewModel.selectedRole.value)
    }

    @Test
    fun `signup with password mismatch sets confirmPasswordError`() = runTest(testDispatcher) {
        val viewModel = SignupViewModel(signupUseCase, googleSignInUseCase)
        viewModel.onNameChanged("Ananya")
        viewModel.onEmailChanged("ananya@cravexa.com")
        viewModel.onPasswordChanged("Password123")
        viewModel.onConfirmPasswordChanged("DifferentPassword123")

        viewModel.signup()

        assertNotNull(viewModel.confirmPasswordError.value)
        assertEquals(SignupUiState.Idle, viewModel.uiState.value)
    }

    @Test
    fun `signup with valid details triggers usecase and updates state to Success`() = runTest(testDispatcher) {
        val mockUser = UserProfile(
            id = "usr_456",
            email = "creator@cravexa.com",
            name = "Food Creator",
            role = UserRole.SELLER,
            profileCompleted = false
        )
        coEvery {
            signupUseCase("Food Creator", "creator@cravexa.com", "Password123", "", UserRole.SELLER)
        } returns Resource.Success(mockUser)

        val viewModel = SignupViewModel(signupUseCase, googleSignInUseCase)
        viewModel.onNameChanged("Food Creator")
        viewModel.onEmailChanged("creator@cravexa.com")
        viewModel.onPasswordChanged("Password123")
        viewModel.onConfirmPasswordChanged("Password123")
        viewModel.onRoleSelected(UserRole.SELLER)

        viewModel.signup()
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value is SignupUiState.Success)
        val success = viewModel.uiState.value as SignupUiState.Success
        assertEquals(UserRole.SELLER, success.user.role)
    }
}

