package com.cravexa.data.repository

import com.cravexa.core.common.Resource
import com.cravexa.domain.model.SellerReview
import com.cravexa.domain.repository.SellerReviewRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SellerReviewRepositoryImpl @Inject constructor() : SellerReviewRepository {

    private val initialReviews = mutableListOf(
        SellerReview(
            id = "rev_1",
            orderId = "ord_seller_101",
            productId = "prod_1",
            productName = "Grandma's Andhra Avakaya Mango Pickle",
            customerName = "Ananya Sharma",
            rating = 5.0,
            comment = "Tastes exactly like how my grandmother made it in Vijayawada! The mustard sharpness and cold pressed sesame oil aroma are incredible. Definitely reordering next month!",
            date = "30 Aug 2026",
            sellerReply = "Thank you so much Ananya! We sun-cure each batch using our heirloom family recipe. So grateful for your love!",
            sellerReplyDate = "30 Aug 2026"
        ),
        SellerReview(
            id = "rev_2",
            orderId = "ord_seller_104",
            productId = "prod_2",
            productName = "Traditional Sun-Dried Gongura Thokku",
            customerName = "Rahul Verma",
            rating = 4.8,
            comment = "Super spicy and authentic Andhra flavor. Goes wonderfully with hot ghee rice and mudda pappu.",
            date = "29 Aug 2026",
            sellerReply = null,
            sellerReplyDate = null
        ),
        SellerReview(
            id = "rev_3",
            orderId = "ord_seller_103",
            productId = "prod_4",
            productName = "Grand Festive Gourmet Delicacy Hamper",
            customerName = "Sneha Reddy",
            rating = 5.0,
            comment = "Ordered this for Raksha Bandhan gift. The packaging was immaculate and eco-friendly, and sweets were fresh and melted in mouth!",
            date = "26 Aug 2026",
            sellerReply = "Warm thanks Sneha! Hope your family enjoyed the festive sweets.",
            sellerReplyDate = "27 Aug 2026"
        )
    )

    private val _reviewsFlow = MutableStateFlow<List<SellerReview>>(initialReviews)

    override fun getReviews(): Flow<Resource<List<SellerReview>>> = flow {
        emit(Resource.Loading())
        emit(Resource.Success(_reviewsFlow.value.toList()))
    }

    override suspend fun replyToReview(reviewId: String, replyText: String): Resource<Unit> {
        return try {
            val currentList = _reviewsFlow.value.toMutableList()
            val index = currentList.indexOfFirst { it.id == reviewId }
            if (index != -1) {
                val updated = currentList[index].copy(
                    sellerReply = replyText.trim(),
                    sellerReplyDate = "Today"
                )
                currentList[index] = updated
                _reviewsFlow.value = currentList
                Resource.Success(Unit)
            } else {
                Resource.Error("Review not found.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to submit review reply.")
        }
    }
}

