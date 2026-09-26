package com.cravexa.domain.model

data class Product(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val originalPrice: Double? = null,
    val imageUrl: String? = null,
    val images: List<String> = emptyList(),
    val sellerId: String,
    val sellerName: String,
    val sellerLocation: String = "",
    val categoryId: String,
    val categoryName: String,
    val rating: Double = 0.0,
    val reviewCount: Int = 0,
    val stock: Int = 10,
    val weight: String = "500g",
    val ingredients: List<String> = emptyList(),
    val shelfLife: String = "6 Months",
    val storageInstructions: String = "Store in a cool, dry place away from direct sunlight.",
    val region: String = "India",
    val foodType: String = "Vegetarian",
    val available: Boolean = true,
    val featured: Boolean = false,
    val trending: Boolean = false,
    val tag: String? = null
) {
    val availability: ProductAvailability
        get() = ProductAvailability.fromStock(stock, available)

    val discountPercentage: Int?
        get() = if (originalPrice != null && originalPrice > price) {
            (((originalPrice - price) / originalPrice) * 100).toInt()
        } else null
}

