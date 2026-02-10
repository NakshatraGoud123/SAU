package com.nisr.sau

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class CategoryViewModel : ViewModel() {

    private val _categories = mutableStateOf<List<Category>>(getStaticCategories())
    val categories: State<List<Category>> = _categories

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private fun getStaticCategories(): List<Category> {
        return listOf(
            Category(
                name = "Essential Supplies", 
                icon = "shoppingbasket",
                subcategories = listOf(
                    SubCategoryData("Vegetables", "eco"),
                    SubCategoryData("Fruits", "shoppingbasket"),
                    SubCategoryData("Grocery", "localgrocerystore"),
                    SubCategoryData("Milk & Dairy", "coffee"),
                    SubCategoryData("Snacks", "fastfood"),
                    SubCategoryData("Meat", "kebabdining"),
                    SubCategoryData("Fish", "setmeal"),
                    SubCategoryData("Eggs", "egg"),
                    SubCategoryData("Water", "waterdrop"),
                    SubCategoryData("Gas Cylinder", "propanetank")
                )
            ),
            Category(
                name = "Residential Services", 
                icon = "homework",
                subcategories = listOf(
                    SubCategoryData("Electrician", "bolt"),
                    SubCategoryData("Plumber", "plumbing"),
                    SubCategoryData("Carpenter", "handyman"),
                    SubCategoryData("Painter", "formatpaint"),
                    SubCategoryData("AC Repair", "acunit"),
                    SubCategoryData("Laundry", "locallaundryservice"),
                    SubCategoryData("Appliance Repair", "kitchen"),
                    SubCategoryData("Pest Control", "bugreport"),
                    SubCategoryData("Cleaning", "cleaningservices"),
                    SubCategoryData("Furniture", "weekend")
                )
            ),
            Category(
                name = "Food Solutions", 
                icon = "restaurant",
                subcategories = listOf(
                    SubCategoryData("Home Delivery", "deliverydining"),
                    SubCategoryData("Catering", "bakerydining"),
                    SubCategoryData("Bakery", "cake"),
                    SubCategoryData("Restaurant", "restaurant"),
                    SubCategoryData("Tiffin", "lunchdining"),
                    SubCategoryData("Beverages", "localdrink")
                )
            ),
            Category(
                name = "Education Services", 
                icon = "school",
                subcategories = listOf(
                    SubCategoryData("Tutor", "person"),
                    SubCategoryData("Language", "assignment"),
                    SubCategoryData("Music", "modeltraining"),
                    SubCategoryData("Arts", "brush")
                )
            ),
            Category(
                name = "Business Solutions", 
                icon = "businesscenter",
                subcategories = listOf(
                    SubCategoryData("IT Support", "laptop"),
                    SubCategoryData("Marketing", "assignment"),
                    SubCategoryData("Accounting", "assignment"),
                    SubCategoryData("Legal", "assignment"),
                    SubCategoryData("Printing", "print"),
                    SubCategoryData("Courier", "localpostoffice")
                )
            ),
            Category(
                name = "Lifestyle Management", 
                icon = "style",
                subcategories = listOf(
                    SubCategoryData("Event Planner", "celebration"),
                    SubCategoryData("Photographer", "videocam"),
                    SubCategoryData("Personal Trainer", "selfimprovement"),
                    SubCategoryData("Travel Agent", "timetoleave"),
                    SubCategoryData("Pet Care", "pets"),
                    SubCategoryData("Gardening", "grass")
                )
            ),
            Category(
                name = "Technology Services", 
                icon = "devices",
                subcategories = listOf(
                    SubCategoryData("Mobile Repair", "smartphone"),
                    SubCategoryData("Laptop Repair", "laptopmac"),
                    SubCategoryData("CCTV", "videocam"),
                    SubCategoryData("Networking", "wifi"),
                    SubCategoryData("Smart Home", "settingsremote")
                )
            ),
            Category(
                name = "Men's Personal Care", 
                icon = "face",
                subcategories = listOf(
                    SubCategoryData("Haircut", "contentcut"),
                    SubCategoryData("Shaving", "faceretouchingnatural"),
                    SubCategoryData("Massage", "selfimprovement"),
                    SubCategoryData("Facial", "face")
                )
            ),
            Category(
                name = "Women's Personal Care", 
                icon = "autofixhigh",
                subcategories = listOf(
                    SubCategoryData("Hair Styling", "contentcut"),
                    SubCategoryData("Makeup", "face"),
                    SubCategoryData("Manicure", "brush"),
                    SubCategoryData("Pedicure", "brush"),
                    SubCategoryData("Spa", "spa")
                )
            )
        )
    }
}
