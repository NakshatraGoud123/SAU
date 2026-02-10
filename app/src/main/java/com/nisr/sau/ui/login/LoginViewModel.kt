package com.nisr.sau.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChanged(email: String) {
        val isValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        _uiState.update { it.copy(email = email, emailError = null, isEmailValid = isValid) }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(password = password, passwordError = null) }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun login() {
        if (!validateFields()) return

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            auth.signInWithEmailAndPassword(_uiState.value.email.trim(), _uiState.value.password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _uiState.update { it.copy(isLoading = false, isLoginSuccess = true) }
                    } else {
                        _uiState.update { 
                            it.copy(
                                isLoading = false, 
                                errorMessage = task.exception?.localizedMessage ?: "Login failed"
                            ) 
                        }
                    }
                }
        }
    }

    fun signInWithGoogle(idToken: String) {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                handleAuthResult(task.isSuccessful, task.exception?.localizedMessage)
            }
    }

    fun signInWithFacebook(accessToken: String) {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        
        val credential = FacebookAuthProvider.getCredential(accessToken)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                handleAuthResult(task.isSuccessful, task.exception?.localizedMessage)
            }
    }

    private fun handleAuthResult(isSuccessful: Boolean, error: String?) {
        if (isSuccessful) {
            _uiState.update { it.copy(isLoading = false, isLoginSuccess = true) }
        } else {
            _uiState.update { 
                it.copy(
                    isLoading = false, 
                    errorMessage = error ?: "Authentication failed"
                ) 
            }
        }
    }

    private fun validateFields(): Boolean {
        var isValid = true
        val email = _uiState.value.email
        val password = _uiState.value.password

        if (email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _uiState.update { it.copy(emailError = "Please enter a valid email address") }
            isValid = false
        }

        if (password.length < 6) {
            _uiState.update { it.copy(passwordError = "Password must be at least 6 characters") }
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
