package com.cravexa

import com.cravexa.core.common.AuthValidator
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AuthValidatorTest {

    @Test
    fun `validateName with valid name returns valid result`() {
        val result = AuthValidator.validateName("Rajesh Kumar")
        assertTrue(result.isValid)
    }

    @Test
    fun `validateName with empty string returns error`() {
        val result = AuthValidator.validateName("   ")
        assertFalse(result.isValid)
    }

    @Test
    fun `validateEmail with valid email returns valid result`() {
        val result = AuthValidator.validateEmail("foodie@cravexa.com")
        assertTrue(result.isValid)
    }

    @Test
    fun `validateEmail with invalid email returns error`() {
        val result = AuthValidator.validateEmail("invalid-email-address")
        assertFalse(result.isValid)
    }

    @Test
    fun `validatePassword with strong password returns valid result`() {
        val result = AuthValidator.validatePassword("Cravexa2026!")
        assertTrue(result.isValid)
    }

    @Test
    fun `validatePassword with short password returns error`() {
        val result = AuthValidator.validatePassword("Pass1")
        assertFalse(result.isValid)
    }

    @Test
    fun `validatePassword without numbers returns error`() {
        val result = AuthValidator.validatePassword("PasswordOnly")
        assertFalse(result.isValid)
    }

    @Test
    fun `validateConfirmPassword with matching passwords returns valid`() {
        val result = AuthValidator.validateConfirmPassword("Password123", "Password123")
        assertTrue(result.isValid)
    }

    @Test
    fun `validateConfirmPassword with mismatching passwords returns error`() {
        val result = AuthValidator.validateConfirmPassword("Password123", "Different123")
        assertFalse(result.isValid)
    }

    @Test
    fun `validatePhone with valid 10 digit Indian number returns valid`() {
        val result = AuthValidator.validatePhone("9876543210")
        assertTrue(result.isValid)
    }

    @Test
    fun `validatePhone with invalid starting digit returns error`() {
        val result = AuthValidator.validatePhone("1234567890")
        assertFalse(result.isValid)
    }

    @Test
    fun `validateOtp with 6 digits returns valid`() {
        val result = AuthValidator.validateOtp("654321")
        assertTrue(result.isValid)
    }

    @Test
    fun `validateOtp with invalid length returns error`() {
        val result = AuthValidator.validateOtp("1234")
        assertFalse(result.isValid)
    }

    @Test
    fun `validateFssaiNumber with valid 14 digit number returns valid`() {
        val result = AuthValidator.validateFssaiNumber("11223344556677")
        assertTrue(result.isValid)
    }

    @Test
    fun `validateFssaiNumber with non 14 digits returns error`() {
        val shortResult = AuthValidator.validateFssaiNumber("123456789")
        assertFalse(shortResult.isValid)

        val alphaResult = AuthValidator.validateFssaiNumber("1122334455667A")
        assertFalse(alphaResult.isValid)
    }

    @Test
    fun `validatePincode with valid 6 digit Indian PIN code returns valid`() {
        val result = AuthValidator.validatePincode("560038")
        assertTrue(result.isValid)
    }

    @Test
    fun `validatePincode with invalid length returns error`() {
        val result = AuthValidator.validatePincode("56003")
        assertFalse(result.isValid)
    }
}
