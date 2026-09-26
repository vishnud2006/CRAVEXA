package com.cravexa

import com.cravexa.core.common.Resource
import com.cravexa.domain.model.SellerReview
import com.cravexa.domain.usecase.GetSellerReviewsUseCase
import com.cravexa.domain.usecase.ReplyToReviewUseCase
import com.cravexa.presentation.seller.reviews.SellerReviewsUiState
import com.cravexa.presentation.seller.reviews.SellerReviewsViewModel
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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SellerReviewsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val getSellerReviewsUseCase = mockk<GetSellerReviewsUseCase>()
    private val replyToReviewUseCase = mockk<ReplyToReviewUseCase>()

    private val testReviews = listOf(
        SellerReview(
            id = "rev_1",
            orderId = "ord_1",
            productId = "prod_1",
            productName = "Avakaya Pickle",
            customerName = "Ananya",
            rating = 5.0,
            comment = "Delicious!",
            date = "30 Aug 2026"
        ),
        SellerReview(
            id = "rev_2",
            orderId = "ord_2",
            productId = "prod_2",
            productName = "Gongura Thokku",
            customerName = "Rahul",
            rating = 4.0,
            comment = "Very authentic",
            date = "29 Aug 2026"
        )
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { getSellerReviewsUseCase() } returns flowOf(Resource.Success(testReviews))
        coEvery { replyToReviewUseCase(any(), any()) } returns Resource.Success(Unit)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `uiState emits Success with reviews and calculated average rating`() = runTest(testDispatcher) {
        val viewModel = SellerReviewsViewModel(getSellerReviewsUseCase, replyToReviewUseCase)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is SellerReviewsUiState.Success)
        val success = state as SellerReviewsUiState.Success
        assertEquals(2, success.totalReviews)
        assertEquals(4.5, success.averageRating, 0.01)
    }

    @Test
    fun `submitReply calls replyToReviewUseCase`() = runTest(testDispatcher) {
        val viewModel = SellerReviewsViewModel(getSellerReviewsUseCase, replyToReviewUseCase)

        viewModel.submitReply("rev_1", "Thank you!")
        advanceUntilIdle()

        coVerify { replyToReviewUseCase("rev_1", "Thank you!") }
    }
}

