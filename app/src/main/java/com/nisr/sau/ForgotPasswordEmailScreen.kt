package com.nisr.sau

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.nisr.sau.ui.theme.SauBg
import com.nisr.sau.ui.theme.SauBlue
import com.nisr.sau.ui.theme.SauTextGray

@Composable
fun ForgotPasswordEmailScreen(
    viewModel: ForgotPasswordViewModel,
    onBackClick: () -> Unit,
    onCloseClick: () -> Unit,
    onNext: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    // Navigate to next screen when step changes
    LaunchedEffect(uiState.currentStep) {
        if (uiState.currentStep == 2) {
            onNext()
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(SauBg)) {
        // Top Header Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .background(Color.White)
        ) {
            Image(
                painter = painterResource(id = R.drawable.auth_illustration),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
                Text(
                    text = "Forgot Password",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                IconButton(onClick = onCloseClick) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }
        }

        // Bottom Content Section
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White,
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxSize()
            ) {
                Text(
                    text = "Password Recovery",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "We will send password recovery code on this email or phone",
                    style = MaterialTheme.typography.bodyMedium,
                    color = SauTextGray
                )

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Email or Phone Number",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = uiState.emailOrPhone,
                    onValueChange = { viewModel.onEmailOrPhoneChanged(it) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Email or phone number") },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SauBlue,
                        unfocusedBorderColor = SauBg
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email // Fixed: Use Email type to allow both text and numbers without auto-switching
                    ),
                    trailingIcon = {
                        if (uiState.isInputValid) {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color(0xFF4CAF50)
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = { viewModel.startRecovery() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SauBlue),
                    enabled = uiState.isInputValid && !uiState.isLoading
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text(
                            text = "Send Link Recovery",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
