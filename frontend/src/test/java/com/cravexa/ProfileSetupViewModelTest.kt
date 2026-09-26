package com.cravexa

import com.cravexa.core.common.Resource
import com.cravexa.domain.model.UserProfile
import com.cravexa.domain.model.UserRole
import com.cravexa.domain.usecase.GetUserProfileUseCase
import com.cravexa.domain.usecase.SaveUserProfileUseCase
import com.cravexa.presentation.auth.profile.ProfileSetupUiState
import com.cravexa.presentation.auth.profile.ProfileSetupViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
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
class ProfileSetupViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val getUserProfileUseCase = mockk<GetUserProfileUseCase>()
    private val saveUserProfileUseCase = mockk<SaveUserProfileUseCase>()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { getUserProfileUseCase.observe() } returns flowOf(null)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `saveProfile with invalid customer data sets validation errors`() = runTest(testDispatcher) {
        val viewModel = ProfileSetupViewModel(getUserProfileUseCase, saveUserProfileUseCase)
        advanceUntilIdle()

        viewModel.onNameChanged("")
        viewModel.onEmailChanged("bad-email")
        viewModel.onPhoneChanged("123")

        viewModel.saveProfile()

        assertNotNull(viewModel.nameError.value)
        assertNotNull(viewModel.emailError.value)
        assertNotNull(viewModel.phoneError.value)
    }

    @Test
    fun `saveProfile with valid customer data saves profile and updates to Success`() = runTest(testDispatcher) {
        val savedUser = UserProfile(
            id = "usr_100",
            name = "Meera Krishnan",
            email = "meera@cravexa.com",
            phone = "9876543210",
            role = UserRole.CUSTOMER,
            profileCompleted = true
        )
        coEvery { saveUserProfileUseCase(any()) } returns Resource.Success(savedUser)

        val viewModel = ProfileSetupViewModel(getUserProfileUseCase, saveUserProfileUseCase)
        advanceUntilIdle()

        viewModel.onNameChanged("Meera Krishnan")
        viewModel.onEmailChanged("meera@cravexa.com")
        viewModel.onPhoneChanged("9876543210")

        viewModel.saveProfile()
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value is ProfileSetupUiState.Success)
        val success = viewModel.uiState.value as ProfileSetupUiState.Success
        assertTrue(success.user.profileCompleted)
        assertEquals("Meera Krishnan", success.user.name)
    }
}

