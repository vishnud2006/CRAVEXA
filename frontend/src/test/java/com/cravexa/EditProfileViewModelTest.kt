package com.cravexa

import com.cravexa.core.common.Resource
import com.cravexa.domain.model.CustomerProfile
import com.cravexa.domain.usecase.GetCustomerProfileUseCase
import com.cravexa.domain.usecase.UpdateCustomerProfileUseCase
import com.cravexa.presentation.customer.profile.EditProfileUiState
import com.cravexa.presentation.customer.profile.EditProfileViewModel
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
class EditProfileViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val getCustomerProfileUseCase = mockk<GetCustomerProfileUseCase>()
    private val updateCustomerProfileUseCase = mockk<UpdateCustomerProfileUseCase>()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { getCustomerProfileUseCase.observe() } returns flowOf(null)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `saveProfile with invalid name sets nameError`() = runTest(testDispatcher) {
        val viewModel = EditProfileViewModel(getCustomerProfileUseCase, updateCustomerProfileUseCase)
        advanceUntilIdle()

        viewModel.onNameChanged("")
        viewModel.onPhoneChanged("9876543210")
        viewModel.saveProfile()

        assertNotNull(viewModel.nameError.value)
        assertEquals(EditProfileUiState.Idle, viewModel.uiState.value)
    }

    @Test
    fun `saveProfile with invalid phone sets phoneError`() = runTest(testDispatcher) {
        val viewModel = EditProfileViewModel(getCustomerProfileUseCase, updateCustomerProfileUseCase)
        advanceUntilIdle()

        viewModel.onNameChanged("Vikram")
        viewModel.onPhoneChanged("1234")
        viewModel.saveProfile()

        assertNotNull(viewModel.phoneError.value)
        assertEquals(EditProfileUiState.Idle, viewModel.uiState.value)
    }

    @Test
    fun `saveProfile with valid data triggers use case and updates state to Success`() = runTest(testDispatcher) {
        val updated = CustomerProfile(
            id = "cust_2",
            name = "Vikram Aditya",
            email = "vikram@cravexa.com",
            phone = "9876543210"
        )
        coEvery { updateCustomerProfileUseCase(any()) } returns Resource.Success(updated)

        val viewModel = EditProfileViewModel(getCustomerProfileUseCase, updateCustomerProfileUseCase)
        advanceUntilIdle()

        viewModel.onNameChanged("Vikram Aditya")
        viewModel.onPhoneChanged("9876543210")
        viewModel.saveProfile()
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value is EditProfileUiState.Success)
        val successState = viewModel.uiState.value as EditProfileUiState.Success
        assertEquals("Vikram Aditya", successState.profile.name)
    }
}

