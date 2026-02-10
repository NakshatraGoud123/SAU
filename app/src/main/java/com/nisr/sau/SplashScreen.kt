package com.nisr.sau

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nisr.sau.ui.theme.SauBlue
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    // Fade-in animation state
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        // Subtle fade-in animation
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000)
        )
        // Short delay before navigation (total ~3 seconds)
        delay(2000)
        
        // Navigate to Login screen
        navController.navigate("login") {
            popUpTo("splash") { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.alpha(alpha.value)
        ) {
            // SAU Logo (House + Wrench)
            Image(
                painter = painterResource(id = R.drawable.sau_logo),
                contentDescription = "SAU Logo",
                modifier = Modifier.size(140.dp),
                contentScale = ContentScale.Fit
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Minimal, modern progress indicator (Urban Company style)
            LinearProgressIndicator(
                modifier = Modifier
                    .width(120.dp)
                    .height(3.dp),
                color = SauBlue,
                trackColor = Color(0xFFF0F0F0)
            )
        }
    }
}
