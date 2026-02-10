package com.nisr.sau

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.WorkOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

private val BrandOrange = Color(0xFFFD7E41)
private val LightGreyText = Color(0xFF757575)
private val DarkGreyText = Color(0xFF2D2D2D)
private val NavBg = Color(0xFFFFF1EB)
private val UnselectedGrey = Color(0xFFBDBDBD)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(navController: NavController) {
    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Empty Cart", 
                        fontSize = 19.sp, 
                        fontWeight = FontWeight.SemiBold,
                        color = DarkGreyText
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.Black)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = { CustomBottomNav(navController) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Illustration using the actual image from drawable
            Image(
                painter = painterResource(id = R.drawable.sau_cart),
                contentDescription = "Empty Cart Illustration",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "SAU Services Makes Life Easy",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = DarkGreyText,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Your Cart is Empty.",
                fontSize = 15.sp,
                color = LightGreyText,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Normal
            )
            
            Text(
                text = "Browse our services and find what you need.",
                fontSize = 15.sp,
                color = LightGreyText,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Normal
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = { navController.navigate("all_categories") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp), // Fully rounded as per image
                colors = ButtonDefaults.buttonColors(containerColor = BrandOrange)
            ) {
                Text(
                    "Browse Services",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            
            Spacer(modifier = Modifier.height(80.dp)) 
        }
    }
}

@Composable
fun CustomBottomNav(navController: NavController) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp, start = 20.dp, end = 20.dp)
            .height(72.dp),
        shape = RoundedCornerShape(36.dp),
        color = Color.White,
        shadowElevation = 12.dp
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Home Tab (Selected)
            Surface(
                shape = RoundedCornerShape(25.dp),
                color = NavBg,
                modifier = Modifier.clickable { 
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Home",
                        tint = BrandOrange,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Home",
                        color = BrandOrange,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }

            // Booking Tab
            IconButton(onClick = { navController.navigate("bookings") }) {
                Icon(
                    imageVector = Icons.Outlined.WorkOutline,
                    contentDescription = "Bookings",
                    tint = UnselectedGrey,
                    modifier = Modifier.size(26.dp)
                )
            }

            // Wishlist Tab
            IconButton(onClick = { /* Navigate to Wishlist */ }) {
                Icon(
                    imageVector = Icons.Outlined.FavoriteBorder,
                    contentDescription = "Wishlist",
                    tint = UnselectedGrey,
                    modifier = Modifier.size(26.dp)
                )
            }
        }
    }
}
