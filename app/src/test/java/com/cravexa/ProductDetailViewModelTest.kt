package com.cravexa

import androidx.lifecycle.SavedStateHandle
import com.cravexa.core.common.Resource
import com.cravexa.domain.model.Product
import com.cravexa.domain.usecase.AddToCartUseCase
import com.cravexa.domain.usecase.GetProductDetailUseCase
import com.cravexa.domain.usecase.GetWishlistUseCase
import com.cravexa.domain.usecase.ToggleWishlistUseCase
import com.cravexa.presentation.product.ProductDetailViewModel
import io.mockk.coEvery
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
class ProductDetailViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val getProductDetailUseCase = mockk<GetProductDetailUseCase>()
    private val getWishlistUseCase = mockk<GetWishlistUseCase>()
    private val toggleWishlistUseCase = mockk<ToggleWishlistUseCase>()
    private val addToCartUseCase = mockk<AddToCartUseCase>()

    private val testProduct = Product(
        id = "prod_1",
        name = "Grandma's Andhra Avakaya Mango Pickle",
        description = "Fiery Avakaya",
        price = 280.0,
        sellerId = "sel_1",
        sellerName = "Lakshmi's Home Kitchen",
        categoryId = "cat_pickles",
        categoryName = "Pickles",
        stock = 10
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { getProductDetailUseCase("prod_1") } returns flowOf(Resource.Success(testProduct))
        every { getProductDetailUseCase.getRelatedProducts("prod_1", "cat_pickles") } returns flowOf(Resource.Success(emptyList()))
        every { getWishlistUseCase.isWishlisted("prod_1") } returns flowOf(true)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadProduct emits success product data`() = runTest(testDispatcher) {
        val savedState = SavedStateHandle(mapOf("productId" to "prod_1"))
        val viewModel = ProductDetailViewModel(
            savedState,
            getProductDetailUseCase,
            getWishlistUseCase,
            toggleWishlistUseCase,
            addToCartUseCase
        )

        advanceUntilIdle()

        val state = viewModel.productState.value
        assertTrue(state is Resource.Success)
        assertEquals("Grandma's Andhra Avakaya Mango Pickle", (state as Resource.Success).data?.name)
    }

    @Test
    fun `incrementQuantity and decrementQuantity update quantity within bounds`() = runTest(testDispatcher) {
        val savedState = SavedStateHandle(mapOf("productId" to "prod_1"))
        val viewModel = ProductDetailViewModel(
            savedState,
            getProductDetailUseCase,
            getWishlistUseCase,
            toggleWishlistUseCase,
            addToCartUseCase
        )

        advanceUntilIdle()

        assertEquals(1, viewModel.quantity.value)
        viewModel.incrementQuantity()
        assertEquals(2, viewModel.quantity.value)
        viewModel.decrementQuantity()
        assertEquals(1, viewModel.quantity.value)
        viewModel.decrementQuantity() // should not go below 1
        assertEquals(1, viewModel.quantity.value)
    }
}

