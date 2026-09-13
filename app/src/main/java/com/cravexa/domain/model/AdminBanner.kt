package com.cravexa.domain.model

data class AdminBanner(
    val id: String,
    val title: String,
    val subtitle: String,
    val actionRoute: String,
    val active: Boolean = true,
    val displayOrder: Int = 1
)
