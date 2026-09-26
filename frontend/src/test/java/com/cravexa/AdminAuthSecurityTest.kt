package com.cravexa

import com.cravexa.core.common.Resource
import com.cravexa.data.local.PreferenceManager
import com.cravexa.data.repository.AdminSettingsRepositoryImpl
import com.cravexa.data.repository.AuthRepositoryImpl
import com.cravexa.domain.model.UserRole
import com.cravexa.domain.usecase.AdminChangePasswordUseCase
import com.cravexa.domain.usecase.CheckAdminMustChangePasswordUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class AdminAuthSecurityTest {

    private val preferenceManager = mockk<PreferenceManager>(relaxed = true)
    private lateinit var authRepository: AuthRepositoryImpl
    private lateinit var settingsRepository: AdminSettingsRepositoryImpl
    private lateinit var checkAdminMustChangePasswordUseCase: CheckAdminMustChangePasswordUseCase
    private lateinit var adminChangePasswordUseCase: AdminChangePasswordUseCase

    @Before
    fun setup() {
        every { preferenceManager.authToken } returns flowOf(null)
        every { preferenceManager.userProfile } returns flowOf(null)
        every { preferenceManager.userRole } returns flowOf(UserRole.CUSTOMER)
        every { preferenceManager.usersRegistry } returns flowOf(emptyMap())
        io.mockk.coEvery { preferenceManager.getUserFromRegistry(any()) } returns null

        authRepository = AuthRepositoryImpl(preferenceManager)
        settingsRepository = AdminSettingsRepositoryImpl()
        checkAdminMustChangePasswordUseCase = CheckAdminMustChangePasswordUseCase(settingsRepository)
        adminChangePasswordUseCase = AdminChangePasswordUseCase(settingsRepository)
    }

    @Test
    fun `public signup rejects ADMIN role and defaults to CUSTOMER`() = runTest {
        val result = authRepository.signupWithEmail(
            name = "Test Hacker",
            email = "hacker@example.com",
            password = "Password@123",
            phone = "9876543210",
            role = UserRole.ADMIN
        )

        assertTrue(result is Resource.Success)
        val user = (result as Resource.Success).data
        assertNotNull(user)
        assertEquals(UserRole.CUSTOMER, user?.role)
        assertNotEquals(UserRole.ADMIN, user?.role)
    }

    @Test
    fun `admin email authenticate assigns ADMIN role`() = runTest {
        val result = authRepository.loginWithEmail("cravexa10@gmail.com", "anyPass")
        assertTrue(result is Resource.Success)
        val user = (result as Resource.Success).data
        assertNotNull(user)
        assertEquals(UserRole.ADMIN, user?.role)
    }

    @Test
    fun `initial admin requires mandatory password change on first login`() = runTest {
        val mustRotate = checkAdminMustChangePasswordUseCase("cravexa10@gmail.com")
        assertTrue("Initial administrator must be flagged to rotate password", mustRotate)
    }

    @Test
    fun `admin password rotation validates minimum length and mismatch`() = runTest {
        val shortResult = adminChangePasswordUseCase("cravexa10@gmail.com", "oldPass", "short")
        assertTrue(shortResult is Resource.Error)

        val sameResult = adminChangePasswordUseCase("cravexa10@gmail.com", "samePass", "samePass")
        assertTrue(sameResult is Resource.Error)
    }

    @Test
    fun `successful password rotation clears mustChangePassword requirement`() = runTest {
        val result = adminChangePasswordUseCase("cravexa10@gmail.com", "Pass@0118", "NewSuperSecurePass@2026")
        assertTrue(result is Resource.Success)

        val mustRotateAfter = checkAdminMustChangePasswordUseCase("cravexa10@gmail.com")
        assertFalse("After rotation, mustChangePassword must be false", mustRotateAfter)
    }
}
