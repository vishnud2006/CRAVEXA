package com.cravexa.data.repository

import com.cravexa.core.common.Resource
import com.cravexa.domain.model.AdminUser
import com.cravexa.domain.model.AdminUserStatus
import com.cravexa.domain.model.UserRole
import com.cravexa.domain.repository.AdminUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdminUserRepositoryImpl @Inject constructor() : AdminUserRepository {

    private val initialUsers = mutableListOf(
        AdminUser(
            id = "usr_101",
            name = "Ananya Sharma",
            email = "ananya.sharma@example.com",
            phone = "+91 9876543210",
            role = UserRole.CUSTOMER,
            status = AdminUserStatus.ACTIVE,
            createdAt = "12 Aug 2026",
            totalOrders = 8
        ),
        AdminUser(
            id = "usr_102",
            name = "Rahul Verma",
            email = "rahul.verma@example.com",
            phone = "+91 9812345678",
            role = UserRole.CUSTOMER,
            status = AdminUserStatus.ACTIVE,
            createdAt = "18 Aug 2026",
            totalOrders = 3
        ),
        AdminUser(
            id = "usr_103",
            name = "Sneha Reddy",
            email = "sneha.reddy@example.com",
            phone = "+91 9723456789",
            role = UserRole.CUSTOMER,
            status = AdminUserStatus.ACTIVE,
            createdAt = "20 Aug 2026",
            totalOrders = 5
        ),
        AdminUser(
            id = "usr_104",
            name = "Vikram Aditya",
            email = "vikram.aditya@example.com",
            phone = "+91 9634567890",
            role = UserRole.CUSTOMER,
            status = AdminUserStatus.SUSPENDED,
            createdAt = "05 Jul 2026",
            totalOrders = 1
        ),
        AdminUser(
            id = "usr_sel_1",
            name = "Lakshmi Devi",
            email = "seller@cravexa.com",
            phone = "+91 9845123456",
            role = UserRole.SELLER,
            status = AdminUserStatus.ACTIVE,
            createdAt = "01 Aug 2026",
            totalOrders = 24
        ),
        AdminUser(
            id = "usr_sel_2",
            name = "Sujata Joshi",
            email = "sujata.sweets@example.com",
            phone = "+91 9123456780",
            role = UserRole.SELLER,
            status = AdminUserStatus.ACTIVE,
            createdAt = "10 Aug 2026",
            totalOrders = 15
        ),
        AdminUser(
            id = "usr_sel_3",
            name = "Geeta Patel",
            email = "geeta.gujaratikhakhra@example.com",
            phone = "+91 9234567891",
            role = UserRole.SELLER,
            status = AdminUserStatus.ACTIVE,
            createdAt = "25 Aug 2026",
            totalOrders = 0
        )
    )

    private val _usersFlow = MutableStateFlow<List<AdminUser>>(initialUsers)
    override val usersFlow: Flow<List<AdminUser>> = _usersFlow.asStateFlow()

    override fun getUsers(): Flow<Resource<List<AdminUser>>> = flow {
        emit(Resource.Loading())
        emit(Resource.Success(_usersFlow.value.toList()))
    }

    override suspend fun updateUserStatus(userId: String, newStatus: AdminUserStatus): Resource<Unit> {
        return try {
            val list = _usersFlow.value.toMutableList()
            val index = list.indexOfFirst { it.id == userId }
            if (index != -1) {
                list[index] = list[index].copy(status = newStatus)
                _usersFlow.value = list
                Resource.Success(Unit)
            } else {
                Resource.Error("User not found.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to update user status.")
        }
    }
}
