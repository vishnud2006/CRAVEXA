package com.cravexa.domain.model

data class AdminCategory(
    val id: String,
    val name: String,
    val description: String,
    val productCount: Int = 0,
    val enabled: Boolean = true
)
