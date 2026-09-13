package com.cravexa.domain.model

enum class ProductAvailability(val displayTitle: String, val isPurchasable: Boolean) {
    IN_STOCK("In Stock", true),
    LOW_STOCK("Only a Few Left", true),
    OUT_OF_STOCK("Currently Out of Stock", false),
    UNAVAILABLE("Currently Unavailable", false);

    companion object {
        fun fromStock(stock: Int, available: Boolean): ProductAvailability {
            if (!available) return UNAVAILABLE
            return when {
                stock > 5 -> IN_STOCK
                stock in 1..5 -> LOW_STOCK
                else -> OUT_OF_STOCK
            }
        }
    }
}

