package com.nisr.sau.ui.login

data class LoginUiState(
    val email: String = "",
    val emailError: String? = null,
    val isEmailValid: Boolean = false,
    val password: String = "",
    val passwordError: String? = null,
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isLoginSuccess: Boolean = false
)
