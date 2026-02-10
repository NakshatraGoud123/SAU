package com.nisr.sau

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.PropertyName

// ✅ Data class updated to match your Firestore fields exactly
data class Booking(
    var id: String = "",
    @get:PropertyName("User ID") @set:PropertyName("User ID") var userId: String = "",
    @get:PropertyName("service Name") @set:PropertyName("service Name") var serviceName: String = "",
    @get:PropertyName("date") @set:PropertyName("date") var date: String = "",
    @get:PropertyName("time") @set:PropertyName("time") var time: String = "",
    @get:PropertyName("status") @set:PropertyName("status") var status: String = "Pending"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingsScreen(navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    val userIdAuth = FirebaseAuth.getInstance().currentUser?.uid
    var bookings by remember { mutableStateOf<List<Booking>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    fun loadBookings() {
        if (userIdAuth == null) {
            errorMessage = "Not logged in"
            isLoading = false
            return
        }
        isLoading = true
        errorMessage = null

        // ✅ Using Capitalized "Bookings" collection
        db.collection("Bookings")
            .whereEqualTo("User ID", userIdAuth)
            .get()
            .addOnSuccessListener { result ->
                val list = mutableListOf<Booking>()
                for (doc in result) {
                    val booking = doc.toObject(Booking::class.java)
                    booking.id = doc.id
                    list.add(booking)
                }
                bookings = list
                isLoading = false
            }
            .addOnFailureListener { e ->
                errorMessage = "Failed to load: ${e.message}"
                isLoading = false
            }
    }

    LaunchedEffect(userIdAuth) {
        loadBookings()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Bookings") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding).background(Color(0xFFFFF4EC))) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = Color(0xFFE57373))
            } else if (errorMessage != null) {
                Column(modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(errorMessage!!, color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { loadBookings() }) { Text("Retry") }
                }
            } else if (bookings.isEmpty()) {
                Text("No bookings found", modifier = Modifier.align(Alignment.Center), color = Color.Gray)
            } else {
                LazyColumn(contentPadding = PaddingValues(16.dp)) {
                    items(bookings) { booking ->
                        BookingCard(booking)
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun BookingCard(booking: Booking) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(booking.serviceName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(booking.status, color = if (booking.status == "Completed") Color(0xFF2FBF71) else Color(0xFFE57373))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("Date: ${booking.date}", fontSize = 14.sp)
            Text("Time: ${booking.time}", fontSize = 14.sp)
            Text("Booking ID: ${booking.id.take(8).uppercase()}", fontSize = 12.sp, color = Color.Gray)
        }
    }
}
