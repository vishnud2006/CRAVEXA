package com.cravexa

import com.cravexa.core.common.Resource
import com.cravexa.domain.model.Product
import com.cravexa.domain.usecase.DeleteSellerProductUseCase
import com.cravexa.domain.usecase.GetSellerKitchenProductsUseCase
import com.cravexa.domain.usecase.ToggleProductAvailabilityUseCase
import com.cravexa.domain.usecase.UpdateProductStockUseCase
import com.cravexa.presentation.seller.products.ProductFilter
import com.cravexa.presentation.seller.products.SellerProductsUiState
import com.cravexa.presentation.seller.products.SellerProductsViewModel
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
class SellerProductsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val getSellerKitchenProductsUseCase = mockk<GetSellerKitchenProductsUseCase>()
    private val updateProductStockUseCase = mockk<UpdateProductStockUseCase>()
    private val toggleProductAvailabilityUseCase = mockk<ToggleProductAvailabilityUseCase>()
    private val deleteSellerProductUseCase = mockk<DeleteSellerProductUseCase>()

    private val testProducts = listOf(
        Product(
            id = "prod_1",
            name = "Andhra Avakaya Mango Pickle",
            description = "Spicy mango pickle",
            price = 280.0,
            sellerId = "sel_1",
            sellerName = "Lakshmi Kitchen",
            categoryId = "cat_pickles",
            categoryName = "Handmade Pickles",
            stock = 25,
            available = true
        ),
        Product(
            id = "prod_2",
            name = "Gongura Thokku",
            description = "Tangy chutney",
            price = 240.0,
            sellerId = "sel_1",
            sellerName = "Lakshmi Kitchen",
            categoryId = "cat_pickles",
            categoryName = "Handmade Pickles",
            stock = 3, // Low stock
            available = true
        ),
        Product(
            id = "prod_3",
            name = "Kandi Podi",
            description = "Gunpowder",
            price = 180.0,
            sellerId = "sel_1",
            sellerName = "Lakshmi Kitchen",
            categoryId = "cat_spices",
            categoryName = "Spices",
            stock = 0, // Out of stock
            available = true
        )
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { getSellerKitchenProductsUseCase.productsFlow } returns flowOf(testProducts)
        coEvery { updateProductStockUseCase(any(), any()) } returns Resource.Success(Unit)
        coEvery { toggleProductAvailabilityUseCase(any(), any()) } returns Resource.Success(Unit)
        coEvery { deleteSellerProductUseCase(any()) } returns Resource.Success(Unit)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `uiState emits all products by default`() = runTest(testDispatcher) {
        val viewModel = SellerProductsViewModel(
            getSellerKitchenProductsUseCase,
            updateProductStockUseCase,
            toggleProductAvailabilityUseCase,
            deleteSellerProductUseCase
        )

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }

        advanceUntilIdle()

        val state = viewModel.uiState.value as SellerProductsUiState.Success
        assertEquals(3, state.filteredProducts.size)
        assertEquals(ProductFilter.ALL, state.activeFilter)
    }

    @Test
    fun `filter LOW_STOCK returns only products with stock in low stock range`() = runTest(testDispatcher) {
        val viewModel = SellerProductsViewModel(
            getSellerKitchenProductsUseCase,
            updateProductStockUseCase,
            toggleProductAvailabilityUseCase,
            deleteSellerProductUseCase
        )

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }

        viewModel.onFilterSelected(ProductFilter.LOW_STOCK)
        advanceUntilIdle()

        val state = viewModel.uiState.value as SellerProductsUiState.Success
        assertEquals(1, state.filteredProducts.size)
        assertEquals("prod_2", state.filteredProducts.first().id)
    }

    @Test
    fun `updateStock calls updateProductStockUseCase`() = runTest(testDispatcher) {
        val viewModel = SellerProductsViewModel(
            getSellerKitchenProductsUseCase,
            updateProductStockUseCase,
            toggleProductAvailabilityUseCase,
            deleteSellerProductUseCase
        )

        viewModel.updateStock("prod_1", 30)
        advanceUntilIdle()

        coVerify { updateProductStockUseCase("prod_1", 30) }
    }

    @Test
    fun `deleteProduct calls deleteSellerProductUseCase`() = runTest(testDispatcher) {
        val viewModel = SellerProductsViewModel(
            getSellerKitchenProductsUseCase,
            updateProductStockUseCase,
            toggleProductAvailabilityUseCase,
            deleteSellerProductUseCase
        )

        viewModel.deleteProduct("prod_1")
        advanceUntilIdle()

        coVerify { deleteSellerProductUseCase("prod_1") }
    }
}
