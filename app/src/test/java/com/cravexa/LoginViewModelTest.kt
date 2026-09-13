package com.cravexa

import com.cravexa.core.common.Resource
import com.cravexa.domain.model.UserProfile
import com.cravexa.domain.model.UserRole
import com.cravexa.domain.usecase.GoogleSignInUseCase
import com.cravexa.domain.usecase.LoginUseCase
import com.cravexa.presentation.auth.login.LoginUiState
import com.cravexa.presentation.auth.login.LoginViewModel
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
class LoginViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val loginUseCase = mockk<LoginUseCase>()
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
    fun `login with invalid email sets emailError and does not call usecase`() = runTest(testDispatcher) {
        val viewModel = LoginViewModel(loginUseCase, googleSignInUseCase)
        viewModel.onEmailChanged("invalid-email")
        viewModel.onPasswordChanged("ValidPassword123")

        viewModel.login()

        assertNotNull(viewModel.emailError.value)
        assertEquals(LoginUiState.Idle, viewModel.uiState.value)
    }

    @Test
    fun `login with empty password sets passwordError and does not call usecase`() = runTest(testDispatcher) {
        val viewModel = LoginViewModel(loginUseCase, googleSignInUseCase)
        viewModel.onEmailChanged("valid@cravexa.com")
        viewModel.onPasswordChanged("")

        viewModel.login()

        assertNotNull(viewModel.passwordError.value)
        assertEquals(LoginUiState.Idle, viewModel.uiState.value)
    }

    @Test
    fun `login with valid credentials and successful response updates uiState to Success`() = runTest(testDispatcher) {
        val mockUser = UserProfile(
            id = "usr_123",
            email = "valid@cravexa.com",
            name = "Test User",
            role = UserRole.CUSTOMER,
            profileCompleted = true
        )
        coEvery { loginUseCase("valid@cravexa.com", "Password123") } returns Resource.Success(mockUser)

        val viewModel = LoginViewModel(loginUseCase, googleSignInUseCase)
        viewModel.onEmailChanged("valid@cravexa.com")
        viewModel.onPasswordChanged("Password123")

        viewModel.login()
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value is LoginUiState.Success)
        val successState = viewModel.uiState.value as LoginUiState.Success
        assertTrue(successState.profileCompleted)
    }

    @Test
    fun `login with error response updates uiState to Error`() = runTest(testDispatcher) {
        coEvery { loginUseCase(any(), any()) } returns Resource.Error("Incorrect email or password.")

        val viewModel = LoginViewModel(loginUseCase, googleSignInUseCase)
        viewModel.onEmailChanged("wrong@cravexa.com")
        viewModel.onPasswordChanged("WrongPassword123")

        viewModel.login()
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value is LoginUiState.Error)
        val errorState = viewModel.uiState.value as LoginUiState.Error
        assertEquals("Incorrect email or password.", errorState.message)
    }
}

