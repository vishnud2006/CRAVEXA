package com.cravexa.domain.usecase

import com.cravexa.core.common.Resource
import com.cravexa.domain.model.AuthState
import com.cravexa.domain.model.UserProfile
import com.cravexa.domain.model.UserRole
import com.cravexa.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Resource<UserProfile> {
        return authRepository.loginWithEmail(email, password)
    }
}

class SignupUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        name: String,
        email: String,
        password: String,
        phone: String,
        role: UserRole
    ): Resource<UserProfile> {
        return authRepository.signupWithEmail(name, email, password, phone, role)
    }
}

class ForgotPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String): Resource<Unit> {
        return authRepository.sendPasswordResetEmail(email)
    }
}

class SendPhoneOtpUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        phone: String,
        onCodeSent: (verificationId: String) -> Unit
    ): Resource<Unit> {
        return authRepository.sendPhoneOtp(phone, onCodeSent)
    }
}

class VerifyPhoneOtpUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        verificationId: String,
        otpCode: String,
        role: UserRole
    ): Resource<UserProfile> {
        return authRepository.verifyPhoneOtp(verificationId, otpCode, role)
    }
}

class GoogleSignInUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        idToken: String,
        role: UserRole
    ): Resource<UserProfile> {
        return authRepository.loginWithGoogle(idToken, role)
    }
}

class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Resource<Unit> {
        return authRepository.logout()
    }
}

class GetAuthStateUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Flow<AuthState> {
        return authRepository.authState
    }
}

class GetCurrentUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): UserProfile? {
        return authRepository.getCurrentUser()
    }
}


