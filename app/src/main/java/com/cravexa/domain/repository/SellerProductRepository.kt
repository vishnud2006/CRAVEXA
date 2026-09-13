package com.cravexa.domain.repository

import com.cravexa.core.common.Resource
import com.cravexa.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface SellerProductRepository {
    val sellerProducts: Flow<List<Product>>

    fun getProducts(): Flow<Resource<List<Product>>>

    suspend fun getProductById(id: String): Resource<Product>

    suspend fun addProduct(product: Product): Resource<Product>

    suspend fun updateProduct(product: Product): Resource<Product>

    suspend fun deleteProduct(id: String): Resource<Unit>

    suspend fun updateStock(id: String, newStock: Int): Resource<Unit>

    suspend fun toggleProductAvailability(id: String, available: Boolean): Resource<Unit>
}

