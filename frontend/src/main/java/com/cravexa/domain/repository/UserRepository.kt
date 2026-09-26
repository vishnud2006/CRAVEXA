package com.cravexa.domain.repository

import com.cravexa.core.common.Resource
import com.cravexa.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    val currentUserProfile: Flow<UserProfile?>

    suspend fun getUserProfile(userId: String): Resource<UserProfile>

    suspend fun saveUserProfile(profile: UserProfile): Resource<UserProfile>

    suspend fun updateUserProfile(profile: UserProfile): Resource<UserProfile>

    suspend fun clearProfile(): Resource<Unit>
}

