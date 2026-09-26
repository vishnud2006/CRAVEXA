package com.cravexa

import com.cravexa.domain.model.CartItem
import com.cravexa.domain.model.Product
import com.cravexa.domain.usecase.CartCalculator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CartCalculatorTest {

    private lateinit var cartCalculator: CartCalculator

    private val sampleProduct1 = Product(
        id = "prod_1",
        name = "Grandma's Andhra Avakaya Mango Pickle",
        description = "Spicy homemade pickle",
        price = 280.0,
        sellerId = "sel_1",
        sellerName = "Lakshmi's Andhra Kitchen",
        categoryId = "cat_pickles",
        categoryName = "Pickles",
        stock = 10,
        available = true
    )

    private val sampleProduct2 = Product(
        id = "prod_2",
        name = "Pure Desi Ghee Mysore Pak",
        description = "Melt in mouth sweet",
        price = 250.0,
        sellerId = "sel_2",
        sellerName = "Annapoorna Sweets",
        categoryId = "cat_sweets",
        categoryName = "Sweets",
        stock = 8,
        available = true
    )

    @Before
    fun setUp() {
        cartCalculator = CartCalculator()
    }

    @Test
    fun `empty cart calculates zero values and free delivery flag`() {
        val result = cartCalculator.calculate(emptyList())

        assertEquals(0.0, result.subtotal, 0.01)
        assertEquals(0.0, result.deliveryFee, 0.01)
        assertEquals(0.0, result.platformFee, 0.01)
        assertEquals(0.0, result.discount, 0.01)
        assertEquals(0.0, result.total, 0.01)
        assertTrue(result.isFreeDelivery)
        assertEquals("₹0", result.formattedTotal)
    }

    @Test
    fun `cart below threshold 499 includes delivery fee 49 and platform fee 5`() {
        val items = listOf(CartItem(product = sampleProduct1, quantity = 1)) // 1 * 280 = 280
        val result = cartCalculator.calculate(items)

        assertEquals(280.0, result.subtotal, 0.01)
        assertEquals(49.0, result.deliveryFee, 0.01)
        assertEquals(5.0, result.platformFee, 0.01)
        assertEquals(0.0, result.discount, 0.01)
        assertEquals(334.0, result.total, 0.01) // 280 + 49 + 5
        assertFalse(result.isFreeDelivery)
        assertEquals(219.0, result.amountNeededForFreeDelivery, 0.01) // 499 - 280
        assertEquals("₹49", result.formattedDeliveryFee)
    }

    @Test
    fun `cart above threshold 499 unlocks free delivery`() {
        val items = listOf(
            CartItem(product = sampleProduct1, quantity = 1), // 280
            CartItem(product = sampleProduct2, quantity = 1)  // 250 -> subtotal = 530
        )
        val result = cartCalculator.calculate(items)

        assertEquals(530.0, result.subtotal, 0.01)
        assertEquals(0.0, result.deliveryFee, 0.01)
        assertEquals(5.0, result.platformFee, 0.01)
        assertEquals(535.0, result.total, 0.01) // 530 + 0 + 5
        assertTrue(result.isFreeDelivery)
        assertEquals(0.0, result.amountNeededForFreeDelivery, 0.01)
        assertEquals("FREE", result.formattedDeliveryFee)
    }

    @Test
    fun `coupon discount is applied correctly and subtracted from total`() {
        val items = listOf(
            CartItem(product = sampleProduct1, quantity = 1), // 280
            CartItem(product = sampleProduct2, quantity = 1)  // 250 -> 530
        )
        val result = cartCalculator.calculate(items, couponDiscount = 50.0)

        assertEquals(530.0, result.subtotal, 0.01)
        assertEquals(0.0, result.deliveryFee, 0.01)
        assertEquals(5.0, result.platformFee, 0.01)
        assertEquals(50.0, result.discount, 0.01)
        assertEquals(485.0, result.total, 0.01) // 530 + 5 - 50 = 485
        assertEquals("-₹50", result.formattedDiscount)
    }
}

