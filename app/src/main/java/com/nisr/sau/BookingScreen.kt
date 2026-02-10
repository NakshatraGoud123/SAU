package com.nisr.sau

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

private val BrandBlue = Color(0xFF007BFF)
private val TextDarkBlue = Color(0xFF002366)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(navController: NavController, serviceName: String) {
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    val userId = FirebaseAuth.getInstance().currentUser?.uid

    var customerName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var fromAddress by remember { mutableStateOf("Hyderabad") }
    var toAddress by remember { mutableStateOf("User Address") }
    var selectedDay by remember { mutableStateOf(17) }
    var isLoading by remember { mutableStateOf(false) }

    val serviceImage = when {
        serviceName.contains("Floor", ignoreCase = true) -> R.drawable.floor_cleaning_service
        serviceName.contains("Pest", ignoreCase = true) -> R.drawable.pest_control_service
        serviceName.contains("AC", ignoreCase = true) -> R.drawable.ac_cleaning_service
        else -> R.drawable.ac_cleaning_service
    }

    Scaffold(
        containerColor = Color(0xFFF0F4F8),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Booking Details", color = TextDarkBlue, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.padding(8.dp).background(Color.White, CircleShape).shadow(1.dp, CircleShape)
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextDarkBlue)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(16.dp))

            // Service Info Card
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Image(painter = painterResource(id = serviceImage), contentDescription = null, modifier = Modifier.size(80.dp).clip(RoundedCornerShape(12.dp)), contentScale = ContentScale.Crop)
                    Spacer(Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(serviceName, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextDarkBlue)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                            Text("Hyderabad, India", fontSize = 13.sp, color = Color.Gray)
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // Customer Information
            Text("Customer information", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextDarkBlue)
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(value = customerName, onValueChange = { customerName = it }, label = { Text("Name") }, placeholder = { Text("Enter your name") }, leadingIcon = { Icon(Icons.Default.PersonOutline, null) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = TextFieldDefaults.outlinedTextFieldColors(containerColor = Color.White, unfocusedBorderColor = Color(0xFFEEEEEE)))
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(value = phoneNumber, onValueChange = { phoneNumber = it }, label = { Text("Phone Number") }, placeholder = { Text("Enter phone number") }, leadingIcon = { Icon(Icons.Default.PhoneEnabled, null) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = TextFieldDefaults.outlinedTextFieldColors(containerColor = Color.White, unfocusedBorderColor = Color(0xFFEEEEEE)))

            Spacer(Modifier.height(24.dp))

            // Calendar Card
            Text("Schedule your service", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextDarkBlue)
            Spacer(Modifier.height(12.dp))
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                // Calendar UI here...
            }
            
            Spacer(Modifier.height(32.dp))

            // Action Button
            Button(
                onClick = {
                    if (userId == null || customerName.isBlank() || phoneNumber.isBlank()) {
                        Toast.makeText(context, "Please fill in your name and phone number", Toast.LENGTH_LONG).show()
                        return@Button
                    }
                    isLoading = true
                    val date = "$selectedDay SEP 2025"
                    val bookingData = hashMapOf("User ID" to userId, "service Name" to serviceName, "date" to date, "time" to "10:00 AM", "status" to "Pending", "createdAt" to com.google.firebase.Timestamp.now())
                    db.collection("Bookings").add(bookingData)
                        .addOnSuccessListener {
                            isLoading = false
                            Toast.makeText(context, "Booking Successful!", Toast.LENGTH_SHORT).show()
                            navController.navigate("booking_success") { popUpTo("home") }
                        }
                        .addOnFailureListener { e ->
                            isLoading = false
                            Toast.makeText(context, "Booking failed: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BrandBlue),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                } else {
                    Text("Continue", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
                }
            }
            Spacer(Modifier.height(32.dp))
        }
    }
}
