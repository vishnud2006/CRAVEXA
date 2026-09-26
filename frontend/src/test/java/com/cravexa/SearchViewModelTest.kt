package com.cravexa

import androidx.lifecycle.SavedStateHandle
import com.cravexa.core.common.Resource
import com.cravexa.domain.model.Product
import com.cravexa.domain.usecase.AddToCartUseCase
import com.cravexa.domain.usecase.GetCategoriesUseCase
import com.cravexa.domain.usecase.GetWishlistUseCase
import com.cravexa.domain.usecase.RecentSearchUseCases
import com.cravexa.domain.usecase.SearchProductsUseCase
import com.cravexa.domain.usecase.ToggleWishlistUseCase
import com.cravexa.presentation.search.SearchViewModel
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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val searchProductsUseCase = mockk<SearchProductsUseCase>()
    private val getCategoriesUseCase = mockk<GetCategoriesUseCase>()
    private val recentSearchUseCases = mockk<RecentSearchUseCases>(relaxed = true)
    private val getWishlistUseCase = mockk<GetWishlistUseCase>()
    private val toggleWishlistUseCase = mockk<ToggleWishlistUseCase>()
    private val addToCartUseCase = mockk<AddToCartUseCase>()

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
        )
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { searchProductsUseCase(any(), any(), any()) } returns flowOf(Resource.Success(testProducts))
        every { searchProductsUseCase.getSuggestions(any()) } returns flowOf(listOf("Andhra Mango Pickle"))
        every { getCategoriesUseCase() } returns flowOf(Resource.Success(emptyList()))
        every { recentSearchUseCases.getRecentSearches() } returns flowOf(listOf("Pickles", "Mysore Pak"))
        every { getWishlistUseCase() } returns flowOf(emptyList())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onQueryChange updates search query and fetches results`() = runTest(testDispatcher) {
        val savedState = SavedStateHandle(mapOf("query" to ""))
        val viewModel = SearchViewModel(
            savedState,
            searchProductsUseCase,
            getCategoriesUseCase,
            recentSearchUseCases,
            getWishlistUseCase,
            toggleWishlistUseCase,
            addToCartUseCase
        )

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.searchResults.collect()
        }

        viewModel.onQueryChange("Mango")
        advanceUntilIdle()

        assertEquals("Mango", viewModel.searchQuery.value)
        val results = viewModel.searchResults.value
        assertTrue(results is Resource.Success)
        assertEquals(1, (results as Resource.Success).data?.size)
    }
}

