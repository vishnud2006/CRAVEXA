package com.cravexa

import com.cravexa.core.common.Resource
import com.cravexa.data.repository.ProductRepositoryImpl
import com.cravexa.domain.model.SearchFilter
import com.cravexa.domain.model.SortOption
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ProductRepositoryTest {

    private lateinit var repository: ProductRepositoryImpl

    @Before
    fun setUp() {
        repository = ProductRepositoryImpl()
    }

    @Test
    fun `getHeroBanners returns non-empty list of banners`() = runTest {
        val banners = repository.getHeroBanners().toList().first()
        assertTrue(banners.isNotEmpty())
        assertEquals("banner_1", banners[0].id)
    }

    @Test
    fun `getFeaturedProducts returns only featured items`() = runTest {
        val emissions = repository.getFeaturedProducts().toList()
        val success = emissions.last() as Resource.Success
        assertNotNull(success.data)
        assertTrue(success.data!!.all { it.featured })
    }

    @Test
    fun `getTrendingProducts returns only trending items`() = runTest {
        val emissions = repository.getTrendingProducts().toList()
        val success = emissions.last() as Resource.Success
        assertNotNull(success.data)
        assertTrue(success.data!!.all { it.trending })
    }

    @Test
    fun `getProductById returns correct product for existing ID`() = runTest {
        val emissions = repository.getProductById("prod_1").toList()
        val success = emissions.last() as Resource.Success
        assertEquals("prod_1", success.data?.id)
        assertEquals("Grandma's Andhra Avakaya Mango Pickle", success.data?.name)
    }

    @Test
    fun `getProductById returns error for invalid ID`() = runTest {
        val emissions = repository.getProductById("invalid_id").toList()
        val error = emissions.last() as Resource.Error
        assertEquals("Product not found.", error.message)
    }

    @Test
    fun `searchProducts filters correctly by query`() = runTest {
        val emissions = repository.searchProducts("Avakaya").toList()
        val success = emissions.last() as Resource.Success
        assertTrue(success.data!!.any { it.name.contains("Avakaya", ignoreCase = true) })
    }

    @Test
    fun `searchProducts applies price sorting`() = runTest {
        val emissions = repository.searchProducts("", sort = SortOption.PRICE_LOW_TO_HIGH).toList()
        val success = emissions.last() as Resource.Success
        val list = success.data!!
        for (i in 0 until list.size - 1) {
            assertTrue(list[i].price <= list[i + 1].price)
        }
    }

    @Test
    fun `searchProducts applies category and region filters`() = runTest {
        val filter = SearchFilter(categoryId = "cat_pickles", region = "Andhra Pradesh")
        val emissions = repository.searchProducts("", filter = filter).toList()
        val success = emissions.last() as Resource.Success
        assertTrue(success.data!!.all { it.categoryId == "cat_pickles" && it.region == "Andhra Pradesh" })
    }
}

