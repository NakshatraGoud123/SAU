package com.nisr.sau

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CategoryScreen() {

    val categories = listOf(
        Category(name = "Hair & Grooming", icon = "contentcut"),
        Category(name = "Beard Grooming", icon = "faceretouchingnatural"),
        Category(name = "Facial & Cleanup", icon = "face"),
        Category(name = "Head & Shoulder Massage", icon = "selfimprovement"),
        Category(name = "Body Massage", icon = "spa"),
        Category(name = "Hair Color", icon = "invertcolors")
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.padding(12.dp)
    ) {
        items(categories) { category ->
            CategoryItem(category)
        }
    }
}

@Composable
fun CategoryItem(category: Category) {

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Icon(
                imageVector = getIconFromName(category.icon),
                contentDescription = category.name,
                modifier = Modifier.size(50.dp),
                tint = Color(0xFF0D47A1)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = category.name,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
