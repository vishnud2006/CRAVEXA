package com.cravexa.domain.repository

import com.cravexa.core.common.Resource
import com.cravexa.domain.model.SellerReview
import kotlinx.coroutines.flow.Flow

interface SellerReviewRepository {
    fun getReviews(): Flow<Resource<List<SellerReview>>>
    suspend fun replyToReview(reviewId: String, replyText: String): Resource<Unit>
}
