package com.nisr.sau.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun onFullNameChanged(name: String) {
        _uiState.update { it.copy(fullName = name, fullNameError = null) }
    }

    fun onEmailChanged(email: String) {
        _uiState.update { it.copy(email = email, emailError = null) }
    }

    fun onPhoneNumberChanged(phone: String) {
        _uiState.update { it.copy(phoneNumber = phone, phoneError = null) }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(password = password, passwordError = null) }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun onTermsAcceptedChanged(accepted: Boolean) {
        _uiState.update { it.copy(isTermsAccepted = accepted) }
    }

    fun register() {
        if (!validateFields()) return

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            auth.createUserWithEmailAndPassword(_uiState.value.email.trim(), _uiState.value.password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        val profileUpdates = UserProfileChangeRequest.Builder()
                            .setDisplayName(_uiState.value.fullName)
                            .build()

                        user?.updateProfile(profileUpdates)?.addOnCompleteListener {
                            _uiState.update { it.copy(isLoading = false, isRegisterSuccess = true) }
                        }
                    } else {
                        _uiState.update { 
                            it.copy(
                                isLoading = false, 
                                errorMessage = task.exception?.localizedMessage ?: "Registration failed"
                            ) 
                        }
                    }
                }
        }
    }

    private fun validateFields(): Boolean {
        var isValid = true
        if (_uiState.value.fullName.isBlank()) {
            _uiState.update { it.copy(fullNameError = "Please enter your full name") }
            isValid = false
        }
        if (_uiState.value.email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(_uiState.value.email).matches()) {
            _uiState.update { it.copy(emailError = "Please enter a valid email address") }
            isValid = false
        }
        if (_uiState.value.phoneNumber.isBlank() || _uiState.value.phoneNumber.length < 10) {
            _uiState.update { it.copy(phoneError = "Please enter a valid 10-digit phone number") }
            isValid = false
        }
        if (_uiState.value.password.length < 6) {
            _uiState.update { it.copy(passwordError = "Password must be at least 6 characters") }
            isValid = false
        }
        if (!_uiState.value.isTermsAccepted) {
            _uiState.update { it.copy(errorMessage = "You must agree to the Terms of Service") }
            isValid = false
        }
        return isValid
    }

    fun setError(message: String) {
        _uiState.update { it.copy(errorMessage = message, isLoading = false) }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
