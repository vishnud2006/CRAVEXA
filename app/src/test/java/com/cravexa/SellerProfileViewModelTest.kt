package com.cravexa

import com.cravexa.core.common.Resource
import com.cravexa.domain.model.FssaiStatus
import com.cravexa.domain.model.SellerProfile
import com.cravexa.domain.usecase.GetSellerProfileUseCase
import com.cravexa.domain.usecase.LogoutUseCase
import com.cravexa.domain.usecase.SubmitFssaiUseCase
import com.cravexa.domain.usecase.UpdateSellerProfileUseCase
import com.cravexa.presentation.seller.profile.SellerProfileUiState
import com.cravexa.presentation.seller.profile.SellerProfileViewModel
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
class SellerProfileViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val getSellerProfileUseCase = mockk<GetSellerProfileUseCase>()
    private val updateSellerProfileUseCase = mockk<UpdateSellerProfileUseCase>()
    private val submitFssaiUseCase = mockk<SubmitFssaiUseCase>()
    private val logoutUseCase = mockk<LogoutUseCase>()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { getSellerProfileUseCase.observe() } returns flowOf(null)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `submitFssai with invalid 10 digit number sets fssaiError and does not call use case`() = runTest(testDispatcher) {
        val viewModel = SellerProfileViewModel(
            getSellerProfileUseCase,
            updateSellerProfileUseCase,
            submitFssaiUseCase,
            logoutUseCase
        )
        advanceUntilIdle()

        viewModel.onFssaiNumberChanged("1234567890")
        viewModel.submitFssai()

        assertNotNull(viewModel.fssaiError.value)
        assertEquals(SellerProfileUiState.Idle, viewModel.uiState.value)
    }

    @Test
    fun `submitFssai with valid 14 digit number submits and sets Success state`() = runTest(testDispatcher) {
        val updatedSeller = SellerProfile(
            id = "sel_1",
            fssaiNumber = "11223344556677",
            fssaiStatus = FssaiStatus.PENDING,
            isFssaiSubmitted = true
        )
        coEvery { submitFssaiUseCase("11223344556677", null) } returns Resource.Success(updatedSeller)

        val viewModel = SellerProfileViewModel(
            getSellerProfileUseCase,
            updateSellerProfileUseCase,
            submitFssaiUseCase,
            logoutUseCase
        )
        advanceUntilIdle()

        viewModel.onFssaiNumberChanged("11223344556677")
        viewModel.submitFssai()
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value is SellerProfileUiState.Success)
    }

    @Test
    fun `saveBusinessDetails with empty name sets businessNameError`() = runTest(testDispatcher) {
        val viewModel = SellerProfileViewModel(
            getSellerProfileUseCase,
            updateSellerProfileUseCase,
            submitFssaiUseCase,
            logoutUseCase
        )
        advanceUntilIdle()

        viewModel.onBusinessNameChanged("")
        viewModel.onAddressChanged("12th Main Road")
        viewModel.onPincodeChanged("560038")
        viewModel.saveBusinessDetails()

        assertNotNull(viewModel.businessNameError.value)
        assertEquals(SellerProfileUiState.Idle, viewModel.uiState.value)
    }
}

