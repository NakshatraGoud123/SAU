package com.nisr.sau

import kotlinx.coroutines.delay
import kotlin.random.Random

class AuthRepository {

    /**
     * Simulates calling /auth/forgot-password
     */
    suspend fun forgotPassword(emailOrPhone: String): Result<Unit> {
        delay(1000)
        return Result.success(Unit)
    }

    /**
     * Simulates calling /auth/send-otp.
     * Returns a randomly generated 4-digit OTP.
     */
    suspend fun sendOtp(emailOrPhone: String, method: String): Result<String> {
        delay(1000)
        val otp = (1000..9999).random().toString()
        // In a real app, the server sends this to the user's phone/email.
        // We print it to console/log for the developer to see during testing.
        println("REAL SIMULATION: Sending OTP ($otp) to $emailOrPhone via $method")
        return Result.success(otp)
    }

    /**
     * Simulates calling /auth/verify-otp
     */
    suspend fun verifyOtp(emailOrPhone: String, otp: String, expectedOtp: String): Result<Unit> {
        delay(1000)
        return if (otp == expectedOtp) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("Invalid OTP. Please check the code sent to you."))
        }
    }

    /**
     * Simulates calling /auth/reset-password
     */
    suspend fun resetPassword(emailOrPhone: String, newPassword: String): Result<Unit> {
        delay(1000)
        return Result.success(Unit)
    }
}
