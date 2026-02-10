package com.nisr.sau

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ServicesScreen(onServiceSelected: (String) -> Unit) {

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {

        Text("Men Services", style = MaterialTheme.typography.headlineMedium)

        Spacer(Modifier.height(20.dp))

        Text("Hair Cut", Modifier.clickable {
            onServiceSelected("Hair Cut")
        })

        Text("Beard Trim", Modifier.clickable {
            onServiceSelected("Beard Trim")
        })

        Text("Facial", Modifier.clickable {
            onServiceSelected("Facial")
        })
    }
}
