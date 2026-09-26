package com.cravexa.data.repository

import com.cravexa.core.common.Resource
import com.cravexa.data.local.PreferenceManager
import com.cravexa.domain.model.Product
import com.cravexa.domain.repository.WishlistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WishlistRepositoryImpl @Inject constructor(
    private val preferenceManager: PreferenceManager
) : WishlistRepository {

    override fun getWishlist(): Flow<List<Product>> = preferenceManager.wishlist

    override fun isWishlisted(productId: String): Flow<Boolean> = preferenceManager.wishlist.map { list ->
        list.any { it.id == productId }
    }

    override suspend fun toggleWishlist(product: Product): Resource<Boolean> {
        return try {
            val current = preferenceManager.wishlist.first().toMutableList()
            val exists = current.any { it.id == product.id }
            val newState: Boolean
            if (exists) {
                current.removeAll { it.id == product.id }
                newState = false
            } else {
                current.add(product)
                newState = true
            }
            preferenceManager.saveWishlist(current)
            Resource.Success(newState)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to update wishlist")
        }
    }

    override suspend fun addToWishlist(product: Product): Resource<Unit> {
        return try {
            val current = preferenceManager.wishlist.first().toMutableList()
            if (current.none { it.id == product.id }) {
                current.add(product)
                preferenceManager.saveWishlist(current)
            }
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to add to wishlist")
        }
    }

    override suspend fun removeFromWishlist(productId: String): Resource<Unit> {
        return try {
            val current = preferenceManager.wishlist.first().toMutableList()
            current.removeAll { it.id == productId }
            preferenceManager.saveWishlist(current)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to remove from wishlist")
        }
    }

    override suspend fun clearWishlist(): Resource<Unit> {
        return try {
            preferenceManager.saveWishlist(emptyList())
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to clear wishlist")
        }
    }
}
