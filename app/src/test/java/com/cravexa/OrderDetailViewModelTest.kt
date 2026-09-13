package com.cravexa

import com.cravexa.core.common.Resource
import com.cravexa.domain.model.Order
import com.cravexa.domain.model.OrderStatus
import com.cravexa.domain.model.PaymentStatus
import com.cravexa.domain.usecase.GetOrderDetailUseCase
import com.cravexa.presentation.customer.orders.OrderDetailUiState
import com.cravexa.presentation.customer.orders.OrderDetailViewModel
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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class OrderDetailViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val getOrderDetailUseCase = mockk<GetOrderDetailUseCase>()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadOrder with valid id updates state to Success`() = runTest(testDispatcher) {
        val sampleOrder = Order(
            id = "ord_201",
            orderNumber = "CRV-2001",
            orderDate = "26 Aug 2026",
            sellerName = "Malnad Spices",
            totalAmount = 350.0,
            paymentStatus = PaymentStatus.PAID,
            orderStatus = OrderStatus.DELIVERED
        )
        coEvery { getOrderDetailUseCase("ord_201") } returns Resource.Success(sampleOrder)

        val viewModel = OrderDetailViewModel(getOrderDetailUseCase)
        viewModel.loadOrder("ord_201")
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value is OrderDetailUiState.Success)
        val success = viewModel.uiState.value as OrderDetailUiState.Success
        assertEquals("CRV-2001", success.order.orderNumber)
    }

    @Test
    fun `loadOrder with invalid id updates state to Error`() = runTest(testDispatcher) {
        coEvery { getOrderDetailUseCase("ord_invalid") } returns Resource.Error("Order not found.")

        val viewModel = OrderDetailViewModel(getOrderDetailUseCase)
        viewModel.loadOrder("ord_invalid")
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value is OrderDetailUiState.Error)
        val errorState = viewModel.uiState.value as OrderDetailUiState.Error
        assertEquals("Order not found.", errorState.message)
    }
}

