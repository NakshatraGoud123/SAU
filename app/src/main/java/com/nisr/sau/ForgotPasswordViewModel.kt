package com.nisr.sau

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nisr.sau.utils.UserSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ForgotPasswordUiState(
    val emailOrPhone: String = "",
    val isInputValid: Boolean = false,
    val selectedMethod: RecoveryMethod = RecoveryMethod.EMAIL,
    val otp: String = "",
    val sentOtp: String = "", // Stores the generated OTP for verification
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

    fun onEmailOrPhoneChanged(value: String) {
        _uiState.update { it.copy(
            emailOrPhone = value,
            isInputValid = android.util.Patterns.EMAIL_ADDRESS.matcher(value).matches() || 
                           (value.length >= 10 && value.all { char -> char.isDigit() })
        ) }
    }

    fun onMethodSelected(method: RecoveryMethod) {
        _uiState.update { it.copy(selectedMethod = method) }
    }

    fun onOtpChanged(otp: String) {
        _uiState.update { it.copy(otp = otp) }
        if (otp.length == 4) {
            verifyOtp()
        }
    }

    fun startRecovery() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = repository.forgotPassword(_uiState.value.emailOrPhone)
            _uiState.update { it.copy(isLoading = false) }
            
            result.onSuccess {
                _uiState.update { it.copy(currentStep = 2) }
            }.onFailure { e ->
                _uiState.update { it.copy(errorMessage = e.message) }
            }
        }
    }

    fun sendOtp() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val method = if (_uiState.value.selectedMethod == RecoveryMethod.EMAIL) "email" else "sms"
            val result = repository.sendOtp(_uiState.value.emailOrPhone, method)
            _uiState.update { it.copy(isLoading = false) }
            
            result.onSuccess { otp ->
                // In a real app, 'otp' comes from the server and we verify it later.
                // For this simulation, we store it so the user can "receive" it.
                _uiState.update { it.copy(currentStep = 3, sentOtp = otp) }
            }.onFailure { e ->
                _uiState.update { it.copy(errorMessage = e.message) }
            }
        }
    }

    fun verifyOtp() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = repository.verifyOtp(
                _uiState.value.emailOrPhone, 
                _uiState.value.otp,
                _uiState.value.sentOtp
            )
            _uiState.update { it.copy(isLoading = false) }
            
            result.onSuccess {
                // Simulate logging in the user upon successful verification
                UserSession.username = _uiState.value.emailOrPhone.substringBefore("@")
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
