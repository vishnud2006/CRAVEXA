package com.cravexa.domain.repository

import com.cravexa.core.common.Resource
import com.cravexa.domain.model.CartItem
import com.cravexa.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    fun getCartItems(): Flow<List<CartItem>>
    fun getCartItemCount(): Flow<Int>
    fun getCartTotal(): Flow<Double>
    suspend fun addToCart(product: Product, quantity: Int = 1): Resource<Unit>
    suspend fun updateQuantity(productId: String, quantity: Int): Resource<Unit>
    suspend fun removeFromCart(productId: String): Resource<Unit>
    suspend fun clearCart(): Resource<Unit>
}

