package com.cravexa

import com.cravexa.core.common.Resource
import com.cravexa.domain.model.CartItem
import com.cravexa.domain.model.Product
import com.cravexa.domain.usecase.CartCalculator
import com.cravexa.domain.usecase.ClearCartUseCase
import com.cravexa.domain.usecase.GetCartUseCase
import com.cravexa.domain.usecase.RemoveFromCartUseCase
import com.cravexa.domain.usecase.SaveForLaterUseCase
import com.cravexa.domain.usecase.UpdateCartQuantityUseCase
import com.cravexa.presentation.cart.CartViewModel
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
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CartViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val getCartUseCase = mockk<GetCartUseCase>()
    private val updateCartQuantityUseCase = mockk<UpdateCartQuantityUseCase>()
    private val removeFromCartUseCase = mockk<RemoveFromCartUseCase>()
    private val clearCartUseCase = mockk<ClearCartUseCase>()
    private val saveForLaterUseCase = mockk<SaveForLaterUseCase>()
    private val cartCalculator = CartCalculator()

    private val sampleProduct = Product(
        id = "prod_1",
        name = "Grandma's Mango Pickle",
        description = "Authentic Andhra style spicy avakaya mango pickle.",
        price = 249.0,
        originalPrice = 299.0,
        categoryId = "cat_pickles",
        categoryName = "Pickles & Chutneys",
        sellerId = "sel_1",
        sellerName = "Lakshmi Devi",
        sellerLocation = "Guntur, Andhra Pradesh",
        stock = 15,
        rating = 4.8,
        reviewCount = 124,
        weight = "500g",
        region = "Andhra Pradesh",
        foodType = "Vegetarian",
        shelfLife = "12 Months"
    )

    private val sampleItem = CartItem(
        product = sampleProduct,
        quantity = 2
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { getCartUseCase.getCartItems() } returns flowOf(listOf(sampleItem))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `uiState accurately calculates subtotal, delivery fee, and total`() = runTest(testDispatcher) {
        val viewModel = CartViewModel(
            getCartUseCase,
            updateCartQuantityUseCase,
            removeFromCartUseCase,
            clearCartUseCase,
            saveForLaterUseCase,
            cartCalculator
        )

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(1, state.items.size)
        assertEquals(2, state.itemCount)
        assertEquals(498.0, state.subtotal, 0.01) // 2 * 249
        assertEquals(49.0, state.deliveryFee, 0.01) // subtotal < 499
        assertEquals(5.0, state.platformFee, 0.01)
        assertEquals(552.0, state.total, 0.01) // 498 + 49 + 5
    }

    @Test
    fun `incrementQuantity calls updateCartQuantityUseCase with increased count`() = runTest(testDispatcher) {
        coEvery { updateCartQuantityUseCase(any(), any()) } returns Resource.Success(Unit)

        val viewModel = CartViewModel(
            getCartUseCase,
            updateCartQuantityUseCase,
            removeFromCartUseCase,
            clearCartUseCase,
            saveForLaterUseCase,
            cartCalculator
        )

        advanceUntilIdle()
        viewModel.incrementQuantity(sampleItem)
        advanceUntilIdle()

        coVerify { updateCartQuantityUseCase("prod_1", 3) }
    }

    @Test
    fun `decrementQuantity when quantity is 1 calls removeFromCartUseCase`() = runTest(testDispatcher) {
        coEvery { removeFromCartUseCase(any()) } returns Resource.Success(Unit)

        val singleItem = sampleItem.copy(quantity = 1)
        val viewModel = CartViewModel(
            getCartUseCase,
            updateCartQuantityUseCase,
            removeFromCartUseCase,
            clearCartUseCase,
            saveForLaterUseCase,
            cartCalculator
        )

        advanceUntilIdle()
        viewModel.decrementQuantity(singleItem)
        advanceUntilIdle()

        coVerify { removeFromCartUseCase("prod_1") }
    }

    @Test
    fun `removeItem calls removeFromCartUseCase`() = runTest(testDispatcher) {
        coEvery { removeFromCartUseCase(any()) } returns Resource.Success(Unit)

        val viewModel = CartViewModel(
            getCartUseCase,
            updateCartQuantityUseCase,
            removeFromCartUseCase,
            clearCartUseCase,
            saveForLaterUseCase,
            cartCalculator
        )

        advanceUntilIdle()
        viewModel.removeItem(sampleItem)
        advanceUntilIdle()

        coVerify { removeFromCartUseCase("prod_1") }
    }

    @Test
    fun `saveForLater calls saveForLaterUseCase`() = runTest(testDispatcher) {
        coEvery { saveForLaterUseCase(any()) } returns Resource.Success(Unit)

        val viewModel = CartViewModel(
            getCartUseCase,
            updateCartQuantityUseCase,
            removeFromCartUseCase,
            clearCartUseCase,
            saveForLaterUseCase,
            cartCalculator
        )

        advanceUntilIdle()
        viewModel.saveForLater(sampleItem)
        advanceUntilIdle()

        coVerify { saveForLaterUseCase(sampleItem) }
    }

    @Test
    fun `clearCart calls clearCartUseCase`() = runTest(testDispatcher) {
        coEvery { clearCartUseCase() } returns Resource.Success(Unit)

        val viewModel = CartViewModel(
            getCartUseCase,
            updateCartQuantityUseCase,
            removeFromCartUseCase,
            clearCartUseCase,
            saveForLaterUseCase,
            cartCalculator
        )

        advanceUntilIdle()
        viewModel.clearCart()
        advanceUntilIdle()

        coVerify { clearCartUseCase() }
    }

    @Test
    fun `applyCoupon with valid code updates discount and total`() = runTest(testDispatcher) {
        val viewModel = CartViewModel(
            getCartUseCase,
            updateCartQuantityUseCase,
            removeFromCartUseCase,
            clearCartUseCase,
            saveForLaterUseCase,
            cartCalculator
        )

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }

        advanceUntilIdle()
        viewModel.applyCoupon("CRAVE10")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("CRAVE10", state.appliedCoupon)
        assertEquals(50.0, state.promoDiscount, 0.01)
        assertEquals(502.0, state.total, 0.01) // 552 - 50
    }
}
