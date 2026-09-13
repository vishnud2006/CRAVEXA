package com.cravexa

import com.cravexa.core.common.Resource
import com.cravexa.data.repository.CategoryRepositoryImpl
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CategoryRepositoryTest {

    private lateinit var repository: CategoryRepositoryImpl

    @Before
    fun setUp() {
        repository = CategoryRepositoryImpl()
    }

    @Test
    fun `getCategories returns 10 core marketplace categories`() = runTest {
        val emissions = repository.getCategories().toList()
        val success = emissions.last() as Resource.Success
        assertNotNull(success.data)
        assertEquals(10, success.data!!.size)
    }

    @Test
    fun `getCategoryById returns category for valid id`() = runTest {
        val emissions = repository.getCategoryById("cat_pickles").toList()
        val success = emissions.last() as Resource.Success
        assertEquals("cat_pickles", success.data?.id)
        assertEquals("Handmade Pickles", success.data?.name)
    }

    @Test
    fun `getCategoryById returns error for invalid id`() = runTest {
        val emissions = repository.getCategoryById("invalid_cat").toList()
        val error = emissions.last() as Resource.Error
        assertEquals("Category not found", error.message)
    }
}

