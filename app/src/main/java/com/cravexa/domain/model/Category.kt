package com.cravexa.domain.model

import androidx.annotation.DrawableRes

data class Category(
    val id: String,
    val name: String,
    val description: String,
    @DrawableRes val iconRes: Int? = null,
    val imageUrl: String? = null,
    val itemCount: Int = 0
)

