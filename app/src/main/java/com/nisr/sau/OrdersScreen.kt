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

// ✅ Corrected Order model to match your Firebase fields exactly
data class Order(
    var id: String = "",
    @get:PropertyName("User ID") @set:PropertyName("User ID") var userId: String = "",
    @get:PropertyName("date") @set:PropertyName("date") var date: String = "",
    @get:PropertyName("status") @set:PropertyName("status") var status: String = "Pending",
    @get:PropertyName("totalPrice") @set:PropertyName("totalPrice") var totalPrice: String = "0.00",
    @get:PropertyName("items") @set:PropertyName("items") var items: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    val userIdAuth = FirebaseAuth.getInstance().currentUser?.uid
    var orders by remember { mutableStateOf<List<Order>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    fun loadOrders() {
        if (userIdAuth == null) {
            errorMessage = "Not logged in"
            isLoading = false
            return
        }
        isLoading = true
        errorMessage = null

        // ✅ Using Capitalized "Orders" collection
        db.collection("Orders")
            .whereEqualTo("User ID", userIdAuth)
            .get()
            .addOnSuccessListener { result ->
                val list = mutableListOf<Order>()
                for (doc in result) {
                    val order = doc.toObject(Order::class.java)
                    order.id = doc.id
                    list.add(order)
                }
                orders = list
                isLoading = false
            }
            .addOnFailureListener { e ->
                errorMessage = "Failed to load: ${e.message}"
                isLoading = false
            }
    }

    LaunchedEffect(userIdAuth) {
        loadOrders()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Orders") },
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
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (errorMessage != null) {
                Column(modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(errorMessage!!, color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { loadOrders() }) { Text("Retry") }
                }
            } else if (orders.isEmpty()) {
                Text("No orders found", modifier = Modifier.align(Alignment.Center), color = Color.Gray)
            } else {
                LazyColumn(contentPadding = PaddingValues(16.dp)) {
                    items(orders) { order ->
                        OrderCard(order)
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun OrderCard(order: Order) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Order ID: ${order.id.take(8).uppercase()}", fontWeight = FontWeight.Bold)
                Text(order.status, color = if (order.status == "Completed") Color(0xFF2FBF71) else Color(0xFFE57373))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("Date: ${order.date}", fontSize = 14.sp, color = Color.Gray)
            Text("Items: ${order.items}", fontSize = 14.sp)
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            Text("Total: SAR ${order.totalPrice}", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFFE57373))
        }
    }
}
