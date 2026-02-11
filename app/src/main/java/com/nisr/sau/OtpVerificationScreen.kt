package com.nisr.sau

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nisr.sau.ui.theme.SauBg
import com.nisr.sau.ui.theme.SauBlue
import com.nisr.sau.ui.theme.SauTextGray

@Composable
fun OtpVerificationScreen(
    viewModel: ForgotPasswordViewModel,
    onBackClick: () -> Unit,
    onCloseClick: () -> Unit,
    onVerifySuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isOtpVerified) {
        if (uiState.isOtpVerified) {
            onVerifySuccess()
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(SauBg)) {
        // Top Header Section (Same as others)
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
                    text = "Phone Verification",
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
                    .fillMaxSize()
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = "Enter recovery code",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "We have sent it on your ${if (uiState.selectedMethod == RecoveryMethod.EMAIL) "email" else "phone"}\n${uiState.emailOrPhone}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = SauTextGray
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // OTP Input Fields
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        repeat(4) { index ->
                            val digit = uiState.otp.getOrNull(index)?.toString() ?: ""
                            OtpDigitBox(
                                digit = digit,
                                isFocused = uiState.otp.length == index,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "This code will expire in 5 minutes",
                        style = MaterialTheme.typography.bodySmall,
                        color = SauBlue
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Custom Numeric Keypad
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = SauBlue,
                            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
                        )
                        .padding(24.dp)
                ) {
                    val keys = listOf(
                        listOf("1", "2", "3"),
                        listOf("4", "5", "6"),
                        listOf("7", "8", "9"),
                        listOf("", "0", "⌫")
                    )

                    keys.forEach { row ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            row.forEach { key ->
                                Box(
                                    modifier = Modifier
                                        .size(64.dp)
                                        .clickable(enabled = key.isNotEmpty()) {
                                            if (key == "⌫") {
                                                if (uiState.otp.isNotEmpty()) {
                                                    viewModel.onOtpChanged(uiState.otp.dropLast(1))
                                                }
                                            } else if (uiState.otp.length < 4) {
                                                viewModel.onOtpChanged(uiState.otp + key)
                                            }
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = key,
                                        style = MaterialTheme.typography.headlineMedium,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OtpDigitBox(
    digit: String,
    isFocused: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(60.dp)
            .background(
                color = if (digit.isEmpty()) Color(0xFFF7F8F9) else Color.White,
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = if (isFocused) 2.dp else 1.dp,
                color = if (isFocused) SauBlue else Color(0xFFE8ECF4),
                shape = RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        if (digit.isNotEmpty()) {
            // Check image shows dots for hidden code, let's use dots
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(SauBlue, RoundedCornerShape(6.dp))
            )
        }
    }
}
