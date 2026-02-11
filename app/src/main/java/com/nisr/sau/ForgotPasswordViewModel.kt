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
    val currentStep: Int = 1,
    val isOtpVerified: Boolean = false
)

enum class RecoveryMethod {
    EMAIL, SMS
}

class ForgotPasswordViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ForgotPasswordUiState())
    val uiState: StateFlow<ForgotPasswordUiState> = _uiState.asStateFlow()

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage = _toastMessage.asSharedFlow()

    fun onEmailOrPhoneChanged(value: String) {
        val trimmedValue = value.trim()
        val isEmail = android.util.Patterns.EMAIL_ADDRESS.matcher(trimmedValue).matches()
        val digitCount = trimmedValue.count { it.isDigit() }
        val isPhone = digitCount in 10..15
        val isValid = isEmail || isPhone
        
        _uiState.update { it.copy(
            emailOrPhone = value,
            isInputValid = isValid
        ) }
    }

    fun onMethodSelected(method: RecoveryMethod) {
        _uiState.update { it.copy(selectedMethod = method) }
    }

    fun onOtpChanged(otp: String) {
        _uiState.update { it.copy(otp = otp) }
        
        val requiredLength = if (_uiState.value.selectedMethod == RecoveryMethod.SMS) 6 else 4
        
        if (otp.length == requiredLength) {
            if (_uiState.value.selectedMethod == RecoveryMethod.SMS) {
                verifyOtp()
            } else {
                // For Email, we just simulate verification success as it's usually link-based
                // or if you have a custom OTP backend, call it here.
                _uiState.update { it.copy(isOtpVerified = true) }
            }
        }
    }

    fun startRecovery() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            // Move to step 2 to select recovery method
            _uiState.update { it.copy(isLoading = false, currentStep = 2) }
        }
    }

    fun sendOtp(activity: Activity) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val method = _uiState.value.selectedMethod
            val target = _uiState.value.emailOrPhone.trim()

            if (method == RecoveryMethod.EMAIL) {
                val result = repository.sendPasswordResetEmail(target)
                _uiState.update { it.copy(isLoading = false) }
                result.onSuccess {
                    _toastMessage.emit("Reset link sent to your email!")
                    // Move to step 3 so the user sees a confirmation/otp screen
                    _uiState.update { it.copy(currentStep = 3) }
                }.onFailure { e ->
                    _uiState.update { it.copy(errorMessage = e.message) }
                }
            } else {
                // For SMS, we use the Indian prefix +91
                val formattedPhone = if (target.startsWith("+")) target else "+91$target"
                val result = repository.sendPhoneOtp(formattedPhone, activity)
                _uiState.update { it.copy(isLoading = false) }
                result.onSuccess { verificationId ->
                    _uiState.update { it.copy(currentStep = 3, verificationId = verificationId) }
                    _toastMessage.emit("SMS code sent!")
                }.onFailure { e ->
                    _uiState.update { it.copy(errorMessage = e.message) }
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
            }.onFailure { e ->
                _uiState.update { it.copy(errorMessage = e.message) }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
