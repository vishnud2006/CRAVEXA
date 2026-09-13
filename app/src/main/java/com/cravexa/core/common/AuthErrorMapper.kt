package com.cravexa.core.common

import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object AuthErrorMapper {

    fun mapError(throwable: Throwable?): String {
        if (throwable == null) return "An unexpected error occurred. Please try again."

        val message = throwable.message?.lowercase() ?: ""

        return when {
            throwable is UnknownHostException || throwable is IOException || message.contains("network") || message.contains("offline") -> {
                "Unable to connect. Please check your internet connection and try again."
            }
            throwable is SocketTimeoutException || message.contains("timeout") -> {
                "Connection timed out. Please try again in a few moments."
            }
            message.contains("user-not-found") || message.contains("wrong-password") || message.contains("invalid-credential") || message.contains("invalid_login_credentials") -> {
                "Incorrect email or password. Please check your credentials."
            }
            message.contains("email-already-in-use") || message.contains("already_exists") -> {
                "This email address is already registered. Please login instead."
            }
            message.contains("invalid-email") -> {
                "The email address is invalid. Please check the format."
            }
            message.contains("weak-password") -> {
                "The password provided is too weak. Please use at least 8 characters with letters and numbers."
            }
            message.contains("too-many-requests") || message.contains("quota-exceeded") -> {
                "Too many attempts. For security, please wait a few minutes before trying again."
            }
            message.contains("invalid-verification-code") || message.contains("invalid-otp") -> {
                "Invalid verification code. Please check the OTP and try again."
            }
            message.contains("session-expired") || message.contains("code-expired") -> {
                "The OTP has expired. Please request a new code."
            }
            message.contains("user-disabled") -> {
                "This account has been disabled. Please contact CRAVEXA support."
            }
            message.contains("google_sign_in_failed") || message.contains("sign_in_canceled") -> {
                "Google sign-in was canceled or could not be completed."
            }
            message.isNotBlank() && !message.contains("exception") -> {
                throwable.message ?: "Authentication failed. Please try again."
            }
            else -> {
                "Something went wrong while communicating with CRAVEXA servers. Please try again."
            }
        }
    }
}

