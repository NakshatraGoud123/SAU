package com.nisr.sau

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

/* ---------------- BRAND COLORS ---------------- */
private val BrandBlue = Color(0xFF0D47A1)
private val LightBlueBorder = Color(0xFFE3F2FD)
private val TextGrey = Color(0xFF757575)
private val CardBg = Color(0xFFF9FBFF)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    userName: String,
    userLocation: String
) {
    var searchQuery by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Scaffold(
        containerColor = Color.White,
        bottomBar = { HomeBottomBar(navController) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(16.dp))

            /* ---------------- TOP BAR ---------------- */
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Location Section
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { /* Change Location */ }
                ) {
                    Icon(
                        Icons.Default.LocationOn,
                        null,
                        tint = BrandBlue,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        userLocation,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                    Icon(
                        Icons.Default.KeyboardArrowDown,
                        null,
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(Modifier.weight(1f))

                // Cart and Notification
                IconButton(onClick = { navController.navigate("cart") }) {
                    Icon(Icons.Outlined.ShoppingCart, null, modifier = Modifier.size(24.dp), tint = Color.Black)
                }
                IconButton(onClick = { }) {
                    Icon(Icons.Outlined.Notifications, null, modifier = Modifier.size(24.dp), tint = Color.Black)
                }
            }

            Spacer(Modifier.height(12.dp))

            // Greeting Section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.profile),
                    contentDescription = null,
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color.LightGray, CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(Modifier.width(14.dp))
                Column {
                    Text("Hello $userName,", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text("What service you are looking for?", fontSize = 13.sp, color = TextGrey)
                }
            }

            Spacer(Modifier.height(20.dp))

            /* ---------------- SEARCH ---------------- */
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                placeholder = { Text("Search for repair, cleaning ...", color = Color.LightGray, fontSize = 14.sp) },
                leadingIcon = { Icon(Icons.Default.Search, null, tint = Color.LightGray) },
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color(0xFFEEEEEE),
                    focusedBorderColor = BrandBlue,
                    containerColor = Color.White
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        focusManager.clearFocus()
                        if (searchQuery.isNotEmpty()) {
                            navController.navigate("all_categories?search=${searchQuery}")
                        }
                    }
                )
            )

            Spacer(Modifier.height(24.dp))

            /* ---------------- BANNERS ---------------- */
            val bannerState = rememberScrollState()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(bannerState)
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                HomeBannerItem(
                    title = "Floor cleaning\nservices",
                    discount = "Sale 20% Off",
                    imageRes = R.drawable.floor_cleaning_service,
                    bgColor = Color(0xFFFDF2F0),
                    onBookNow = { navController.navigate("bookings?serviceName=Floor Cleaning") }
                )
                HomeBannerItem(
                    title = "AC Repair &\nCleaning",
                    discount = "Sale 20% Off",
                    imageRes = R.drawable.ac_cleaning_service,
                    bgColor = Color(0xFFE3F2FD),
                    onBookNow = { navController.navigate("bookings?serviceName=AC Repair & Cleaning") }
                )
                HomeBannerItem(
                    title = "Pest Control\nServices",
                    discount = "Sale 20% Off",
                    imageRes = R.drawable.pest_control_service,
                    bgColor = Color(0xFFF1F8E9),
                    onBookNow = { navController.navigate("bookings?serviceName=Pest Control") }
                )
            }
            
            // Indicator dots
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(3) { index ->
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 3.dp)
                            .size(6.dp, 6.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray)
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            /* ---------------- SERVICES GRID ---------------- */
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Services", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text("View all", color = BrandBlue, fontWeight = FontWeight.Bold, fontSize = 14.sp, modifier = Modifier.clickable { navController.navigate("all_categories") })
            }
            Spacer(Modifier.height(16.dp))

            val allServices = listOf(
                Triple("Home Essentials", Icons.Default.Inventory2, "all_categories"),
                Triple("Household Services", Icons.Default.HomeWork, "all_categories"),
                Triple("Food & Beverages", Icons.Default.LocalDining, "all_categories"),
                Triple("Education & Tutoring", Icons.Default.School, "all_categories"),
                Triple("Business & Professional", Icons.Default.BusinessCenter, "all_categories"),
                Triple("Home & Lifestyle", Icons.Default.Style, "all_categories"),
                Triple("Tech Services", Icons.Default.Devices, "all_categories"),
                Triple("Men's Grooming", Icons.Default.Face, "all_categories"),
                Triple("Women's Beauty", Icons.Default.AutoFixHigh, "all_categories")
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .height(420.dp), 
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                userScrollEnabled = false
            ) {
                items(allServices) { service ->
                    ServiceCategoryItem(service.first, service.second) {
                        navController.navigate("all_categories?search=${service.first}")
                    }
                }
            }

            /* ---------------- POPULAR BOOKINGS ---------------- */
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Popular Bookings", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text("View all", color = BrandBlue, fontWeight = FontWeight.Bold, fontSize = 14.sp, modifier = Modifier.clickable { navController.navigate("all_categories") })
            }

            Row(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                PopularItem(R.drawable.ac_cleaning_service) { navController.navigate("bookings?serviceName=AC Cleaning") }
                PopularItem(R.drawable.pest_control_service) { navController.navigate("bookings?serviceName=Pest Control") }
                PopularItem(R.drawable.floor_cleaning_service) { navController.navigate("bookings?serviceName=Floor Cleaning") }
            }
            
            Spacer(Modifier.height(30.dp))
        }
    }
}

@Composable
fun HomeBannerItem(
    title: String,
    discount: String,
    imageRes: Int,
    bgColor: Color,
    onBookNow: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(300.dp)
            .height(170.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .weight(1.2f)
                    .padding(20.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    title,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 24.sp,
                    color = Color(0xFF1A237E)
                )
                Spacer(Modifier.height(4.dp))
                Text(discount, fontSize = 13.sp, color = TextGrey)
                Spacer(Modifier.height(12.dp))
                Button(
                    onClick = onBookNow,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BrandBlue),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
                ) {
                    Text("Book Now", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
            Image(
                painter = painterResource(imageRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )
        }
    }
}

@Composable
fun ServiceCategoryItem(title: String, icon: ImageVector, onClick: () -> Unit) {
    Column(
        modifier = Modifier.clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = CardBg,
            border = BorderStroke(1.dp, LightBlueBorder),
            modifier = Modifier
                .size(94.dp)
                .shadow(2.dp, RoundedCornerShape(16.dp), spotColor = BrandBlue)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = BrandBlue, modifier = Modifier.size(34.dp))
            }
        }
        Spacer(Modifier.height(8.dp))
        Text(
            title, 
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            maxLines = 2,
            lineHeight = 14.sp,
            color = Color.Black
        )
    }
}

@Composable
fun PopularItem(imageRes: Int, onClick: () -> Unit) {
    Image(
        painter = painterResource(imageRes),
        contentDescription = null,
        modifier = Modifier
            .size(160.dp)
            .clip(RoundedCornerShape(18.dp))
            .clickable { onClick() },
        contentScale = ContentScale.Crop
    )
}

@Composable
fun HomeBottomBar(navController: NavController) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 0.dp,
        modifier = Modifier.border(1.dp, Color(0xFFF5F5F5), RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, null) },
            label = { Text("Home") },
            selected = true,
            onClick = { navController.navigate("home") },
            colors = NavigationBarItemDefaults.colors(selectedIconColor = BrandBlue, selectedTextColor = BrandBlue, indicatorColor = Color.Transparent)
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Category, null) },
            label = { Text("Category") },
            selected = false,
            onClick = { navController.navigate("all_categories") },
            colors = NavigationBarItemDefaults.colors(selectedIconColor = BrandBlue, selectedTextColor = BrandBlue, indicatorColor = Color.Transparent)
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.CalendarMonth, null) },
            label = { Text("Booking") },
            selected = false,
            onClick = { navController.navigate("bookings") },
            colors = NavigationBarItemDefaults.colors(selectedIconColor = BrandBlue, selectedTextColor = BrandBlue, indicatorColor = Color.Transparent)
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, null) },
            label = { Text("Profile") },
            selected = false,
            onClick = { navController.navigate("profile") }
        )
    }
}
