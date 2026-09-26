package com.cravexa.domain.usecase

import com.cravexa.core.common.Resource
import com.cravexa.domain.model.CartItem
import com.cravexa.domain.model.Category
import com.cravexa.domain.model.HeroBanner
import com.cravexa.domain.model.Product
import com.cravexa.domain.model.SearchFilter
import com.cravexa.domain.model.SortOption
import com.cravexa.domain.repository.CartRepository
import com.cravexa.domain.repository.CategoryRepository
import com.cravexa.domain.repository.ProductRepository
import com.cravexa.domain.repository.RecentSearchRepository
import com.cravexa.domain.repository.WishlistRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHomeMarketplaceUseCase @Inject constructor(
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository
) {
    fun getHeroBanners(): Flow<List<HeroBanner>> = productRepository.getHeroBanners()
    fun getFeaturedProducts(): Flow<Resource<List<Product>>> = productRepository.getFeaturedProducts()
    fun getTrendingProducts(): Flow<Resource<List<Product>>> = productRepository.getTrendingProducts()
    fun getRecommendedProducts(): Flow<Resource<List<Product>>> = productRepository.getRecommendedProducts()
    fun getRegionalSpecialties(): Flow<Resource<List<Product>>> = productRepository.getRegionalSpecialties()
    fun getCategories(): Flow<Resource<List<Category>>> = categoryRepository.getCategories()
}

class GetCategoriesUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    operator fun invoke(): Flow<Resource<List<Category>>> = categoryRepository.getCategories()
    fun getCategoryById(categoryId: String): Flow<Resource<Category>> = categoryRepository.getCategoryById(categoryId)
}

class GetCategoryProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    operator fun invoke(categoryId: String): Flow<Resource<List<Product>>> = productRepository.getProductsByCategory(categoryId)
}

class GetProductDetailUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    operator fun invoke(productId: String): Flow<Resource<Product>> = productRepository.getProductById(productId)
    fun getRelatedProducts(productId: String, categoryId: String): Flow<Resource<List<Product>>> =
        productRepository.getRelatedProducts(productId, categoryId)
}

class GetSellerProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    operator fun invoke(sellerId: String): Flow<Resource<List<Product>>> = productRepository.getProductsBySeller(sellerId)
}

class SearchProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    operator fun invoke(
        query: String,
        filter: SearchFilter? = null,
        sort: SortOption = SortOption.RELEVANCE
    ): Flow<Resource<List<Product>>> = productRepository.searchProducts(query, filter, sort)

    fun getSuggestions(query: String): Flow<List<String>> = productRepository.getSearchSuggestions(query)
}

class GetWishlistUseCase @Inject constructor(
    private val wishlistRepository: WishlistRepository
) {
    operator fun invoke(): Flow<List<Product>> = wishlistRepository.getWishlist()
    fun isWishlisted(productId: String): Flow<Boolean> = wishlistRepository.isWishlisted(productId)
}

class ToggleWishlistUseCase @Inject constructor(
    private val wishlistRepository: WishlistRepository
) {
    suspend operator fun invoke(product: Product): Resource<Boolean> = wishlistRepository.toggleWishlist(product)
}

class GetCartUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    fun getCartItems(): Flow<List<CartItem>> = cartRepository.getCartItems()
    fun getCartItemCount(): Flow<Int> = cartRepository.getCartItemCount()
    fun getCartTotal(): Flow<Double> = cartRepository.getCartTotal()
}

class AddToCartUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(product: Product, quantity: Int = 1): Resource<Unit> =
        cartRepository.addToCart(product, quantity)
}

class UpdateCartQuantityUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(productId: String, quantity: Int): Resource<Unit> =
        cartRepository.updateQuantity(productId, quantity)
}

class RemoveFromCartUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(productId: String): Resource<Unit> =
        cartRepository.removeFromCart(productId)
}

class ClearCartUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(): Resource<Unit> =
        cartRepository.clearCart()
}

class RecentSearchUseCases @Inject constructor(
    private val recentSearchRepository: RecentSearchRepository
) {
    fun getRecentSearches(): Flow<List<String>> = recentSearchRepository.getRecentSearches()
    suspend fun addRecentSearch(query: String) = recentSearchRepository.addRecentSearch(query)
    suspend fun removeRecentSearch(query: String) = recentSearchRepository.removeRecentSearch(query)
    suspend fun clearRecentSearches() = recentSearchRepository.clearRecentSearches()
}

