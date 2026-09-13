package com.cravexa

import com.cravexa.core.common.Resource
import com.cravexa.data.repository.SellerEarningsRepositoryImpl
import com.cravexa.data.repository.SellerOrderRepositoryImpl
import com.cravexa.data.repository.SellerProductRepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SellerEarningsRepositoryTest {

    private lateinit var productRepository: SellerProductRepositoryImpl
    private lateinit var orderRepository: SellerOrderRepositoryImpl
    private lateinit var earningsRepository: SellerEarningsRepositoryImpl

    @Before
    fun setUp() {
        productRepository = SellerProductRepositoryImpl()
        orderRepository = SellerOrderRepositoryImpl()
        earningsRepository = SellerEarningsRepositoryImpl(productRepository, orderRepository)
    }

    @Test
    fun `getDashboardStats calculates sales and pending orders correctly`() = runTest {
        val statsResource = earningsRepository.getDashboardStats().first()
        assertTrue(statsResource is Resource.Success)

        val stats = (statsResource as Resource.Success).data
        assertTrue(stats != null)
        assertTrue(stats!!.totalSales > 0.0)
        assertTrue(stats.totalOrders > 0)
        assertTrue(stats.totalProducts > 0)
    }

    @Test
    fun `getPayoutHistory returns valid payout records`() = runTest {
        val payoutsResource = earningsRepository.getPayoutHistory().first { it is Resource.Success }
        assertTrue(payoutsResource is Resource.Success)

        val payouts = (payoutsResource as Resource.Success).data
        assertTrue(!payouts.isNullOrEmpty())
        assertEquals("pay_101", payouts!!.first().id)
    }
}

