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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nisr.sau.ui.theme.SauBlue
import com.nisr.sau.utils.SettingsManager
import com.nisr.sau.utils.UserSession
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(navController: NavController) {
    val context = LocalContext.current
    val alpha = remember { Animatable(0f) }
    val scale = remember { Animatable(0.9f) }

    LaunchedEffect(Unit) {
        launch {
            alpha.animateTo(1f, animationSpec = tween(1000))
        }
        launch {
            scale.animateTo(1f, animationSpec = tween(1000))
        }
        
        delay(2500)
        
        // Temporarily forcing Onboarding to open for verification
        if (UserSession.isLoggedIn) {
            navController.navigate("home") {
                popUpTo("splash") { inclusive = true }
            }
        } else {
            // Force onboarding even if complete, or you can revert this later
            navController.navigate("onboarding") {
                popUpTo("splash") { inclusive = true }
            }
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
            modifier = Modifier
                .fillMaxWidth()
                .alpha(alpha.value)
                .scale(scale.value)
        ) {
            Image(
                painter = painterResource(id = R.drawable.sau_logo),
                contentDescription = "SAU Logo",
                modifier = Modifier.size(200.dp),
                contentScale = ContentScale.Fit
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth(0.55f)
                    .height(4.dp),
                color = SauBlue,
                trackColor = Color(0xFFF0F0F0)
            )
        }
    }
}
