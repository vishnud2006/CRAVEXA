package com.cravexa.domain.usecase

import com.cravexa.core.common.Resource
import com.cravexa.domain.model.UserProfile
import com.cravexa.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SaveUserProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(profile: UserProfile): Resource<UserProfile> {
        return userRepository.saveUserProfile(profile)
    }
}

class UpdateUserProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(profile: UserProfile): Resource<UserProfile> {
        return userRepository.updateUserProfile(profile)
    }
}

class GetUserProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userId: String): Resource<UserProfile> {
        return userRepository.getUserProfile(userId)
    }

    fun observe(): Flow<UserProfile?> {
        return userRepository.currentUserProfile
    }
}

