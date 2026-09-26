package com.cravexa.data.repository

import com.cravexa.core.common.Resource
import com.cravexa.domain.model.Product
import com.cravexa.domain.repository.SellerProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SellerProductRepositoryImpl @Inject constructor() : SellerProductRepository {

    private val initialProducts = mutableListOf(
        Product(
            id = "prod_1",
            name = "Grandma's Andhra Avakaya Mango Pickle",
            description = "Iconic fiery Andhra Avakaya made with raw sour mango cubes, Guntur red chillies, freshly crushed mustard powder, and cold-pressed sesame oil. Aged naturally under sunlight with zero chemical preservatives.",
            price = 280.0,
            originalPrice = 320.0,
            sellerId = "sel_1",
            sellerName = "Lakshmi's Home Kitchen",
            sellerLocation = "Guntur, Andhra Pradesh",
            categoryId = "cat_pickles",
            categoryName = "Handmade Pickles",
            rating = 4.9,
            reviewCount = 142,
            stock = 25,
            weight = "500g Glass Jar",
            ingredients = listOf("Raw Mangoes", "Guntur Red Chilli", "Mustard Seeds", "Cold-Pressed Sesame Oil", "Rock Salt", "Fenugreek", "Turmeric"),
            shelfLife = "12 Months",
            storageInstructions = "Keep in a dry place. Use only dry spoons. Keep the oil layer floating on top.",
            region = "Andhra Pradesh",
            foodType = "Vegetarian",
            available = true,
            featured = true,
            trending = true,
            tag = "BESTSELLER"
        ),
        Product(
            id = "prod_2",
            name = "Traditional Sun-Dried Gongura Thokku",
            description = "Authentic tangy Andhra Gongura (sorrel leaves) chutney thokku pounded in stone with roasted garlic, red chillies, and aromatic spices. Perfect with piping hot rice and ghee.",
            price = 240.0,
            originalPrice = 270.0,
            sellerId = "sel_1",
            sellerName = "Lakshmi's Home Kitchen",
            sellerLocation = "Guntur, Andhra Pradesh",
            categoryId = "cat_pickles",
            categoryName = "Handmade Pickles",
            rating = 4.8,
            reviewCount = 89,
            stock = 4, // Low stock for testing low-stock badges
            weight = "350g Glass Jar",
            ingredients = listOf("Fresh Gongura Leaves", "Garlic", "Red Chillies", "Sesame Oil", "Mustard", "Salt", "Coriander"),
            shelfLife = "9 Months",
            storageInstructions = "Store in a cool, dry place. Refrigerate after opening for extended freshness.",
            region = "Andhra Pradesh",
            foodType = "Vegetarian",
            available = true,
            featured = false,
            trending = true,
            tag = "POPULAR"
        ),
        Product(
            id = "prod_3",
            name = "Artisanal Andhra Kandi Podi (Gunpowder)",
            description = "Heritage lentil spice mix prepared by slow roasting toor dal, chana dal, cumin, garlic, and dry chillies. Coarsely ground on traditional stone for unmatched aroma.",
            price = 180.0,
            originalPrice = 200.0,
            sellerId = "sel_1",
            sellerName = "Lakshmi's Home Kitchen",
            sellerLocation = "Guntur, Andhra Pradesh",
            categoryId = "cat_spices",
            categoryName = "Artisanal Spices & Podis",
            rating = 4.9,
            reviewCount = 115,
            stock = 0, // Out of stock for testing out-of-stock badges
            weight = "250g Pouch",
            ingredients = listOf("Toor Dal", "Chana Dal", "Dry Red Chillies", "Cumin Seeds", "Garlic", "Salt", "Hing"),
            shelfLife = "6 Months",
            storageInstructions = "Transfer to an airtight container once opened.",
            region = "Andhra Pradesh",
            foodType = "Vegetarian",
            available = true,
            featured = false,
            trending = false,
            tag = "TRADITIONAL"
        ),
        Product(
            id = "prod_4",
            name = "Grand Festive Gourmet Delicacy Hamper",
            description = "A celebratory gift box curated with homemade Avakaya pickle, Bellam Sunnundalu, and Spicy Chekkalu snack discs. Packaged in an eco-friendly handcrafted box.",
            price = 850.0,
            originalPrice = 999.0,
            sellerId = "sel_1",
            sellerName = "Lakshmi's Home Kitchen",
            sellerLocation = "Guntur, Andhra Pradesh",
            categoryId = "cat_hampers",
            categoryName = "Gift Hampers",
            rating = 5.0,
            reviewCount = 31,
            stock = 12,
            weight = "800g Combo Pack",
            ingredients = listOf("Mango Pickle (250g)", "Sunnundalu (300g)", "Chekkalu (250g)"),
            shelfLife = "3 Months",
            storageInstructions = "Store separate items in cool airtight containers.",
            region = "Andhra Pradesh",
            foodType = "Vegetarian",
            available = false, // Disabled for testing disabled toggle
            featured = false,
            trending = false,
            tag = "LUXURY HAMPER"
        )
    )

    private val _sellerProductsFlow = MutableStateFlow<List<Product>>(initialProducts)
    override val sellerProducts: Flow<List<Product>> = _sellerProductsFlow.asStateFlow()

    override fun getProducts(): Flow<Resource<List<Product>>> = flow {
        emit(Resource.Loading())
        emit(Resource.Success(_sellerProductsFlow.value.toList()))
    }

    override suspend fun getProductById(id: String): Resource<Product> {
        val product = _sellerProductsFlow.value.find { it.id == id }
        return if (product != null) {
            Resource.Success(product)
        } else {
            Resource.Error("Product not found.")
        }
    }

    override suspend fun addProduct(product: Product): Resource<Product> {
        return try {
            val newId = if (product.id.isBlank()) "prod_${System.currentTimeMillis()}" else product.id
            val newProduct = product.copy(id = newId)
            val currentList = _sellerProductsFlow.value.toMutableList()
            currentList.add(0, newProduct)
            _sellerProductsFlow.value = currentList
            Resource.Success(newProduct)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to add product.")
        }
    }

    override suspend fun updateProduct(product: Product): Resource<Product> {
        return try {
            val currentList = _sellerProductsFlow.value.toMutableList()
            val index = currentList.indexOfFirst { it.id == product.id }
            if (index != -1) {
                currentList[index] = product
                _sellerProductsFlow.value = currentList
                Resource.Success(product)
            } else {
                Resource.Error("Product not found for update.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to update product.")
        }
    }

    override suspend fun deleteProduct(id: String): Resource<Unit> {
        return try {
            val currentList = _sellerProductsFlow.value.toMutableList()
            val removed = currentList.removeAll { it.id == id }
            if (removed) {
                _sellerProductsFlow.value = currentList
                Resource.Success(Unit)
            } else {
                Resource.Error("Product not found.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to delete product.")
        }
    }

    override suspend fun updateStock(id: String, newStock: Int): Resource<Unit> {
        return try {
            val safeStock = newStock.coerceAtLeast(0)
            val currentList = _sellerProductsFlow.value.toMutableList()
            val index = currentList.indexOfFirst { it.id == id }
            if (index != -1) {
                currentList[index] = currentList[index].copy(stock = safeStock)
                _sellerProductsFlow.value = currentList
                Resource.Success(Unit)
            } else {
                Resource.Error("Product not found.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to update stock.")
        }
    }

    override suspend fun toggleProductAvailability(id: String, available: Boolean): Resource<Unit> {
        return try {
            val currentList = _sellerProductsFlow.value.toMutableList()
            val index = currentList.indexOfFirst { it.id == id }
            if (index != -1) {
                currentList[index] = currentList[index].copy(available = available)
                _sellerProductsFlow.value = currentList
                Resource.Success(Unit)
            } else {
                Resource.Error("Product not found.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to update availability.")
        }
    }
}

