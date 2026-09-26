package com.cravexa

import com.cravexa.core.common.Resource
import com.cravexa.domain.model.Product
import com.cravexa.domain.usecase.AddToCartUseCase
import com.cravexa.domain.usecase.GetWishlistUseCase
import com.cravexa.domain.usecase.ToggleWishlistUseCase
import com.cravexa.presentation.wishlist.WishlistViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WishlistViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val getWishlistUseCase = mockk<GetWishlistUseCase>()
    private val toggleWishlistUseCase = mockk<ToggleWishlistUseCase>()
    private val addToCartUseCase = mockk<AddToCartUseCase>()

    private val testProduct = Product(
        id = "prod_1",
        name = "Andhra Mango Pickle",
        description = "Spicy",
        price = 280.0,
        sellerId = "sel_1",
        sellerName = "Lakshmi Kitchen",
        categoryId = "cat_pickles",
        categoryName = "Pickles"
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { getWishlistUseCase() } returns flowOf(listOf(testProduct))
        coEvery { toggleWishlistUseCase(any()) } returns Resource.Success(false)
        coEvery { addToCartUseCase(any(), any()) } returns Resource.Success(Unit)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `wishlistProducts flow emits current saved products`() = runTest(testDispatcher) {
        val viewModel = WishlistViewModel(
            getWishlistUseCase,
            toggleWishlistUseCase,
            addToCartUseCase
        )

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.wishlistProducts.collect()
        }

        advanceUntilIdle()

        assertEquals(1, viewModel.wishlistProducts.value.size)
        assertEquals("prod_1", viewModel.wishlistProducts.value.first().id)
    }

    @Test
    fun `moveToCart adds item to cart and removes from wishlist`() = runTest(testDispatcher) {
        val viewModel = WishlistViewModel(
            getWishlistUseCase,
            toggleWishlistUseCase,
            addToCartUseCase
        )

        viewModel.moveToCart(testProduct)
        advanceUntilIdle()

        coVerify { addToCartUseCase(testProduct, 1) }
        coVerify { toggleWishlistUseCase(testProduct) }
    }
}

