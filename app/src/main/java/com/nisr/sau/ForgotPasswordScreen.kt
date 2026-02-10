package com.nisr.sau

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nisr.sau.ui.theme.SauBlue
import com.nisr.sau.ui.theme.SauBg
import com.nisr.sau.ui.theme.SauTextGray

enum class ForgotPasswordStep {
    RECOVERY, OPTION, VERIFICATION
}

@Composable
fun ForgotPasswordScreen(onBackToLogin: () -> Unit) {
    var currentStep by remember { mutableStateOf(ForgotPasswordStep.RECOVERY) }
    var email by remember { mutableStateOf("") }
    var selectedOption by remember { mutableStateOf("email") }
    var recoveryCode by remember { mutableStateOf(listOf("", "", "", "")) }

    Box(modifier = Modifier.fillMaxSize().background(SauBg)) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header with illustration
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color.White)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.floor_cleaning_service),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    alpha = 0.3f
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", modifier = Modifier.clickable { 
                        if (currentStep == ForgotPasswordStep.RECOVERY) onBackToLogin()
                        else currentStep = ForgotPasswordStep.values()[currentStep.ordinal - 1]
                    })
                    Text(
                        text = when(currentStep) {
                            ForgotPasswordStep.RECOVERY -> "Forgot Password"
                            ForgotPasswordStep.OPTION -> "Forgot Password"
                            ForgotPasswordStep.VERIFICATION -> "Phone Verification"
                        },
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )
                    Icon(Icons.Default.Close, contentDescription = "Close", modifier = Modifier.clickable { onBackToLogin() })
                }
            }

            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.White,
                shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
            ) {
                when (currentStep) {
                    ForgotPasswordStep.RECOVERY -> RecoveryStep(
                        email = email,
                        onEmailChange = { email = it },
                        onContinue = { currentStep = ForgotPasswordStep.OPTION }
                    )
                    ForgotPasswordStep.OPTION -> OptionStep(
                        selectedOption = selectedOption,
                        onOptionSelected = { selectedOption = it },
                        onContinue = { currentStep = ForgotPasswordStep.VERIFICATION }
                    )
                    ForgotPasswordStep.VERIFICATION -> VerificationStep(
                        code = recoveryCode,
                        email = email,
                        onContinue = { onBackToLogin() }
                    )
                }
            }
        }
    }
}

@Composable
fun RecoveryStep(email: String, onEmailChange: (String) -> Unit, onContinue: () -> Unit) {
    Column(modifier = Modifier.padding(24.dp)) {
        Text("Password Recovery", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text("We will send password recovery code on this email", fontSize = 14.sp, color = SauTextGray)
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text("Email Address", fontWeight = FontWeight.Medium, fontSize = 14.sp)
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Your email address") },
            shape = RoundedCornerShape(12.dp),
            trailingIcon = { if (email.contains("@")) Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.Green) }
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onContinue,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(containerColor = SauBlue)
        ) {
            Text("Send Link Recovery", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun OptionStep(selectedOption: String, onOptionSelected: (String) -> Unit, onContinue: () -> Unit) {
    Column(modifier = Modifier.padding(24.dp)) {
        Text("Forgot Password", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text("Please select option to send link reset password", fontSize = 14.sp, color = SauTextGray)
        
        Spacer(modifier = Modifier.height(32.dp))
        
        ResetOptionItem(
            title = "Reset via email",
            subtitle = "If you have email linked to account",
            icon = Icons.Default.Email,
            isSelected = selectedOption == "email",
            onClick = { onOptionSelected("email") }
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        ResetOptionItem(
            title = "Reset via SMS",
            subtitle = "If you have number linked to account",
            icon = Icons.Default.Phone,
            isSelected = selectedOption == "sms",
            onClick = { onOptionSelected("sms") }
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onContinue,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(containerColor = SauBlue)
        ) {
            Text("Select Option", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ResetOptionItem(title: String, subtitle: String, icon: androidx.compose.ui.graphics.vector.ImageVector, isSelected: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, SauBlue) else null,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(48.dp).background(SauBg, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = SauBlue)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(subtitle, fontSize = 12.sp, color = SauTextGray)
            }
            if (isSelected) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.Green)
            }
        }
    }
}

@Composable
fun VerificationStep(code: List<String>, email: String, onContinue: () -> Unit) {
    Column(modifier = Modifier.padding(24.dp)) {
        Text("Enter recovery code", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text("We have sent it on your email $email", fontSize = 14.sp, color = SauTextGray)
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            code.forEach { char ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(60.dp)
                        .background(SauBg, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = if (char.isEmpty()) "•" else char, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        Text("This code will expire in 5 minutes", fontSize = 12.sp, color = SauTextGray)

        Spacer(modifier = Modifier.weight(1f))
        
        // Numeric Keypad Simulation (as in image)
        Column(modifier = Modifier.fillMaxWidth().background(SauBlue, RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)).padding(24.dp)) {
            val keys = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "", "0", "<-")
            keys.chunked(3).forEach { row ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    row.forEach { key ->
                        Text(
                            text = key,
                            color = Color.White,
                            fontSize = 24.sp,
                            modifier = Modifier.padding(16.dp).clickable { 
                                if (key == "<-") { /* Handle backspace */ }
                                else if (key.isNotEmpty()) { /* Handle input */ onContinue() } 
                            },
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}
