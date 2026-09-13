package com.cravexa

import com.cravexa.core.common.Resource
import com.cravexa.data.local.PreferenceManager
import com.cravexa.data.repository.CartRepositoryImpl
import com.cravexa.domain.model.CartItem
import com.cravexa.domain.model.Product
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CartRepositoryTest {

    private val preferenceManager = mockk<PreferenceManager>(relaxed = true)
    private lateinit var repository: CartRepositoryImpl

    private val testProduct = Product(
        id = "prod_1",
        name = "Test Pickle",
        description = "Test Description",
        price = 150.0,
        sellerId = "sel_1",
        sellerName = "Test Kitchen",
        categoryId = "cat_pickles",
        categoryName = "Pickles"
    )

    @Before
    fun setUp() {
        repository = CartRepositoryImpl(preferenceManager)
    }

    @Test
    fun `getCartItemCount calculates total quantities correctly`() = runTest {
        val cartItems = listOf(
            CartItem(product = testProduct, quantity = 2),
            CartItem(product = testProduct.copy(id = "prod_2"), quantity = 3)
        )
        every { preferenceManager.cartItems } returns flowOf(cartItems)

        var count = 0
        repository.getCartItemCount().collect {
            count = it
        }

        assertEquals(5, count)
    }

    @Test
    fun `getCartTotal calculates total price correctly`() = runTest {
        val cartItems = listOf(
            CartItem(product = testProduct, quantity = 2), // 300
            CartItem(product = testProduct.copy(id = "prod_2", price = 200.0), quantity = 1) // 200
        )
        every { preferenceManager.cartItems } returns flowOf(cartItems)

        var total = 0.0
        repository.getCartTotal().collect {
            total = it
        }

        assertEquals(500.0, total, 0.01)
    }

    @Test
    fun `addToCart adds new item to cart list`() = runTest {
        every { preferenceManager.cartItems } returns flowOf(emptyList())

        val result = repository.addToCart(testProduct, 2)

        assertTrue(result is Resource.Success)
        coVerify { preferenceManager.saveCartItems(listOf(CartItem(testProduct, 2))) }
    }
}

