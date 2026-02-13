package com.nisr.sau

data class Product(
    val id: String = "",
    val name: String = "",
    val price: Double = 0.0,
    val weight: String = "",
    val imageUrl: String = "",
    val rating: Double = 0.0,
    val discount: String? = null,
    val description: String = "",
    val category: String = "",
    val subcategory: String = ""
)
