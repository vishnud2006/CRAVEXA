package com.cravexa

import com.cravexa.core.common.Resource
import com.cravexa.domain.model.Order
import com.cravexa.domain.model.OrderStatus
import com.cravexa.domain.model.PaymentStatus
import com.cravexa.domain.usecase.GetOrdersUseCase
import com.cravexa.presentation.customer.orders.OrdersUiState
import com.cravexa.presentation.customer.orders.OrdersViewModel
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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class OrdersViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val getOrdersUseCase = mockk<GetOrdersUseCase>()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { getOrdersUseCase.observe() } returns flowOf(emptyList())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadOrders with successful list updates state to Success`() = runTest(testDispatcher) {
        val sampleOrder = Order(
            id = "ord_1",
            orderNumber = "CRV-1001",
            orderDate = "26 Aug 2026",
            sellerName = "Lakshmi's Kitchen",
            totalAmount = 560.0,
            paymentStatus = PaymentStatus.PAID,
            orderStatus = OrderStatus.IN_TRANSIT
        )
        coEvery { getOrdersUseCase() } returns Resource.Success(listOf(sampleOrder))

        val viewModel = OrdersViewModel(getOrdersUseCase)
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value is OrdersUiState.Success)
        val successState = viewModel.uiState.value as OrdersUiState.Success
        assertEquals(1, successState.orders.size)
        assertEquals("CRV-1001", successState.orders[0].orderNumber)
    }

    @Test
    fun `loadOrders with error updates state to Error`() = runTest(testDispatcher) {
        coEvery { getOrdersUseCase() } returns Resource.Error("Network error occurred.")

        val viewModel = OrdersViewModel(getOrdersUseCase)
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value is OrdersUiState.Error)
        val errorState = viewModel.uiState.value as OrdersUiState.Error
        assertEquals("Network error occurred.", errorState.message)
    }
}

