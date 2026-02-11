package com.nisr.sau

import kotlinx.coroutines.delay

class AuthRepository {

    /**
     * Simulates calling /auth/forgot-password
     * Accepts any email or phone for simulation purposes.
     */
    suspend fun forgotPassword(emailOrPhone: String): Result<Unit> {
        delay(1000) // Simulate network latency
        // Return success for any input to allow testing with any email/phone
        return Result.success(Unit)
    }

    /**
     * Simulates calling /auth/send-otp
     */
    suspend fun sendOtp(emailOrPhone: String, method: String): Result<String> {
        delay(1000)
        // In a real app, the server would send an OTP. 
        // We'll use "1234" as a universal simulation code.
        val otp = "1234"
        println("Simulated: Sending OTP ($otp) to $emailOrPhone via $method")
        return Result.success(otp)
    }

    /**
     * Simulates calling /auth/verify-otp
     */
    suspend fun verifyOtp(emailOrPhone: String, otp: String): Result<Unit> {
        delay(1000)
        // For simulation, we accept "1234" as the correct code for any user.
        return if (otp == "1234") {
            Result.success(Unit)
        } else {
            Result.failure(Exception("Invalid OTP. Use 1234 for testing."))
        }
    }

    /**
     * Simulates calling /auth/reset-password
     */
    suspend fun resetPassword(emailOrPhone: String, newPassword: String): Result<Unit> {
        delay(1000)
        println("Password for $emailOrPhone has been reset.")
        return Result.success(Unit)
    }
}
