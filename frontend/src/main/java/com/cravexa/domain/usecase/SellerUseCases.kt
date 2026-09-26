package com.cravexa.domain.usecase

import android.net.Uri
import com.cravexa.core.common.Resource
import com.cravexa.domain.model.Order
import com.cravexa.domain.model.OrderStatus
import com.cravexa.domain.model.Product
import com.cravexa.domain.model.SellerDashboardStats
import com.cravexa.domain.model.SellerPayout
import com.cravexa.domain.model.SellerProfile
import com.cravexa.domain.model.SellerReview
import com.cravexa.domain.repository.ImageUploadRepository
import com.cravexa.domain.repository.SellerEarningsRepository
import com.cravexa.domain.repository.SellerOrderRepository
import com.cravexa.domain.repository.SellerProductRepository
import com.cravexa.domain.repository.SellerRepository
import com.cravexa.domain.repository.SellerReviewRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// Profile & Compliance Use Cases
class GetSellerProfileUseCase @Inject constructor(
    private val sellerRepository: SellerRepository
) {
    suspend operator fun invoke(): Resource<SellerProfile> {
        return sellerRepository.getSellerProfile()
    }

    fun observe(): Flow<SellerProfile?> {
        return sellerRepository.currentSellerProfile
    }
}

class UpdateSellerProfileUseCase @Inject constructor(
    private val sellerRepository: SellerRepository
) {
    suspend operator fun invoke(profile: SellerProfile): Resource<SellerProfile> {
        return sellerRepository.updateSellerProfile(profile)
    }
}

class SubmitFssaiUseCase @Inject constructor(
    private val sellerRepository: SellerRepository
) {
    suspend operator fun invoke(fssaiNumber: String, documentUrl: String? = null): Resource<SellerProfile> {
        return sellerRepository.submitFssai(fssaiNumber, documentUrl)
    }
}

// Dashboard & Analytics Use Case
class GetSellerDashboardUseCase @Inject constructor(
    private val earningsRepository: SellerEarningsRepository,
    private val sellerRepository: SellerRepository
) {
    operator fun invoke(): Flow<Resource<SellerDashboardStats>> = earningsRepository.getDashboardStats()
    fun getProfile(): Flow<SellerProfile?> = sellerRepository.currentSellerProfile
}

// Product Management Use Cases
class GetSellerKitchenProductsUseCase @Inject constructor(
    private val productRepository: SellerProductRepository
) {
    operator fun invoke(): Flow<Resource<List<Product>>> = productRepository.getProducts()
    val productsFlow: Flow<List<Product>> = productRepository.sellerProducts
}

class GetSellerProductByIdUseCase @Inject constructor(
    private val productRepository: SellerProductRepository
) {
    suspend operator fun invoke(id: String): Resource<Product> = productRepository.getProductById(id)
}

class AddSellerProductUseCase @Inject constructor(
    private val productRepository: SellerProductRepository
) {
    suspend operator fun invoke(product: Product): Resource<Product> = productRepository.addProduct(product)
}

class UpdateSellerProductUseCase @Inject constructor(
    private val productRepository: SellerProductRepository
) {
    suspend operator fun invoke(product: Product): Resource<Product> = productRepository.updateProduct(product)
}

class DeleteSellerProductUseCase @Inject constructor(
    private val productRepository: SellerProductRepository
) {
    suspend operator fun invoke(id: String): Resource<Unit> = productRepository.deleteProduct(id)
}

class UpdateProductStockUseCase @Inject constructor(
    private val productRepository: SellerProductRepository
) {
    suspend operator fun invoke(id: String, newStock: Int): Resource<Unit> =
        productRepository.updateStock(id, newStock)
}

class ToggleProductAvailabilityUseCase @Inject constructor(
    private val productRepository: SellerProductRepository
) {
    suspend operator fun invoke(id: String, available: Boolean): Resource<Unit> =
        productRepository.toggleProductAvailability(id, available)
}

// Order Management Use Cases
class GetSellerOrdersUseCase @Inject constructor(
    private val orderRepository: SellerOrderRepository
) {
    operator fun invoke(): Flow<Resource<List<Order>>> = orderRepository.getOrders()
    val ordersFlow: Flow<List<Order>> = orderRepository.sellerOrders
}

class GetSellerOrderDetailUseCase @Inject constructor(
    private val orderRepository: SellerOrderRepository
) {
    suspend operator fun invoke(orderId: String): Resource<Order> = orderRepository.getOrderById(orderId)
}

class UpdateSellerOrderStatusUseCase @Inject constructor(
    private val orderRepository: SellerOrderRepository
) {
    suspend operator fun invoke(orderId: String, newStatus: OrderStatus): Resource<Order> =
        orderRepository.updateOrderStatus(orderId, newStatus)
}

// Earnings & Payouts Use Cases
class GetSellerEarningsUseCase @Inject constructor(
    private val earningsRepository: SellerEarningsRepository
) {
    operator fun invoke(): Flow<Resource<SellerDashboardStats>> = earningsRepository.getDashboardStats()
}

class GetSellerPayoutsUseCase @Inject constructor(
    private val earningsRepository: SellerEarningsRepository
) {
    operator fun invoke(): Flow<Resource<List<SellerPayout>>> = earningsRepository.getPayoutHistory()
}

// Reviews Use Cases
class GetSellerReviewsUseCase @Inject constructor(
    private val reviewRepository: SellerReviewRepository
) {
    operator fun invoke(): Flow<Resource<List<SellerReview>>> = reviewRepository.getReviews()
}

class ReplyToReviewUseCase @Inject constructor(
    private val reviewRepository: SellerReviewRepository
) {
    suspend operator fun invoke(reviewId: String, replyText: String): Resource<Unit> =
        reviewRepository.replyToReview(reviewId, replyText)
}

// Image Upload Use Case
class UploadProductImageUseCase @Inject constructor(
    private val imageUploadRepository: ImageUploadRepository
) {
    suspend fun uploadSingle(uri: Uri): Resource<String> = imageUploadRepository.uploadProductImage(uri)
    suspend fun uploadMultiple(uris: List<Uri>): Resource<List<String>> = imageUploadRepository.uploadProductImages(uris)
}
