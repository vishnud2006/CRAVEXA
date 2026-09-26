package com.cravexa.core.common

import java.util.regex.Pattern

data class ValidationResult(
    val isValid: Boolean,
    val errorMessage: String? = null
)

object AuthValidator {

    private val EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}$"
    )

    private val PHONE_PATTERN = Pattern.compile(
        "^[6-9]\\d{9}$"
    )

    fun validateName(name: String): ValidationResult {
        val trimmed = name.trim()
        return when {
            trimmed.isEmpty() -> ValidationResult(false, "Full name cannot be empty.")
            trimmed.length < 2 -> ValidationResult(false, "Name must be at least 2 characters.")
            else -> ValidationResult(true)
        }
    }

    fun validateEmail(email: String): ValidationResult {
        val trimmed = email.trim()
        return when {
            trimmed.isEmpty() -> ValidationResult(false, "Email address cannot be empty.")
            !EMAIL_PATTERN.matcher(trimmed).matches() -> ValidationResult(false, "Please enter a valid email address.")
            else -> ValidationResult(true)
        }
    }

    fun validatePassword(password: String): ValidationResult {
        return when {
            password.isEmpty() -> ValidationResult(false, "Password cannot be empty.")
            password.length < 8 -> ValidationResult(false, "Password must be at least 8 characters long.")
            !password.any { it.isDigit() } -> ValidationResult(false, "Password must contain at least one digit.")
            !password.any { it.isLetter() } -> ValidationResult(false, "Password must contain at least one letter.")
            else -> ValidationResult(true)
        }
    }

    fun validateConfirmPassword(password: String, confirmPassword: String): ValidationResult {
        return when {
            confirmPassword.isEmpty() -> ValidationResult(false, "Please confirm your password.")
            password != confirmPassword -> ValidationResult(false, "Passwords do not match.")
            else -> ValidationResult(true)
        }
    }

    fun validatePhone(phone: String): ValidationResult {
        val digitsOnly = phone.filter { it.isDigit() }.takeLast(10)
        return when {
            digitsOnly.isEmpty() -> ValidationResult(false, "Phone number cannot be empty.")
            digitsOnly.length != 10 || !PHONE_PATTERN.matcher(digitsOnly).matches() -> {
                ValidationResult(false, "Please enter a valid 10-digit mobile number.")
            }
            else -> ValidationResult(true)
        }
    }

    fun validateOtp(otp: String): ValidationResult {
        val trimmed = otp.trim()
        return when {
            trimmed.isEmpty() -> ValidationResult(false, "Please enter the 6-digit OTP.")
            trimmed.length != 6 || !trimmed.all { it.isDigit() } -> {
                ValidationResult(false, "OTP must be exactly 6 digits.")
            }
            else -> ValidationResult(true)
        }
    }

    fun validateBusinessName(name: String): ValidationResult {
        val trimmed = name.trim()
        return when {
            trimmed.isEmpty() -> ValidationResult(false, "Kitchen or Brand name cannot be empty.")
            trimmed.length < 3 -> ValidationResult(false, "Kitchen name must be at least 3 characters.")
            else -> ValidationResult(true)
        }
    }

    fun validateAddress(address: String): ValidationResult {
        val trimmed = address.trim()
        return when {
            trimmed.isEmpty() -> ValidationResult(false, "Address / Location cannot be empty.")
            trimmed.length < 5 -> ValidationResult(false, "Please enter a more complete address.")
            else -> ValidationResult(true)
        }
    }

    fun validatePincode(pincode: String): ValidationResult {
        val trimmed = pincode.trim()
        return when {
            trimmed.isEmpty() -> ValidationResult(false, "PIN code cannot be empty.")
            trimmed.length != 6 || !trimmed.all { it.isDigit() } -> {
                ValidationResult(false, "Please enter a valid 6-digit postal PIN code.")
            }
            else -> ValidationResult(true)
        }
    }

    fun validateFssaiNumber(fssaiNumber: String): ValidationResult {
        val trimmed = fssaiNumber.trim()
        return when {
            trimmed.isEmpty() -> ValidationResult(false, "FSSAI License/Registration Number is required.")
            trimmed.length != 14 || !trimmed.all { it.isDigit() } -> {
                ValidationResult(false, "FSSAI number must be exactly 14 digits.")
            }
            else -> ValidationResult(true)
        }
    }
}

