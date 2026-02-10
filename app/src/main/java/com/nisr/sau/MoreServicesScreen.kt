package com.nisr.sau

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

private val BrandBlue = Color(0xFF0D47A1)
private val LightBlueBorder = Color(0xFFE3F2FD)
private val CardBg = Color(0xFFF9FBFF)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreServicesScreen(navController: NavController) {
    Scaffold(
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("More Services", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { padding ->
        val remainingServices = listOf(
            Triple("Home & Lifestyle", Icons.Default.Style, "category"),
            Triple("Tech Services", Icons.Default.Computer, "category"),
            Triple("Men's Grooming", Icons.Default.Face, "category"),
            Triple("Women's Beauty", Icons.Default.AutoFixHigh, "category")
        )

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier.padding(top = 16.dp)
            ) {
                items(remainingServices) { service ->
                    MoreServiceItemPolished(service.first, service.second) {
                        navController.navigate(service.third)
                    }
                }
            }
        }
    }
}

@Composable
fun MoreServiceItemPolished(title: String, icon: ImageVector, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Surface(
            modifier = Modifier
                .size(94.dp)
                .shadow(2.dp, RoundedCornerShape(16.dp), spotColor = BrandBlue),
            shape = RoundedCornerShape(16.dp),
            color = CardBg,
            border = BorderStroke(1.dp, LightBlueBorder)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    icon, 
                    contentDescription = title, 
                    modifier = Modifier.size(34.dp), 
                    tint = BrandBlue
                )
            }
        }
        Spacer(Modifier.height(8.dp))
        Text(
            title,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            maxLines = 2,
            lineHeight = 15.sp,
            color = Color.Black
        )
    }
}
