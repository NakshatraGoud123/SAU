package com.nisr.sau

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

private val BrandBlue = Color(0xFF007BFF)
private val LightBlueGradient = Color(0xFFE3F2FD)

@Composable
fun BookingSuccessScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.White, LightBlueGradient)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 40.dp)
        ) {
            // Success Icon with Confetti effect representation
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(120.dp)
            ) {
                // Confetti Dots (Visual representation)
                repeat(6) { index ->
                    Box(
                        modifier = Modifier
                            .offset(
                                x = (if (index % 2 == 0) 40 else -40).dp,
                                y = (index * 15 - 45).dp
                            )
                            .size(if (index % 3 == 0) 8.dp else 5.dp)
                            .background(
                                color = when (index % 3) {
                                    0 -> Color(0xFFFFD54F) // Yellow
                                    1 -> Color(0xFF4FC3F7) // Blue
                                    else -> Color(0xFFCE93D8) // Purple
                                },
                                shape = CircleShape
                            )
                    )
                }

                Surface(
                    modifier = Modifier.size(80.dp),
                    shape = CircleShape,
                    color = BrandBlue,
                    shadowElevation = 8.dp
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Success",
                        tint = Color.White,
                        modifier = Modifier
                            .padding(20.dp)
                            .size(40.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Your SAU Services Task\nis Successfully Booked",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A1A),
                textAlign = TextAlign.Center,
                lineHeight = 32.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "You can view the task booking info in\nthe Tasks section",
                fontSize = 15.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = { navController.navigate("all_categories") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BrandBlue)
            ) {
                Text(
                    "Continue Booking",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(
                onClick = { 
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Go to Home",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrandBlue
                )
            }
        }
    }
}
