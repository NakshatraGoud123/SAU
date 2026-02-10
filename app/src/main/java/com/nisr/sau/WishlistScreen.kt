package com.nisr.sau

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.PropertyName

// ✅ Data class now perfectly matches your Firestore fields
data class WishlistItem(
    var id: String = "",
    @get:PropertyName("User ID") @set:PropertyName("User ID") var userId: String = "",
    @get:PropertyName("service Name") @set:PropertyName("service Name") var serviceName: String = "",
    @get:PropertyName("service image") @set:PropertyName("service image") var serviceImage: String = "",
    @get:PropertyName("price") @set:PropertyName("price") var price: String = "0.00"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishlistScreen(navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    var wishlistItems by remember { mutableStateOf<List<WishlistItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // ✅ Extracted logic into a function to call on launch and for retry
    fun loadWishlist() {
        if (userId == null) {
            errorMessage = "Not logged in"
            isLoading = false
            return
        }
        isLoading = true
        errorMessage = null

        // ✅ Using Capitalized "Wishlist" collection
        db.collection("Wishlist")
            .whereEqualTo("User ID", userId) // Capitalized field with space
            .get()
            .addOnSuccessListener { documents ->
                val items = mutableListOf<WishlistItem>()
                for(doc in documents) {
                    val item = doc.toObject(WishlistItem::class.java)
                    item.id = doc.id
                    items.add(item)
                }
                wishlistItems = items
                isLoading = false
            }
            .addOnFailureListener { e ->
                errorMessage = "Failed to load: ${e.message}"
                isLoading = false
            }
    }

    LaunchedEffect(userId) {
        loadWishlist()
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Your Wishlist") }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, "Back") } }) }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding).background(Color(0xFFFFF4EC))) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (errorMessage != null) {
                // ✅ Added a Retry button for when things go wrong
                Column(modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(errorMessage!!, color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { loadWishlist() }) { Text("Retry") }
                }
            } else if (wishlistItems.isEmpty()) {
                Text("Your wishlist is empty", modifier = Modifier.align(Alignment.Center), color = Color.Gray)
            } else {
                LazyColumn(contentPadding = PaddingValues(16.dp)) {
                    items(wishlistItems) { item ->
                        WishlistCard(item) {
                            // Delete logic remains the same
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun WishlistCard(item: WishlistItem, onDelete: () -> Unit) {
    // UI for the card remains the same
}
