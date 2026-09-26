package com.cravexa.data.repository

import com.cravexa.core.common.Resource
import com.cravexa.domain.model.AdminProduct
import com.cravexa.domain.model.AdminProductStatus
import com.cravexa.domain.repository.AdminProductModerationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdminProductModerationRepositoryImpl @Inject constructor() : AdminProductModerationRepository {

    private val initialProducts = mutableListOf(
        AdminProduct(
            id = "prod_1",
            name = "Grandma's Andhra Avakaya Mango Pickle",
            description = "Heirloom cut mango pickle with roasted mustard powder and cold-pressed gingelly oil.",
            sellerId = "sel_1",
            sellerName = "Lakshmi's Home Kitchen",
            categoryName = "Pickles",
            price = 280.0,
            stock = 15,
            weight = "500g Glass Jar",
            shelfLife = "12 Months",
            ingredients = listOf("Raw Mango", "Mustard Seed Powder", "Red Chili", "Cold Pressed Sesame Oil", "Salt"),
            storageInstructions = "Keep in a cool dry place. Use a clean dry spoon.",
            status = AdminProductStatus.APPROVED,
            createdAt = "02 Aug 2026"
        ),
        AdminProduct(
            id = "prod_2",
            name = "Traditional Sun-Dried Gongura Thokku",
            description = "Tangy sorrel leaves paste tempered with dry red chilies and garlic cloves.",
            sellerId = "sel_1",
            sellerName = "Lakshmi's Home Kitchen",
            categoryName = "Pickles",
            price = 240.0,
            stock = 8,
            weight = "350g Glass Jar",
            shelfLife = "6 Months",
            ingredients = listOf("Gongura Leaves", "Garlic", "Cumin", "Sesame Oil", "Sea Salt"),
            storageInstructions = "Refrigerate after opening.",
            status = AdminProductStatus.APPROVED,
            createdAt = "05 Aug 2026"
        ),
        AdminProduct(
            id = "prod_3",
            name = "Royal Shahi Kaju Katli (Pure Desi Ghee)",
            description = "Silky cashew diamond fudge prepared with high-grade Goan cashews and organic cane sugar.",
            sellerId = "sel_2",
            sellerName = "Malwa Heritage Sweets",
            categoryName = "Sweets",
            price = 450.0,
            stock = 20,
            weight = "400g Box",
            shelfLife = "20 Days",
            ingredients = listOf("Goan Cashew Nuts", "Organic Cane Sugar", "Pure Desi Ghee", "Cardamom"),
            storageInstructions = "Store in an airtight container.",
            status = AdminProductStatus.PENDING,
            createdAt = "28 Aug 2026"
        ),
        AdminProduct(
            id = "prod_4",
            name = "Methi & Jeera Crispy Whole Wheat Khakhra",
            description = "Crisp, roasted whole wheat flat crisps flavored with fresh fenugreek and roasted cumin.",
            sellerId = "sel_3",
            sellerName = "Ahmedabad Crisps & Khakhra",
            categoryName = "Snacks",
            price = 160.0,
            stock = 30,
            weight = "250g Vacuum Pack",
            shelfLife = "3 Months",
            ingredients = listOf("Whole Wheat Flour", "Fresh Fenugreek Leaves", "Jeera", "Groundnut Oil", "Turmeric"),
            storageInstructions = "Keep sealed in airtight pouch.",
            status = AdminProductStatus.PENDING,
            createdAt = "29 Aug 2026"
        )
    )

    private val _productsFlow = MutableStateFlow<List<AdminProduct>>(initialProducts)
    override val productsFlow: Flow<List<AdminProduct>> = _productsFlow.asStateFlow()

    override fun getProducts(): Flow<Resource<List<AdminProduct>>> = flow {
        emit(Resource.Loading())
        emit(Resource.Success(_productsFlow.value.toList()))
    }

    override suspend fun updateProductStatus(productId: String, newStatus: AdminProductStatus): Resource<Unit> {
        return try {
            val list = _productsFlow.value.toMutableList()
            val index = list.indexOfFirst { it.id == productId }
            if (index != -1) {
                list[index] = list[index].copy(status = newStatus)
                _productsFlow.value = list
                Resource.Success(Unit)
            } else {
                Resource.Error("Product not found.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to update product status.")
        }
    }
}
