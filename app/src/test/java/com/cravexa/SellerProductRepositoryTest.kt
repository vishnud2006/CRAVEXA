package com.cravexa

import com.cravexa.core.common.Resource
import com.cravexa.data.repository.SellerProductRepositoryImpl
import com.cravexa.domain.model.Product
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SellerProductRepositoryTest {

    private lateinit var repository: SellerProductRepositoryImpl

    @Before
    fun setUp() {
        repository = SellerProductRepositoryImpl()
    }

    @Test
    fun `initial products are emitted by sellerProducts flow`() = runTest {
        val products = repository.sellerProducts.first()
        assertTrue(products.isNotEmpty())
        assertEquals("prod_1", products.first().id)
    }

    @Test
    fun `addProduct prepends new product to list`() = runTest {
        val newDish = Product(
            id = "prod_custom_100",
            name = "Handcrafted Besan Ladoo",
            description = "Pure desi ghee ladoo",
            price = 350.0,
            sellerId = "sel_1",
            sellerName = "Lakshmi's Home Kitchen",
            categoryId = "cat_sweets",
            categoryName = "Traditional Sweets",
            stock = 20
        )

        val result = repository.addProduct(newDish)
        assertTrue(result is Resource.Success)
        assertEquals("prod_custom_100", (result as Resource.Success).data?.id)

        val currentList = repository.sellerProducts.first()
        assertEquals("prod_custom_100", currentList.first().id)
    }

    @Test
    fun `updateStock adjusts stock count correctly`() = runTest {
        val result = repository.updateStock("prod_1", 50)
        assertTrue(result is Resource.Success)

        val updatedProduct = repository.getProductById("prod_1")
        assertTrue(updatedProduct is Resource.Success)
        assertEquals(50, (updatedProduct as Resource.Success).data?.stock)
    }

    @Test
    fun `toggleProductAvailability changes available flag`() = runTest {
        val result = repository.toggleProductAvailability("prod_1", false)
        assertTrue(result is Resource.Success)

        val updatedProduct = repository.getProductById("prod_1")
        assertTrue(updatedProduct is Resource.Success)
        assertEquals(false, (updatedProduct as Resource.Success).data?.available)
    }

    @Test
    fun `deleteProduct removes dish from catalog`() = runTest {
        val result = repository.deleteProduct("prod_1")
        assertTrue(result is Resource.Success)

        val check = repository.getProductById("prod_1")
        assertTrue(check is Resource.Error)
    }
}

