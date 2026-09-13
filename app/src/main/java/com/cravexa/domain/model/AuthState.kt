package com.cravexa.domain.model

sealed interface AuthState {
    data object Unauthenticated : AuthState
    data object Authenticating : AuthState
    data class Authenticated(val user: UserProfile) : AuthState
    data class NeedsProfileSetup(
        val firebaseUid: String,
        val email: String? = null,
        val name: String? = null,
        val phone: String? = null,
        val role: UserRole = UserRole.CUSTOMER
    ) : AuthState
    data class AuthenticationError(val message: String) : AuthState
}

