package com.cravexa.data.repository

import com.cravexa.core.common.Resource
import com.cravexa.data.local.PreferenceManager
import com.cravexa.domain.model.CustomerProfile
import com.cravexa.domain.model.UserProfile
import com.cravexa.domain.repository.CustomerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CustomerRepositoryImpl @Inject constructor(
    private val preferenceManager: PreferenceManager
) : CustomerRepository {

    override val currentCustomerProfile: Flow<CustomerProfile?> = preferenceManager.userProfile.map { user ->
        user?.let {
            CustomerProfile(
                id = it.id,
                firebaseUid = it.firebaseUid,
                name = it.name,
                email = it.email,
                phone = it.phone,
                profileImageUrl = it.profileImage,
                memberSince = "August 2026",
                isPhoneVerified = it.phone.isNotBlank(),
                isEmailVerified = it.email.isNotBlank(),
                createdAt = it.createdAt,
                updatedAt = it.updatedAt
            )
        }
    }

    override suspend fun getCustomerProfile(): Resource<CustomerProfile> {
        val user = preferenceManager.userProfile.firstOrNull()
        return if (user != null) {
            Resource.Success(
                CustomerProfile(
                    id = user.id,
                    firebaseUid = user.firebaseUid,
                    name = user.name,
                    email = user.email,
                    phone = user.phone,
                    profileImageUrl = user.profileImage,
                    memberSince = "August 2026",
                    isPhoneVerified = user.phone.isNotBlank(),
                    isEmailVerified = user.email.isNotBlank(),
                    createdAt = user.createdAt,
                    updatedAt = user.updatedAt
                )
            )
        } else {
            Resource.Error("Customer profile not found.")
        }
    }

    override suspend fun updateCustomerProfile(profile: CustomerProfile): Resource<CustomerProfile> {
        return try {
            val existingUser = preferenceManager.userProfile.firstOrNull() ?: UserProfile()
            val updatedUser = existingUser.copy(
                name = profile.name,
                email = profile.email,
                phone = profile.phone,
                profileImage = profile.profileImageUrl,
                profileCompleted = true,
                updatedAt = System.currentTimeMillis()
            )
            preferenceManager.saveUserProfile(updatedUser)
            val updatedProfile = profile.copy(updatedAt = updatedUser.updatedAt)
            Resource.Success(updatedProfile)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to update profile.")
        }
    }
}

