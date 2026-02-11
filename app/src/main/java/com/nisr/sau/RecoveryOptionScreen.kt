package com.nisr.sau

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nisr.sau.ui.theme.SauBg
import com.nisr.sau.ui.theme.SauBlue
import com.nisr.sau.ui.theme.SauTextGray

@Composable
fun RecoveryOptionScreen(
    viewModel: ForgotPasswordViewModel,
    onBackClick: () -> Unit,
    onCloseClick: () -> Unit,
    onNext: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(uiState.currentStep) {
        if (uiState.currentStep == 3) {
            onNext()
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(SauBg)) {
        // Top Header Section (Same as Email Screen)
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
                    text = "Recovery Option",
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
                    text = "Forgot Password",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Please select option to send link reset password",
                    style = MaterialTheme.typography.bodyMedium,
                    color = SauTextGray
                )

                Spacer(modifier = Modifier.height(32.dp))

                RecoveryOptionItem(
                    title = "Reset via email",
                    subtitle = "If you have email linked to account",
                    icon = Icons.Default.Email,
                    isSelected = uiState.selectedMethod == RecoveryMethod.EMAIL,
                    onClick = { viewModel.onMethodSelected(RecoveryMethod.EMAIL) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                RecoveryOptionItem(
                    title = "Reset via SMS",
                    subtitle = "If you have number linked to account",
                    icon = Icons.Default.PhoneIphone,
                    isSelected = uiState.selectedMethod == RecoveryMethod.SMS,
                    onClick = { viewModel.onMethodSelected(RecoveryMethod.SMS) }
                )

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = { 
                        val activity = context as? Activity
                        if (activity != null) {
                            viewModel.sendOtp(activity)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SauBlue),
                    enabled = !uiState.isLoading
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text(
                            text = "Select Option",
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

@Composable
fun RecoveryOptionItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = if (isSelected) SauBlue else Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color.White else Color(0xFFF7F8F9)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 0.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = if (isSelected) SauBlue.copy(alpha = 0.1f) else Color.White,
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = SauBlue
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = SauTextGray
                )
            }
            
            if (isSelected) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF4CAF50)
                )
            }
        }
    }
}
