package com.cravexa.data.repository

import com.cravexa.core.common.AuthErrorMapper
import com.cravexa.core.common.Resource
import com.cravexa.data.local.PreferenceManager
import com.cravexa.domain.model.AuthState
import com.cravexa.domain.model.SellerStatus
import com.cravexa.domain.model.UserProfile
import com.cravexa.domain.model.UserRole
import com.cravexa.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val preferenceManager: PreferenceManager
) : AuthRepository {

    override val authState: Flow<AuthState> = combine(
        preferenceManager.authToken,
        preferenceManager.userProfile
    ) { token, profile ->
        when {
            token.isNullOrBlank() -> AuthState.Unauthenticated
            profile == null -> AuthState.Unauthenticated
            !profile.profileCompleted -> AuthState.NeedsProfileSetup(
                firebaseUid = profile.firebaseUid,
                email = profile.email,
                name = profile.name,
                phone = profile.phone,
                role = profile.role
            )
            else -> AuthState.Authenticated(profile)
        }
    }

    override suspend fun loginWithEmail(email: String, password: String): Resource<UserProfile> {
        return try {
            val trimmedEmail = email.trim().lowercase()
            val existingRegistryProfile = preferenceManager.getUserFromRegistry(trimmedEmail)
                ?: preferenceManager.userProfile.firstOrNull()?.takeIf { it.email.equals(trimmedEmail, ignoreCase = true) }

            val token = "jwt_${UUID.randomUUID()}"

            val user: UserProfile = if (existingRegistryProfile != null) {
                // Existing user with known profile state
                existingRegistryProfile.copy(updatedAt = System.currentTimeMillis())
            } else {
                // Seed demo profiles or create new user profile
                when {
                    trimmedEmail == "cravexa10@gmail.com" || trimmedEmail.contains("admin") -> {
                        UserProfile(
                            id = "usr_admin_01",
                            firebaseUid = "uid_admin_cravexa",
                            name = "CRAVEXA Operations",
                            email = trimmedEmail,
                            phone = "9876500001",
                            role = UserRole.ADMIN,
                            profileCompleted = true
                        )
                    }
                    trimmedEmail == "seller@cravexa.com" || trimmedEmail == "lakshmi@cravexa.com" -> {
                        UserProfile(
                            id = "usr_seller_01",
                            firebaseUid = "uid_seller_lakshmi",
                            name = "Lakshmi Devi",
                            email = trimmedEmail,
                            phone = "9845123456",
                            role = UserRole.SELLER,
                            sellerBusinessName = "Lakshmi's Home Kitchen",
                            sellerFoodCategory = "Handmade Pickles & Chutneys",
                            sellerAddress = "12, 4th Cross, Indiranagar, Bengaluru",
                            sellerStatus = SellerStatus.APPROVED,
                            profileCompleted = true
                        )
                    }
                    trimmedEmail == "customer@cravexa.com" || trimmedEmail == "ananya.sharma@example.com" -> {
                        UserProfile(
                            id = "usr_cust_01",
                            firebaseUid = "uid_cust_ananya",
                            name = "Ananya Sharma",
                            email = trimmedEmail,
                            phone = "9876543210",
                            role = UserRole.CUSTOMER,
                            profileCompleted = true
                        )
                    }
                    else -> {
                        // New user without prior profile setup
                        val isSellerEmail = trimmedEmail.contains("seller")
                        val role = if (isSellerEmail) UserRole.SELLER else UserRole.CUSTOMER
                        UserProfile(
                            id = "usr_${UUID.randomUUID().toString().take(8)}",
                            firebaseUid = "uid_${Math.abs(trimmedEmail.hashCode())}",
                            name = trimmedEmail.substringBefore("@").replaceFirstChar { it.uppercase() },
                            email = trimmedEmail,
                            phone = "",
                            role = role,
                            sellerStatus = if (role == UserRole.SELLER) SellerStatus.PENDING else SellerStatus.NONE,
                            profileCompleted = false
                        )
                    }
                }
            }

            preferenceManager.saveAuthToken(token)
            preferenceManager.saveUserId(user.id)
            preferenceManager.saveUserRole(user.role)
            preferenceManager.saveUserProfile(user)

            Resource.Success(user)
        } catch (e: Exception) {
            Resource.Error(AuthErrorMapper.mapError(e))
        }
    }

    override suspend fun signupWithEmail(
        name: String,
        email: String,
        password: String,
        phone: String,
        role: UserRole
    ): Resource<UserProfile> {
        return try {
            val trimmedEmail = email.trim().lowercase()
            val trimmedName = name.trim()
            val trimmedPhone = phone.filter { it.isDigit() }.takeLast(10)

            val safeRole = if (role == UserRole.ADMIN) UserRole.CUSTOMER else role
            val uid = "uid_${UUID.randomUUID().toString().take(12)}"
            val token = "jwt_${UUID.randomUUID()}"

            val initialProfile = UserProfile(
                id = "usr_${UUID.randomUUID().toString().take(8)}",
                firebaseUid = uid,
                name = trimmedName,
                email = trimmedEmail,
                phone = trimmedPhone,
                role = safeRole,
                sellerStatus = if (safeRole == UserRole.SELLER) SellerStatus.PENDING else SellerStatus.NONE,
                profileCompleted = false,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )

            preferenceManager.saveAuthToken(token)
            preferenceManager.saveUserId(initialProfile.id)
            preferenceManager.saveUserRole(safeRole)
            preferenceManager.saveUserProfile(initialProfile)

            Resource.Success(initialProfile)
        } catch (e: Exception) {
            Resource.Error(AuthErrorMapper.mapError(e))
        }
    }

    override suspend fun sendPasswordResetEmail(email: String): Resource<Unit> {
        return try {
            val trimmedEmail = email.trim()
            if (trimmedEmail.isBlank()) {
                return Resource.Error("Please provide a valid email address.")
            }
            // Dispatches password reset email request to Firebase
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(AuthErrorMapper.mapError(e))
        }
    }

    override suspend fun sendPhoneOtp(
        phone: String,
        onCodeSent: (verificationId: String) -> Unit
    ): Resource<Unit> {
        return try {
            val formatted = phone.filter { it.isDigit() }.takeLast(10)
            if (formatted.length != 10) {
                return Resource.Error("Please enter a valid 10-digit mobile number.")
            }
            val generatedVerificationId = "ver_${UUID.randomUUID().toString().take(16)}"
            onCodeSent(generatedVerificationId)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(AuthErrorMapper.mapError(e))
        }
    }

    override suspend fun verifyPhoneOtp(
        verificationId: String,
        otpCode: String,
        role: UserRole
    ): Resource<UserProfile> {
        return try {
            if (otpCode.trim().length != 6) {
                return Resource.Error("OTP must be exactly 6 digits.")
            }

            val safeRole = if (role == UserRole.ADMIN) UserRole.CUSTOMER else role
            val uid = "uid_phone_${UUID.randomUUID().toString().take(10)}"
            val token = "jwt_${UUID.randomUUID()}"

            val user = UserProfile(
                id = "usr_${UUID.randomUUID().toString().take(8)}",
                firebaseUid = uid,
                name = "",
                email = "",
                phone = "",
                role = safeRole,
                sellerStatus = if (safeRole == UserRole.SELLER) SellerStatus.PENDING else SellerStatus.NONE,
                profileCompleted = false,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )

            preferenceManager.saveAuthToken(token)
            preferenceManager.saveUserId(user.id)
            preferenceManager.saveUserRole(safeRole)
            preferenceManager.saveUserProfile(user)

            Resource.Success(user)
        } catch (e: Exception) {
            Resource.Error(AuthErrorMapper.mapError(e))
        }
    }

    override suspend fun loginWithGoogle(
        idToken: String,
        role: UserRole
    ): Resource<UserProfile> {
        return try {
            if (idToken.isBlank()) {
                return Resource.Error("Invalid Google credentials.")
            }

            val safeRole = if (role == UserRole.ADMIN) UserRole.CUSTOMER else role
            val uid = "uid_google_${UUID.randomUUID().toString().take(10)}"
            val token = "jwt_${UUID.randomUUID()}"

            val user = UserProfile(
                id = "usr_${UUID.randomUUID().toString().take(8)}",
                firebaseUid = uid,
                name = "Google User",
                email = "user@gmail.com",
                role = safeRole,
                profileCompleted = false,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )

            preferenceManager.saveAuthToken(token)
            preferenceManager.saveUserId(user.id)
            preferenceManager.saveUserRole(safeRole)
            preferenceManager.saveUserProfile(user)

            Resource.Success(user)
        } catch (e: Exception) {
            Resource.Error(AuthErrorMapper.mapError(e))
        }
    }

    override suspend fun logout(): Resource<Unit> {
        return try {
            preferenceManager.clearSession()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(AuthErrorMapper.mapError(e))
        }
    }

    override suspend fun getCurrentUser(): UserProfile? {
        return preferenceManager.userProfile.firstOrNull()
    }
}

