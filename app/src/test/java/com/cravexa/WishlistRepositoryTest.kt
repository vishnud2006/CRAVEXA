package com.cravexa

import com.cravexa.core.common.Resource
import com.cravexa.data.local.PreferenceManager
import com.cravexa.data.repository.WishlistRepositoryImpl
import com.cravexa.domain.model.Product
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class WishlistRepositoryTest {

    private val preferenceManager = mockk<PreferenceManager>(relaxed = true)
    private lateinit var repository: WishlistRepositoryImpl

    private val testProduct = Product(
        id = "prod_1",
        name = "Test Pickle",
        description = "Test Description",
        price = 100.0,
        sellerId = "sel_1",
        sellerName = "Test Kitchen",
        categoryId = "cat_pickles",
        categoryName = "Pickles"
    )

    @Before
    fun setUp() {
        repository = WishlistRepositoryImpl(preferenceManager)
    }

    @Test
    fun `isWishlisted returns true if product exists in wishlist`() = runTest {
        every { preferenceManager.wishlist } returns flowOf(listOf(testProduct))

        var isWishlisted = false
        repository.isWishlisted("prod_1").collect {
            isWishlisted = it
        }

        assertTrue(isWishlisted)
    }

    @Test
    fun `addToWishlist adds product if not present`() = runTest {
        every { preferenceManager.wishlist } returns flowOf(emptyList())

        val result = repository.addToWishlist(testProduct)

        assertTrue(result is Resource.Success)
        coVerify { preferenceManager.saveWishlist(listOf(testProduct)) }
    }

    @Test
    fun `removeFromWishlist removes product from list`() = runTest {
        every { preferenceManager.wishlist } returns flowOf(listOf(testProduct))

        val result = repository.removeFromWishlist("prod_1")

        assertTrue(result is Resource.Success)
        coVerify { preferenceManager.saveWishlist(emptyList()) }
    }
}

