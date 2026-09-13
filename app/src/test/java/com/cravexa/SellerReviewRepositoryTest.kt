package com.cravexa

import com.cravexa.core.common.Resource
import com.cravexa.data.repository.SellerReviewRepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SellerReviewRepositoryTest {

    private lateinit var repository: SellerReviewRepositoryImpl

    @Before
    fun setUp() {
        repository = SellerReviewRepositoryImpl()
    }

    @Test
    fun `getReviews returns initial customer reviews`() = runTest {
        val resource = repository.getReviews().first { it is Resource.Success }
        assertTrue(resource is Resource.Success)

        val reviews = (resource as Resource.Success).data
        assertTrue(!reviews.isNullOrEmpty())
        assertEquals("rev_1", reviews!!.first().id)
    }

    @Test
    fun `replyToReview updates reply field on review`() = runTest {
        val result = repository.replyToReview("rev_2", "Thank you for the warm feedback!")
        assertTrue(result is Resource.Success)

        val resource = repository.getReviews().first { it is Resource.Success }
        val reviews = (resource as Resource.Success).data
        val rev2 = reviews?.find { it.id == "rev_2" }
        assertEquals("Thank you for the warm feedback!", rev2?.sellerReply)
    }
}

