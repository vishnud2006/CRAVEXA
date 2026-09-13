package com.cravexa

import com.cravexa.core.common.Resource
import com.cravexa.domain.model.Order
import com.cravexa.domain.model.OrderStatus
import com.cravexa.domain.usecase.GetSellerOrdersUseCase
import com.cravexa.domain.usecase.UpdateSellerOrderStatusUseCase
import com.cravexa.presentation.seller.orders.SellerOrderFilter
import com.cravexa.presentation.seller.orders.SellerOrdersUiState
import com.cravexa.presentation.seller.orders.SellerOrdersViewModel
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
class SellerOrdersViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val getSellerOrdersUseCase = mockk<GetSellerOrdersUseCase>()
    private val updateSellerOrderStatusUseCase = mockk<UpdateSellerOrderStatusUseCase>()

    private val testOrders = listOf(
        Order(
            id = "ord_1",
            orderNumber = "CRV-101",
            orderDate = "31 Aug 2026",
            totalAmount = 500.0,
            orderStatus = OrderStatus.PAYMENT_CONFIRMED
        ),
        Order(
            id = "ord_2",
            orderNumber = "CRV-102",
            orderDate = "31 Aug 2026",
            totalAmount = 350.0,
            orderStatus = OrderStatus.PREPARING
        ),
        Order(
            id = "ord_3",
            orderNumber = "CRV-103",
            orderDate = "30 Aug 2026",
            totalAmount = 800.0,
            orderStatus = OrderStatus.DELIVERED
        )
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { getSellerOrdersUseCase.ordersFlow } returns flowOf(testOrders)
        coEvery { updateSellerOrderStatusUseCase(any(), any()) } answers {
            Resource.Success(testOrders.first().copy(orderStatus = secondArg()))
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `uiState emits all orders by default`() = runTest(testDispatcher) {
        val viewModel = SellerOrdersViewModel(getSellerOrdersUseCase, updateSellerOrderStatusUseCase)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }

        advanceUntilIdle()

        val state = viewModel.uiState.value as SellerOrdersUiState.Success
        assertEquals(3, state.filteredOrders.size)
        assertEquals(SellerOrderFilter.ALL, state.activeFilter)
    }

    @Test
    fun `filter PREPARING returns only preparing orders`() = runTest(testDispatcher) {
        val viewModel = SellerOrdersViewModel(getSellerOrdersUseCase, updateSellerOrderStatusUseCase)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }

        viewModel.onFilterSelected(SellerOrderFilter.PREPARING)
        advanceUntilIdle()

        val state = viewModel.uiState.value as SellerOrdersUiState.Success
        assertEquals(1, state.filteredOrders.size)
        assertEquals("ord_2", state.filteredOrders.first().id)
    }

    @Test
    fun `updateStatus calls updateSellerOrderStatusUseCase`() = runTest(testDispatcher) {
        val viewModel = SellerOrdersViewModel(getSellerOrdersUseCase, updateSellerOrderStatusUseCase)

        viewModel.updateStatus("ord_1", OrderStatus.PREPARING)
        advanceUntilIdle()

        coVerify { updateSellerOrderStatusUseCase("ord_1", OrderStatus.PREPARING) }
    }
}

