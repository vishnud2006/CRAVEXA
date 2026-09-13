package com.cravexa.domain.model

data class HeroBanner(
    val id: String,
    val title: String,
    val subtitle: String,
    val badgeText: String,
    val categoryId: String? = null,
    val tag: String? = null
)

