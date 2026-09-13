package com.cravexa

import com.cravexa.core.common.Resource
import com.cravexa.data.repository.SellerOrderRepositoryImpl
import com.cravexa.domain.model.OrderStatus
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SellerOrderRepositoryTest {

    private lateinit var repository: SellerOrderRepositoryImpl

    @Before
    fun setUp() {
        repository = SellerOrderRepositoryImpl()
    }

    @Test
    fun `initial seller orders are emitted by sellerOrders flow`() = runTest {
        val orders = repository.sellerOrders.first()
        assertTrue(orders.isNotEmpty())
        assertEquals("ord_seller_101", orders.first().id)
    }

    @Test
    fun `updateOrderStatus changes order status to PREPARING`() = runTest {
        val result = repository.updateOrderStatus("ord_seller_101", OrderStatus.PREPARING)
        assertTrue(result is Resource.Success)
        assertEquals(OrderStatus.PREPARING, (result as Resource.Success).data?.orderStatus)

        val updated = repository.getOrderById("ord_seller_101")
        assertTrue(updated is Resource.Success)
        assertEquals(OrderStatus.PREPARING, (updated as Resource.Success).data?.orderStatus)
    }

    @Test
    fun `updateOrderStatus changes order status to READY_FOR_PICKUP`() = runTest {
        val result = repository.updateOrderStatus("ord_seller_101", OrderStatus.READY_FOR_PICKUP)
        assertTrue(result is Resource.Success)
        assertEquals(OrderStatus.READY_FOR_PICKUP, (result as Resource.Success).data?.orderStatus)
    }
}

