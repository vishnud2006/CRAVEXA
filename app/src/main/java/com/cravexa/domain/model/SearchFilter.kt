package com.cravexa.domain.model

data class SearchFilter(
    val categoryId: String? = null,
    val minPrice: Double? = null,
    val maxPrice: Double? = null,
    val minRating: Double? = null,
    val region: String? = null,
    val foodType: String? = null,
    val inStockOnly: Boolean = false
) {
    val isActive: Boolean
        get() = categoryId != null ||
                minPrice != null ||
                maxPrice != null ||
                minRating != null ||
                region != null ||
                foodType != null ||
                inStockOnly
}

