package com.cravexa.domain.repository

import com.cravexa.core.common.Resource
import com.cravexa.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface WishlistRepository {
    fun getWishlist(): Flow<List<Product>>
    fun isWishlisted(productId: String): Flow<Boolean>
    suspend fun toggleWishlist(product: Product): Resource<Boolean>
    suspend fun addToWishlist(product: Product): Resource<Unit>
    suspend fun removeFromWishlist(productId: String): Resource<Unit>
    suspend fun clearWishlist(): Resource<Unit>
}

