package com.cravexa.data.repository

import com.cravexa.core.common.Resource
import com.cravexa.data.local.PreferenceManager
import com.cravexa.domain.model.UserProfile
import com.cravexa.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val preferenceManager: PreferenceManager
) : UserRepository {

    override val currentUserProfile: Flow<UserProfile?> = preferenceManager.userProfile

    override suspend fun getUserProfile(userId: String): Resource<UserProfile> {
        val profile = preferenceManager.userProfile.firstOrNull()
        return if (profile != null && (profile.id == userId || profile.firebaseUid == userId)) {
            Resource.Success(profile)
        } else {
            Resource.Error("User profile not found.")
        }
    }

    override suspend fun saveUserProfile(profile: UserProfile): Resource<UserProfile> {
        return try {
            val updated = profile.copy(updatedAt = System.currentTimeMillis())
            preferenceManager.saveUserProfile(updated)
            Resource.Success(updated)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to save user profile.")
        }
    }

    override suspend fun updateUserProfile(profile: UserProfile): Resource<UserProfile> {
        return saveUserProfile(profile)
    }

    override suspend fun clearProfile(): Resource<Unit> {
        return try {
            preferenceManager.saveUserProfile(null)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to clear profile.")
        }
    }
}

