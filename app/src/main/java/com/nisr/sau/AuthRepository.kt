package com.nisr.sau

import android.app.Activity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AuthRepository {

    private val auth = FirebaseAuth.getInstance()

    /**
     * Sends a real Firebase Password Reset Email.
     */
    suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Triggers Firebase Phone Authentication to send a real 6-digit SMS code.
     */
    suspend fun sendPhoneOtp(
        phoneNumber: String,
        activity: Activity
    ): Result<String> = suspendCoroutine { continuation ->
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: com.google.firebase.auth.PhoneAuthCredential) {
                // Auto-retrieval or instant validation (rarely happens on all devices)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                continuation.resume(Result.failure(e))
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                // Return the verificationId to be used for final validation
                continuation.resume(Result.success(verificationId))
            }
        }

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(activity)             // Activity (for callback binding)
            .setCallbacks(callbacks)           // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    /**
     * Verifies the SMS code against the verificationId provided by Firebase.
     */
    suspend fun verifyPhoneCode(verificationId: String, code: String): Result<Unit> {
        return try {
            val credential = PhoneAuthProvider.getCredential(verificationId, code)
            auth.signInWithCredential(credential).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
