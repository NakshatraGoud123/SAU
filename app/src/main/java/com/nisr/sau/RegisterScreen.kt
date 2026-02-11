package com.nisr.sau

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nisr.sau.ui.login.RegisterViewModel

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = viewModel(),
    onRegisterSuccess: () -> Unit,
    onSignInClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.isRegisterSuccess) {
        if (uiState.isRegisterSuccess) {
            onRegisterSuccess()
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.surface
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Sign up",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold
                ),
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Image(
                painter = painterResource(id = R.drawable.auth_illustration),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Hello",
                style = MaterialTheme.typography.headlineMedium.copy(fontSize = 24.sp, fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Create an account to continue",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            CustomRegisterInputField(
                label = "Full Name",
                value = uiState.fullName,
                onValueChange = { viewModel.onFullNameChanged(it) },
                placeholder = "Full name",
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
            )

            Spacer(modifier = Modifier.height(12.dp))

            CustomRegisterInputField(
                label = "Email Address",
                value = uiState.email,
                onValueChange = { viewModel.onEmailChanged(it) },
                placeholder = "Your email address",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
            )

            Spacer(modifier = Modifier.height(12.dp))

            // NEW PHONE NUMBER FIELD
            CustomRegisterInputField(
                label = "Phone Number",
                value = uiState.phoneNumber,
                onValueChange = { viewModel.onPhoneNumberChanged(it) },
                placeholder = "7989244233",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
            )

            Spacer(modifier = Modifier.height(12.dp))

            CustomRegisterInputField(
                label = "Password",
                value = uiState.password,
                onValueChange = { viewModel.onPasswordChanged(it) },
                placeholder = "Password",
                isPassword = true,
                passwordVisible = uiState.isPasswordVisible,
                onPasswordToggle = { viewModel.togglePasswordVisibility() },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                    viewModel.register()
                })
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = uiState.isTermsAccepted,
                    onCheckedChange = { viewModel.onTermsAcceptedChanged(it) }
                )
                Text(
                    text = buildAnnotatedString {
                        append("By creating an account you agree to our ")
                        withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)) {
                            append("Terms of Service")
                        }
                    },
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.clickable { }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.register() },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(16.dp),
                enabled = !uiState.isLoading
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                } else {
                    Text("Create an Account", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = buildAnnotatedString {
                    append("Already have an account? ")
                    withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)) {
                        append("Sign in")
                    }
                },
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.clickable { onSignInClick() }
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun CustomRegisterInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    error: String? = null,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onPasswordToggle: (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder) },
            shape = RoundedCornerShape(12.dp),
            isError = error != null,
            visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            trailingIcon = {
                if (isPassword) {
                    IconButton(onClick = onPasswordToggle ?: {}) {
                        Icon(imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, contentDescription = null)
                    }
                }
            },
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = true
        )
    }
}
