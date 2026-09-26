package com.cravexa.presentation.seller.reviews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cravexa.core.common.Resource
import com.cravexa.domain.model.SellerReview
import com.cravexa.domain.usecase.GetSellerReviewsUseCase
import com.cravexa.domain.usecase.ReplyToReviewUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface SellerReviewsUiState {
    data object Loading : SellerReviewsUiState
    data class Success(
        val reviews: List<SellerReview>,
        val averageRating: Double,
        val totalReviews: Int
    ) : SellerReviewsUiState
    data class Error(val message: String) : SellerReviewsUiState
}

@HiltViewModel
class SellerReviewsViewModel @Inject constructor(
    private val getSellerReviewsUseCase: GetSellerReviewsUseCase,
    private val replyToReviewUseCase: ReplyToReviewUseCase
) : ViewModel() {

    private val _replyDialogReview = MutableStateFlow<SellerReview?>(null)
    val replyDialogReview: StateFlow<SellerReview?> = _replyDialogReview.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    val uiState: StateFlow<SellerReviewsUiState> = getSellerReviewsUseCase()
        .combine(_replyDialogReview) { reviewsRes, _ ->
            when (reviewsRes) {
                is Resource.Loading -> SellerReviewsUiState.Loading
                is Resource.Error -> SellerReviewsUiState.Error(reviewsRes.message ?: "Failed to load reviews.")
                is Resource.Success -> {
                    val reviews = reviewsRes.data ?: emptyList()
                    val avg = if (reviews.isNotEmpty()) reviews.map { it.rating }.average() else 0.0
                    SellerReviewsUiState.Success(
                        reviews = reviews,
                        averageRating = (Math.round(avg * 10.0) / 10.0),
                        totalReviews = reviews.size
                    )
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SellerReviewsUiState.Loading
        )

    fun openReplyDialog(review: SellerReview) {
        _replyDialogReview.value = review
    }

    fun closeReplyDialog() {
        _replyDialogReview.value = null
    }

    fun submitReply(reviewId: String, replyText: String) {
        if (replyText.isBlank()) return

        viewModelScope.launch {
            when (val result = replyToReviewUseCase(reviewId, replyText)) {
                is Resource.Success -> {
                    _replyDialogReview.value = null
                    _toastMessage.value = "Reply sent to customer."
                }
                is Resource.Error -> {
                    _toastMessage.value = result.message ?: "Failed to submit reply."
                }
                else -> Unit
            }
        }
    }

    fun clearToast() {
        _toastMessage.value = null
    }
}

