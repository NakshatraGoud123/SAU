package com.nisr.sau

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nisr.sau.utils.UserSession
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ForgotPasswordUiState(
    val emailOrPhone: String = "",
    val isInputValid: Boolean = false,
    val selectedMethod: RecoveryMethod = RecoveryMethod.EMAIL,
    val otp: String = "",
    val verificationId: String = "", // Firebase Verification ID for SMS
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isOtpVerified: Boolean = false,
    val passwordResetLinkSent: Boolean = false
)

enum class RecoveryMethod {
    EMAIL, SMS
}

sealed class ForgotPasswordNavigation {
    object NavigateToOptions : ForgotPasswordNavigation()
    object NavigateToOtp : ForgotPasswordNavigation()
    object NavigateToLogin : ForgotPasswordNavigation()
}

class ForgotPasswordViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ForgotPasswordUiState())
    val uiState: StateFlow<ForgotPasswordUiState> = _uiState.asStateFlow()

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage = _toastMessage.asSharedFlow()

    private val _navigationEvent = MutableSharedFlow<ForgotPasswordNavigation>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    fun onEmailOrPhoneChanged(value: String) {
        val trimmedValue = value.trim()
        val isEmail = android.util.Patterns.EMAIL_ADDRESS.matcher(trimmedValue).matches()
        // Simple digit check for phone
        val isPhone = trimmedValue.all { it.isDigit() || it == '+' } && trimmedValue.length >= 10
        val isValid = isEmail || isPhone
        
        _uiState.update { it.copy(
            emailOrPhone = value,
            isInputValid = isValid,
            errorMessage = null
        ) }
    }

    fun onMethodSelected(method: RecoveryMethod) {
        _uiState.update { it.copy(selectedMethod = method, errorMessage = null) }
    }

    fun onOtpChanged(otp: String) {
        _uiState.update { it.copy(otp = otp, errorMessage = null) }
        
        val requiredLength = 6 // Firebase SMS is 6 digits
        
        if (otp.length == requiredLength) {
            verifyOtp()
        }
    }

    fun startRecovery() {
        viewModelScope.launch {
            _navigationEvent.emit(ForgotPasswordNavigation.NavigateToOptions)
        }
    }

    fun sendOtp(activity: Activity) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val method = _uiState.value.selectedMethod
            val target = _uiState.value.emailOrPhone.trim()

            if (method == RecoveryMethod.EMAIL) {
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches()) {
                    _uiState.update { it.copy(isLoading = false, errorMessage = "Please enter a valid email address.") }
                    return@launch
                }

                val result = repository.sendPasswordResetEmail(target)
                _uiState.update { it.copy(isLoading = false) }
                result.onSuccess {
                    _toastMessage.emit("Password reset link sent to your email! Please check your inbox and spam folder.")
                    _uiState.update { it.copy(passwordResetLinkSent = true) }
                    _navigationEvent.emit(ForgotPasswordNavigation.NavigateToLogin)
                }.onFailure { e ->
                    Log.e("ForgotPassword", "Email error: ${e.message}", e)
                    _uiState.update { it.copy(errorMessage = e.message ?: "Failed to send reset email. Make sure the email is registered.") }
                }
            } else {
                val isPhone = target.all { it.isDigit() || it == '+' } && target.length >= 10
                if (!isPhone) {
                    _uiState.update { it.copy(isLoading = false, errorMessage = "Please enter a valid phone number.") }
                    return@launch
                }

                // For SMS, we use the Indian prefix +91 as default if not present
                val formattedPhone = if (target.startsWith("+")) target else "+91$target"
                val result = repository.sendPhoneOtp(formattedPhone, activity)
                _uiState.update { it.copy(isLoading = false) }
                result.onSuccess { verificationId ->
                    _uiState.update { it.copy(verificationId = verificationId) }
                    _toastMessage.emit("SMS code sent!")
                    _navigationEvent.emit(ForgotPasswordNavigation.NavigateToOtp)
                }.onFailure { e ->
                    Log.e("ForgotPassword", "SMS error: ${e.message}", e)
                    val displayError = when {
                        e.message?.contains("reCAPTCHA") == true -> "Verification failed. Please try again or check your internet."
                        e.message?.contains("quota") == true -> "SMS quota exceeded. Please try again later."
                        else -> e.message ?: "Failed to send SMS. Please check your phone number and try again."
                    }
                    _uiState.update { it.copy(errorMessage = displayError) }
                }
            }
        }
    }

    fun verifyOtp() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = repository.verifyPhoneCode(
                _uiState.value.verificationId, 
                _uiState.value.otp
            )
            _uiState.update { it.copy(isLoading = false) }
            
            result.onSuccess {
                UserSession.username = "User"
                UserSession.isLoggedIn = true
                _uiState.update { it.copy(isOtpVerified = true) }
                _navigationEvent.emit(ForgotPasswordNavigation.NavigateToLogin)
            }.onFailure { e ->
                _uiState.update { it.copy(errorMessage = e.message ?: "Invalid code. Please try again.") }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
