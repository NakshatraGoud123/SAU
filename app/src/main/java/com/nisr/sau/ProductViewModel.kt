package com.nisr.sau

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    
    private val _products = mutableStateOf<List<Product>>(emptyList())
    val products: State<List<Product>> = _products

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _productDetail = mutableStateOf<Product?>(null)
    val productDetail: State<Product?> = _productDetail

    fun fetchProducts(category: String, subcategory: String = "All") {
        _isLoading.value = true
        var query = db.collection("Products").whereEqualTo("category", category)
        
        if (subcategory != "All") {
            query = query.whereEqualTo("subcategory", subcategory)
        }

        query.get()
            .addOnSuccessListener { result ->
                val fetchedProducts = result.toObjects<Product>()
                if (fetchedProducts.isEmpty()) {
                    seedDefaultProducts()
                    // Refetch after seeding
                    fetchProducts(category, subcategory)
                } else {
                    _products.value = fetchedProducts
                    _isLoading.value = false
                }
            }
            .addOnFailureListener {
                _isLoading.value = false
            }
    }

    fun addToCart(product: Product) {
        viewModelScope.launch {
            val cartItem = hashMapOf(
                "productId" to product.id,
                "name" to product.name,
                "category" to product.category,
                "subcategory" to product.subcategory,
                "weight" to product.weight,
                "imageUrl" to product.imageUrl,
                "price" to product.price,
                "quantity" to 1,
                "timestamp" to com.google.firebase.Timestamp.now()
            )
            db.collection("Cart").add(cartItem)
        }
    }

    private fun seedDefaultProducts() {
        val productsToSeed = mutableListOf<Product>()
        
        // Vegetables
        val vegetables = listOf(
            // Leafy
            Product(name = "Spinach", weight = "250g", rating = 4.8, category = "Vegetables", subcategory = "Leafy", imageUrl = "https://images.unsplash.com/photo-1576045057995-568f588f82fb?q=80&w=500&auto=format&fit=crop"),
            Product(name = "Coriander", weight = "100g", rating = 4.5, category = "Vegetables", subcategory = "Leafy", imageUrl = "https://images.unsplash.com/photo-1588879460618-9249e7d947d1?q=80&w=500&auto=format&fit=crop"),
            // Root
            Product(name = "Potato", weight = "1kg", rating = 4.7, category = "Vegetables", subcategory = "Root", imageUrl = "https://images.unsplash.com/photo-1518977676601-b53f02bad675?q=80&w=500&auto=format&fit=crop"),
            Product(name = "Onion", weight = "1kg", rating = 4.6, category = "Vegetables", subcategory = "Root", imageUrl = "https://images.unsplash.com/photo-1508747703725-719777637510?q=80&w=500&auto=format&fit=crop"),
            // Fruit Veg
            Product(name = "Tomato", weight = "500g", rating = 4.4, category = "Vegetables", subcategory = "Fruit Vegetables", imageUrl = "https://images.unsplash.com/photo-1546094096-0df4bcaaa337?q=80&w=500&auto=format&fit=crop"),
            // Exotic
            Product(name = "Broccoli", weight = "500g", rating = 4.9, category = "Vegetables", subcategory = "Exotic", imageUrl = "https://images.unsplash.com/photo-1584270354949-c26b0d5b4a0c?q=80&w=500&auto=format&fit=crop")
        )
        productsToSeed.addAll(vegetables)

        // Fruits
        val fruits = listOf(
            Product(name = "Orange", weight = "1kg", rating = 4.6, category = "Fruits", subcategory = "Citrus", imageUrl = "https://images.unsplash.com/photo-1547514701-42782101795e?q=80&w=500&auto=format&fit=crop"),
            Product(name = "Banana", weight = "6 pcs", rating = 4.8, category = "Fruits", subcategory = "Tropical", imageUrl = "https://images.unsplash.com/photo-1571771894821-ad990253544a?q=80&w=500&auto=format&fit=crop"),
            Product(name = "Strawberry", weight = "250g", rating = 4.9, category = "Fruits", subcategory = "Berries", imageUrl = "https://images.unsplash.com/photo-1464960350473-9e8dea63f0ca?q=80&w=500&auto=format&fit=crop"),
            Product(name = "Apple", weight = "1kg", rating = 4.7, category = "Fruits", subcategory = "Everyday", imageUrl = "https://images.unsplash.com/photo-1560806887-1e4cd0b6cbd6?q=80&w=500&auto=format&fit=crop")
        )
        productsToSeed.addAll(fruits)

        // Dairy
        val dairy = listOf(
            Product(name = "Fresh Milk", weight = "1L", rating = 4.9, category = "Dairy", subcategory = "Milk", imageUrl = "https://images.unsplash.com/photo-1550583724-125581fe218a?q=80&w=500&auto=format&fit=crop"),
            Product(name = "Paneer", weight = "200g", rating = 4.7, category = "Dairy", subcategory = "Cheese & Paneer", imageUrl = "https://images.unsplash.com/photo-1631452180519-c014fe946bc7?q=80&w=500&auto=format&fit=crop")
        )
        productsToSeed.addAll(dairy)

        // Meat
        val meat = listOf(
            Product(name = "Chicken Breast", weight = "500g", rating = 4.8, category = "Meat & Fish", subcategory = "Chicken", imageUrl = "https://images.unsplash.com/photo-1604503468506-a8da13d82791?q=80&w=500&auto=format&fit=crop"),
            Product(name = "Farm Eggs", weight = "6 pcs", rating = 4.7, category = "Meat & Fish", subcategory = "Eggs", imageUrl = "https://images.unsplash.com/photo-1498654077810-12c21d4d6dc3?q=80&w=500&auto=format&fit=crop")
        )
        productsToSeed.addAll(meat)

        productsToSeed.forEach { product ->
            val ref = db.collection("Products").document()
            val productWithId = product.copy(id = ref.id)
            ref.set(productWithId)
        }
    }
}
