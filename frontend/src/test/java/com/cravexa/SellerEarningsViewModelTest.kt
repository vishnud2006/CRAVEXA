package com.cravexa

import com.cravexa.core.common.Resource
import com.cravexa.domain.model.PayoutStatus
import com.cravexa.domain.model.SellerDashboardStats
import com.cravexa.domain.model.SellerPayout
import com.cravexa.domain.usecase.GetSellerEarningsUseCase
import com.cravexa.domain.usecase.GetSellerPayoutsUseCase
import com.cravexa.presentation.seller.earnings.SellerEarningsUiState
import com.cravexa.presentation.seller.earnings.SellerEarningsViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
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
class SellerEarningsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val getSellerEarningsUseCase = mockk<GetSellerEarningsUseCase>()
    private val getSellerPayoutsUseCase = mockk<GetSellerPayoutsUseCase>()

    private val testStats = SellerDashboardStats(
        totalSales = 5000.0,
        totalEarnings = 4500.0,
        availableBalance = 2000.0
    )

    private val testPayouts = listOf(
        SellerPayout(
            id = "pay_1",
            amount = 2500.0,
            date = "28 Aug 2026",
            status = PayoutStatus.COMPLETED,
            referenceId = "REF-101"
        )
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { getSellerEarningsUseCase() } returns flowOf(Resource.Success(testStats))
        every { getSellerPayoutsUseCase() } returns flowOf(Resource.Success(testPayouts))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `uiState emits Success with stats and payouts`() = runTest(testDispatcher) {
        val viewModel = SellerEarningsViewModel(getSellerEarningsUseCase, getSellerPayoutsUseCase)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is SellerEarningsUiState.Success)
        val success = state as SellerEarningsUiState.Success
        assertEquals(2000.0, success.stats.availableBalance, 0.01)
        assertEquals(1, success.payouts.size)
    }
}

