package com.cravexa

import com.cravexa.core.common.Resource
import com.cravexa.domain.model.CustomerProfile
import com.cravexa.domain.usecase.GetCustomerProfileUseCase
import com.cravexa.domain.usecase.LogoutUseCase
import com.cravexa.presentation.customer.profile.CustomerProfileViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CustomerProfileViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val getCustomerProfileUseCase = mockk<GetCustomerProfileUseCase>()
    private val logoutUseCase = mockk<LogoutUseCase>()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `profile observes customer profile from use case`() = runTest(testDispatcher) {
        val mockProfile = CustomerProfile(
            id = "cust_1",
            name = "Pooja Hegde",
            email = "pooja@cravexa.com",
            phone = "9876543210"
        )
        every { getCustomerProfileUseCase.observe() } returns flowOf(mockProfile)

        val viewModel = CustomerProfileViewModel(getCustomerProfileUseCase, logoutUseCase)
        backgroundScope.launch { viewModel.profile.collect() }
        advanceUntilIdle()

        assertEquals(mockProfile, viewModel.profile.value)
    }

    @Test
    fun `logout triggers logoutUseCase and invokes callback`() = runTest(testDispatcher) {
        every { getCustomerProfileUseCase.observe() } returns flowOf(null)
        coEvery { logoutUseCase() } returns Resource.Success(Unit)

        var loggedOut = false
        val viewModel = CustomerProfileViewModel(getCustomerProfileUseCase, logoutUseCase)
        viewModel.logout { loggedOut = true }
        advanceUntilIdle()

        coVerify(exactly = 1) { logoutUseCase() }
        assertEquals(true, loggedOut)
    }
}
