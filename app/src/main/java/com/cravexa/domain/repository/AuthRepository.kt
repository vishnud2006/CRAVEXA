package com.cravexa.domain.repository

import com.cravexa.core.common.Resource
import com.cravexa.domain.model.AuthState
import com.cravexa.domain.model.UserProfile
import com.cravexa.domain.model.UserRole
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val authState: Flow<AuthState>

    suspend fun loginWithEmail(email: String, password: String): Resource<UserProfile>

    suspend fun signupWithEmail(
        name: String,
        email: String,
        password: String,
        phone: String,
        role: UserRole
    ): Resource<UserProfile>

    suspend fun sendPasswordResetEmail(email: String): Resource<Unit>

    suspend fun sendPhoneOtp(
        phone: String,
        onCodeSent: (verificationId: String) -> Unit
    ): Resource<Unit>

    suspend fun verifyPhoneOtp(
        verificationId: String,
        otpCode: String,
        role: UserRole
    ): Resource<UserProfile>

    suspend fun loginWithGoogle(
        idToken: String,
        role: UserRole
    ): Resource<UserProfile>

    suspend fun logout(): Resource<Unit>

    suspend fun getCurrentUser(): UserProfile?
}

