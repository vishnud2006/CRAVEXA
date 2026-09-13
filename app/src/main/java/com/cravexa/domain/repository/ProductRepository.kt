package com.cravexa.domain.repository

import com.cravexa.core.common.Resource
import com.cravexa.domain.model.HeroBanner
import com.cravexa.domain.model.Product
import com.cravexa.domain.model.SearchFilter
import com.cravexa.domain.model.SortOption
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getHeroBanners(): Flow<List<HeroBanner>>
    fun getFeaturedProducts(): Flow<Resource<List<Product>>>
    fun getTrendingProducts(): Flow<Resource<List<Product>>>
    fun getRecommendedProducts(): Flow<Resource<List<Product>>>
    fun getRegionalSpecialties(): Flow<Resource<List<Product>>>
    fun getProductsByCategory(categoryId: String): Flow<Resource<List<Product>>>
    fun getProductById(productId: String): Flow<Resource<Product>>
    fun getProductsBySeller(sellerId: String): Flow<Resource<List<Product>>>
    fun getRelatedProducts(productId: String, categoryId: String): Flow<Resource<List<Product>>>
    fun searchProducts(query: String, filter: SearchFilter? = null, sort: SortOption = SortOption.RELEVANCE): Flow<Resource<List<Product>>>
    fun getSearchSuggestions(query: String): Flow<List<String>>
}

