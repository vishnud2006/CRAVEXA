package com.cravexa.domain.model

enum class AdminUserStatus(val displayTitle: String) {
    ACTIVE("Active"),
    SUSPENDED("Suspended"),
    DEACTIVATED("Deactivated")
}

data class AdminUser(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val role: UserRole,
    val status: AdminUserStatus = AdminUserStatus.ACTIVE,
    val createdAt: String,
    val totalOrders: Int = 0
)
