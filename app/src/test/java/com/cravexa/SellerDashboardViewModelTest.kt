package com.cravexa

import com.cravexa.core.common.Resource
import com.cravexa.domain.model.SellerDashboardStats
import com.cravexa.domain.model.SellerProfile
import com.cravexa.domain.usecase.GetSellerDashboardUseCase
import com.cravexa.domain.usecase.GetSellerOrdersUseCase
import com.cravexa.presentation.seller.dashboard.SellerDashboardUiState
import com.cravexa.presentation.seller.dashboard.SellerDashboardViewModel
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
class SellerDashboardViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val getDashboardUseCase = mockk<GetSellerDashboardUseCase>()
    private val getOrdersUseCase = mockk<GetSellerOrdersUseCase>()

    private val testStats = SellerDashboardStats(
        todaySales = 500.0,
        totalSales = 2500.0,
        totalOrders = 5,
        pendingOrders = 2,
        totalProducts = 4,
        availableBalance = 1200.0
    )

    private val testProfile = SellerProfile(
        id = "sel_1",
        sellerName = "Lakshmi Devi",
        businessName = "Lakshmi's Home Kitchen"
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { getDashboardUseCase() } returns flowOf(Resource.Success(testStats))
        every { getDashboardUseCase.getProfile() } returns flowOf(testProfile)
        every { getOrdersUseCase.ordersFlow } returns flowOf(emptyList())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `uiState emits Success with dashboard metrics and seller profile`() = runTest(testDispatcher) {
        val viewModel = SellerDashboardViewModel(getDashboardUseCase, getOrdersUseCase)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is SellerDashboardUiState.Success)
        val successState = state as SellerDashboardUiState.Success
        assertEquals(500.0, successState.stats.todaySales, 0.01)
        assertEquals("Lakshmi's Home Kitchen", successState.profile?.businessName)
    }
}

