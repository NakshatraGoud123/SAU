package com.nisr.sau

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubCategoryScreen(navController: NavController, serviceName: String) {
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    
    val bgColor = Color(0xFFFFF4EC)
    val accent = Color(0xFFE57373)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(serviceName) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(bgColor)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Choose your plan for $serviceName",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            ServicePlanCard("Basic Plan", "50.00", "Essential services included", serviceName, db, userId, context, navController)
            Spacer(modifier = Modifier.height(16.dp))
            ServicePlanCard("Standard Plan", "150.00", "Most popular choice", serviceName, db, userId, context, navController)
            Spacer(modifier = Modifier.height(16.dp))
            ServicePlanCard("Premium Plan", "250.00", "All-inclusive premium care", serviceName, db, userId, context, navController)
        }
    }
}

@Composable
fun ServicePlanCard(
    planName: String,
    price: String,
    description: String,
    categoryName: String,
    db: FirebaseFirestore,
    userId: String?,
    context: android.content.Context,
    navController: NavController
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(planName, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(description, fontSize = 14.sp, color = Color.Gray)
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text("SAR $price", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFE57373))
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                // ✅ ADD TO WISHLIST
                OutlinedButton(
                    onClick = {
                        if (userId == null) return@OutlinedButton
                        val item = hashMapOf(
                            "User ID" to userId,
                            "service Name" to "$categoryName ($planName)",
                            "price" to price,
                            "service image" to ""
                        )
                        db.collection("Wishlist").add(item).addOnSuccessListener {
                            Toast.makeText(context, "Added to Wishlist!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Favorite, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Wishlist")
                }

                // ✅ BOOK NOW
                Button(
                    onClick = {
                        if (userId == null) return@Button
                        val date = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
                        val booking = hashMapOf(
                            "User ID" to userId,
                            "service Name" to "$categoryName ($planName)",
                            "date" to date,
                            "time" to "10:00 AM",
                            "status" to "Pending",
                            "createdAt" to com.google.firebase.Timestamp.now()
                        )
                        db.collection("Bookings").add(booking).addOnSuccessListener {
                            Toast.makeText(context, "Booking Successful!", Toast.LENGTH_SHORT).show()
                            navController.navigate("booking_success")
                        }
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE57373))
                ) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Book Now")
                }
            }
        }
    }
}
