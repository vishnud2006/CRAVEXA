package com.cravexa

import androidx.lifecycle.SavedStateHandle
import com.cravexa.core.common.Resource
import com.cravexa.domain.model.Category
import com.cravexa.domain.model.Product
import com.cravexa.domain.model.SortOption
import com.cravexa.domain.usecase.AddToCartUseCase
import com.cravexa.domain.usecase.GetCategoriesUseCase
import com.cravexa.domain.usecase.GetCategoryProductsUseCase
import com.cravexa.domain.usecase.GetWishlistUseCase
import com.cravexa.domain.usecase.ToggleWishlistUseCase
import com.cravexa.presentation.category.CategoryProductsViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
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
class CategoryProductsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val getCategoryProductsUseCase = mockk<GetCategoryProductsUseCase>()
    private val getCategoriesUseCase = mockk<GetCategoriesUseCase>()
    private val getWishlistUseCase = mockk<GetWishlistUseCase>()
    private val toggleWishlistUseCase = mockk<ToggleWishlistUseCase>()
    private val addToCartUseCase = mockk<AddToCartUseCase>()

    private val testCategory = Category(
        id = "cat_pickles",
        name = "Handmade Pickles",
        description = "Sun-ripened pickles"
    )

    private val testProducts = listOf(
        Product(
            id = "prod_1",
            name = "Andhra Mango Pickle",
            description = "Spicy",
            price = 280.0,
            sellerId = "sel_1",
            sellerName = "Lakshmi Kitchen",
            categoryId = "cat_pickles",
            categoryName = "Pickles"
        ),
        Product(
            id = "prod_2",
            name = "Banarasi Chilli Pickle",
            description = "Tangy",
            price = 190.0,
            sellerId = "sel_2",
            sellerName = "Ganga Kitchen",
            categoryId = "cat_pickles",
            categoryName = "Pickles"
        )
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { getCategoriesUseCase.getCategoryById("cat_pickles") } returns flowOf(Resource.Success(testCategory))
        every { getCategoryProductsUseCase("cat_pickles") } returns flowOf(Resource.Success(testProducts))
        every { getWishlistUseCase() } returns flowOf(emptyList())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadCategoryData loads products and category info`() = runTest(testDispatcher) {
        val savedState = SavedStateHandle(mapOf("categoryId" to "cat_pickles", "categoryName" to "Handmade Pickles"))
        val viewModel = CategoryProductsViewModel(
            savedState,
            getCategoryProductsUseCase,
            getCategoriesUseCase,
            getWishlistUseCase,
            toggleWishlistUseCase,
            addToCartUseCase
        )

        advanceUntilIdle()

        assertEquals("Handmade Pickles", viewModel.categoryInfo.value?.name)
        val state = viewModel.productsState.value
        assertTrue(state is Resource.Success)
        assertEquals(2, (state as Resource.Success).data?.size)
    }

    @Test
    fun `setSortOption sorts products by price low to high`() = runTest(testDispatcher) {
        val savedState = SavedStateHandle(mapOf("categoryId" to "cat_pickles", "categoryName" to "Handmade Pickles"))
        val viewModel = CategoryProductsViewModel(
            savedState,
            getCategoryProductsUseCase,
            getCategoriesUseCase,
            getWishlistUseCase,
            toggleWishlistUseCase,
            addToCartUseCase
        )

        advanceUntilIdle()

        viewModel.setSortOption(SortOption.PRICE_LOW_TO_HIGH)
        val state = viewModel.productsState.value as Resource.Success
        assertEquals(190.0, state.data?.first()?.price ?: 0.0, 0.01)
    }
}

