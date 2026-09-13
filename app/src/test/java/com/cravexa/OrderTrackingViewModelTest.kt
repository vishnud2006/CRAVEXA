package com.cravexa

import com.cravexa.core.common.Resource
import com.cravexa.domain.model.OrderStatus
import com.cravexa.domain.model.OrderTracking
import com.cravexa.domain.model.TrackingEvent
import com.cravexa.domain.usecase.GetOrderTrackingUseCase
import com.cravexa.presentation.customer.orders.OrderTrackingUiState
import com.cravexa.presentation.customer.orders.OrderTrackingViewModel
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
class OrderTrackingViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val getOrderTrackingUseCase = mockk<GetOrderTrackingUseCase>()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadTracking with valid orderId updates state to Success and provides 8 milestone events`() = runTest(testDispatcher) {
        val sampleEvents = OrderStatus.trackingSteps.mapIndexed { index, status ->
            TrackingEvent(
                status = status,
                title = status.displayTitle,
                description = "Milestone description",
                isCompleted = index < 3,
                isCurrent = index == 3
            )
        }

        val tracking = OrderTracking(
            orderId = "ord_301",
            orderNumber = "CRV-3001",
            currentStatus = OrderStatus.READY_FOR_PICKUP,
            trackingNumber = "CRX-TRK-3001",
            carrierName = "CRAVEXA Express",
            events = sampleEvents
        )

        coEvery { getOrderTrackingUseCase("ord_301") } returns Resource.Success(tracking)

        val viewModel = OrderTrackingViewModel(getOrderTrackingUseCase)
        viewModel.loadTracking("ord_301")
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value is OrderTrackingUiState.Success)
        val success = viewModel.uiState.value as OrderTrackingUiState.Success
        assertEquals(8, success.tracking.events.size)
        assertEquals(OrderStatus.READY_FOR_PICKUP, success.tracking.currentStatus)
        assertTrue(success.tracking.events[0].isCompleted)
        assertTrue(success.tracking.events[3].isCurrent)
    }

    @Test
    fun `loadTracking with error updates state to Error`() = runTest(testDispatcher) {
        coEvery { getOrderTrackingUseCase("ord_unknown") } returns Resource.Error("Tracking not found.")

        val viewModel = OrderTrackingViewModel(getOrderTrackingUseCase)
        viewModel.loadTracking("ord_unknown")
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value is OrderTrackingUiState.Error)
    }
}

