package com.nisr.sau

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
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
import com.nisr.sau.utils.UserSession

@Composable
fun ProfileScreen(
    onEditProfile: () -> Unit,
    onCartClick: () -> Unit,
    onLogout: () -> Unit,
    navController: NavController
) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val userId = auth.currentUser?.uid

    val bgColor = Color(0xFFFFF4EC)
    val cardColor = Color.White
    val accent = Color(0xFFE57373)

    var isLoading by remember { mutableStateOf(true) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    // ✅ FETCH DATA FROM FIRESTORE
    LaunchedEffect(userId) {
        if (userId != null) {
            db.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        UserSession.username = document.getString("name") ?: ""
                        UserSession.email = document.getString("email") ?: ""
                        UserSession.phone = document.getString("phone") ?: ""
                        UserSession.profileImageUrl = document.getString("profile_image_url") ?: ""
                    }
                    isLoading = false
                }
                .addOnFailureListener {
                    isLoading = false
                    Toast.makeText(context, "Error fetching profile", Toast.LENGTH_SHORT).show()
                }
        } else {
            isLoading = false
        }
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = accent)
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(bgColor)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(12.dp))

            // ✅ Profile Image
            if (UserSession.profileImageUrl.isNotEmpty()) {
                AsyncImage(
                    model = UserSession.profileImageUrl,
                    contentDescription = "Profile",
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.profile),
                    contentDescription = "Profile",
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                )
            }

            Spacer(Modifier.height(8.dp))

            Text(UserSession.username.ifEmpty { "User" }, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
            Text(UserSession.phone, fontSize = 14.sp, color = Color.Gray)

            Spacer(Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ProfileTopCard(Icons.Outlined.Inventory2, "Your Orders") {
                    navController.navigate("orders")
                }
                ProfileTopCard(Icons.Outlined.FavoriteBorder, "Your Wishlist") {
                    navController.navigate("wishlist")
                }
                ProfileTopCard(Icons.Outlined.HelpOutline, "Need Help?") {
                    // Open WhatsApp Support
                    val url = "https://api.whatsapp.com/send?phone=+966123456789"
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    context.startActivity(intent)
                }
            }

            Spacer(Modifier.height(16.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate("my_bookings") },
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(2.dp),
                colors = CardDefaults.cardColors(cardColor)
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.EventNote, contentDescription = null, tint = accent)
                        Spacer(Modifier.width(10.dp))
                        Column {
                            Text("My Bookings", fontWeight = FontWeight.SemiBold)
                            Text("View your service history", fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                    Icon(Icons.Outlined.ChevronRight, contentDescription = null)
                }
            }

            Spacer(Modifier.height(20.dp))
            Text("My Account Section", modifier = Modifier.fillMaxWidth(), fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))

            AccountItem(Icons.Outlined.Person, "Profile information", onClick = onEditProfile)
            AccountItem(Icons.Outlined.LocationOn, "Saved addresses management") {
                Toast.makeText(context, "Address Management coming soon", Toast.LENGTH_SHORT).show()
            }
            AccountItem(Icons.Outlined.CreditCard, "Payment methods saved") {
                Toast.makeText(context, "Payment methods coming soon", Toast.LENGTH_SHORT).show()
            }
            AccountItem(Icons.Outlined.EventNote, "My booked services", onClick = { navController.navigate("my_bookings") })
            AccountItem(Icons.Outlined.Language, "Language preference") {
                Toast.makeText(context, "Selected Language: English", Toast.LENGTH_SHORT).show()
            }
            AccountItem(Icons.Outlined.Notifications, "Notification settings") {
                Toast.makeText(context, "Notifications are ON", Toast.LENGTH_SHORT).show()
            }
            AccountItem(Icons.Outlined.Logout, "Logout") { showLogoutDialog = true }
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    auth.signOut()
                    UserSession.clear()
                    showLogoutDialog = false
                    onLogout()
                }) { Text("Logout") }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) { Text("Cancel") }
            },
            title = { Text("Logout") },
            text = { Text("Are you sure you want to logout?") }
        )
    }
}

@Composable
fun ProfileTopCard(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(100.dp)
            .height(80.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(icon, contentDescription = null, tint = Color.Black.copy(alpha = 0.85f))
            Spacer(Modifier.height(6.dp))
            Text(title, fontSize = 12.sp)
        }
    }
}

@Composable
fun AccountItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(1.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = Color(0xFFE57373))
            Spacer(Modifier.width(12.dp))
            Text(title, modifier = Modifier.weight(1f))
            Icon(Icons.Outlined.ChevronRight, contentDescription = null)
        }
    }
}
