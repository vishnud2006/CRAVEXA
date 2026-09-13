package com.cravexa.data.repository

import com.cravexa.core.common.Resource
import com.cravexa.data.local.PreferenceManager
import com.cravexa.domain.model.CartItem
import com.cravexa.domain.model.Product
import com.cravexa.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRepositoryImpl @Inject constructor(
    private val preferenceManager: PreferenceManager
) : CartRepository {

    override fun getCartItems(): Flow<List<CartItem>> = preferenceManager.cartItems

    override fun getCartItemCount(): Flow<Int> = preferenceManager.cartItems.map { list ->
        list.sumOf { it.quantity }
    }

    override fun getCartTotal(): Flow<Double> = preferenceManager.cartItems.map { list ->
        list.sumOf { it.totalPrice }
    }

    override suspend fun addToCart(product: Product, quantity: Int): Resource<Unit> {
        return try {
            val current = preferenceManager.cartItems.first().toMutableList()
            val existingIndex = current.indexOfFirst { it.product.id == product.id }
            if (existingIndex >= 0) {
                val existing = current[existingIndex]
                current[existingIndex] = existing.copy(quantity = existing.quantity + quantity)
            } else {
                current.add(CartItem(product = product, quantity = quantity))
            }
            preferenceManager.saveCartItems(current)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to add product to cart")
        }
    }

    override suspend fun updateQuantity(productId: String, quantity: Int): Resource<Unit> {
        return try {
            val current = preferenceManager.cartItems.first().toMutableList()
            if (quantity <= 0) {
                current.removeAll { it.product.id == productId }
            } else {
                val index = current.indexOfFirst { it.product.id == productId }
                if (index >= 0) {
                    current[index] = current[index].copy(quantity = quantity)
                }
            }
            preferenceManager.saveCartItems(current)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to update quantity")
        }
    }

    override suspend fun removeFromCart(productId: String): Resource<Unit> {
        return try {
            val current = preferenceManager.cartItems.first().toMutableList()
            current.removeAll { it.product.id == productId }
            preferenceManager.saveCartItems(current)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to remove item from cart")
        }
    }

    override suspend fun clearCart(): Resource<Unit> {
        return try {
            preferenceManager.saveCartItems(emptyList())
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to clear cart")
        }
    }
}

