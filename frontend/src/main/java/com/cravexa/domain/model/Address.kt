package com.cravexa.domain.model

data class Address(
    val id: String = "",
    val userId: String = "",
    val fullName: String = "",
    val phone: String = "",
    val houseBuilding: String = "",
    val street: String = "",
    val area: String = "",
    val city: String = "",
    val state: String = "",
    val pincode: String = "",
    val landmark: String? = null,
    val addressType: AddressType = AddressType.HOME,
    val isDefault: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
) {
    val formattedAddress: String
        get() {
            val parts = listOfNotNull(
                houseBuilding.takeIf { it.isNotBlank() },
                street.takeIf { it.isNotBlank() },
                area.takeIf { it.isNotBlank() },
                landmark?.takeIf { it.isNotBlank() }?.let { "Near $it" },
                "$city, $state - $pincode".takeIf { city.isNotBlank() }
            )
            return parts.joinToString(", ")
        }
}

