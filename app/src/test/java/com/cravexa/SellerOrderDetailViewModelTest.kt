package com.cravexa

import androidx.lifecycle.SavedStateHandle
import com.cravexa.core.common.Resource
import com.cravexa.domain.model.Order
import com.cravexa.domain.model.OrderStatus
import com.cravexa.domain.usecase.GetSellerOrderDetailUseCase
import com.cravexa.domain.usecase.UpdateSellerOrderStatusUseCase
import com.cravexa.presentation.seller.orders.SellerOrderDetailUiState
import com.cravexa.presentation.seller.orders.SellerOrderDetailViewModel
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
class SellerOrderDetailViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val getSellerOrderDetailUseCase = mockk<GetSellerOrderDetailUseCase>()
    private val updateSellerOrderStatusUseCase = mockk<UpdateSellerOrderStatusUseCase>()

    private val testOrder = Order(
        id = "ord_seller_101",
        orderNumber = "CRV-89421",
        orderDate = "31 Aug 2026, 09:15 AM",
        totalAmount = 799.0,
        orderStatus = OrderStatus.PAYMENT_CONFIRMED
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        coEvery { getSellerOrderDetailUseCase("ord_seller_101") } returns Resource.Success(testOrder)
        coEvery { updateSellerOrderStatusUseCase("ord_seller_101", OrderStatus.PREPARING) } returns
                Resource.Success(testOrder.copy(orderStatus = OrderStatus.PREPARING))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadOrder loads order successfully`() = runTest(testDispatcher) {
        val handle = SavedStateHandle(mapOf("orderId" to "ord_seller_101"))
        val viewModel = SellerOrderDetailViewModel(handle, getSellerOrderDetailUseCase, updateSellerOrderStatusUseCase)

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is SellerOrderDetailUiState.Success)
        assertEquals("CRV-89421", (state as SellerOrderDetailUiState.Success).order.orderNumber)
    }

    @Test
    fun `updateStatus updates order status to PREPARING`() = runTest(testDispatcher) {
        val handle = SavedStateHandle(mapOf("orderId" to "ord_seller_101"))
        val viewModel = SellerOrderDetailViewModel(handle, getSellerOrderDetailUseCase, updateSellerOrderStatusUseCase)

        advanceUntilIdle()

        viewModel.updateStatus(OrderStatus.PREPARING)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is SellerOrderDetailUiState.Success)
        assertEquals(OrderStatus.PREPARING, (state as SellerOrderDetailUiState.Success).order.orderStatus)
    }
}

