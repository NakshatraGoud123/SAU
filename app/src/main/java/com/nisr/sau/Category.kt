package com.nisr.sau

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

data class Category(
    val id: String = "",
    val name: String = "",
    val icon: String = "", // Changed to String to prevent Firestore crash
    val subcategories: List<SubCategoryData> = emptyList()
)

data class SubCategoryData(
    val name: String = "",
    val icon: String = "" // Changed to String to prevent Firestore crash
)

// This helper will be used in the UI to get the actual icon
fun getIconFromName(iconName: String): ImageVector {
    return when (iconName.lowercase()) {
        "essential", "inventory2" -> Icons.Default.Inventory2
        "residential", "homework" -> Icons.Default.HomeWork
        "food", "restaurant" -> Icons.Default.Restaurant
        "education", "school" -> Icons.Default.School
        "business", "businesscenter" -> Icons.Default.BusinessCenter
        "lifestyle", "style" -> Icons.Default.Style
        "tech", "devices" -> Icons.Default.Devices
        "men", "face" -> Icons.Default.Face
        "women", "autofixhigh" -> Icons.Default.AutoFixHigh
        "eco" -> Icons.Default.Eco
        "shoppingbasket" -> Icons.Default.ShoppingBasket
        "localgrocerystore" -> Icons.Default.LocalGroceryStore
        "coffee" -> Icons.Default.Coffee
        "fastfood" -> Icons.Default.Fastfood
        "kebabdining" -> Icons.Default.KebabDining
        "setmeal" -> Icons.Default.SetMeal
        "egg" -> Icons.Default.Egg
        "waterdrop" -> Icons.Default.WaterDrop
        "propanetank" -> Icons.Default.PropaneTank
        "bolt" -> Icons.Default.Bolt
        "plumbing" -> Icons.Default.Plumbing
        "handyman" -> Icons.Default.Handyman
        "formatpaint" -> Icons.Default.FormatPaint
        "acunit" -> Icons.Default.AcUnit
        "locallaundryservice" -> Icons.Default.LocalLaundryService
        "kitchen" -> Icons.Default.Kitchen
        "bugreport" -> Icons.Default.BugReport
        "cleaningservices" -> Icons.Default.CleaningServices
        "weekend" -> Icons.Default.Weekend
        "deliverydining" -> Icons.Default.DeliveryDining
        "bakerydining" -> Icons.Default.BakeryDining
        "cake" -> Icons.Default.Cake
        "lunchdining" -> Icons.Default.LunchDining
        "localdrink" -> Icons.Default.LocalDrink
        "person" -> Icons.Default.Person
        "laptop" -> Icons.Default.Laptop
        "assignment" -> Icons.Default.Assignment
        "modeltraining" -> Icons.Default.ModelTraining
        "print" -> Icons.Default.Print
        "localpostoffice" -> Icons.Default.LocalPostOffice
        "soupkitchen" -> Icons.Default.SoupKitchen
        "timetoleave" -> Icons.Default.TimeToLeave
        "childcare" -> Icons.Default.ChildCare
        "elderly" -> Icons.Default.Elderly
        "pets" -> Icons.Default.Pets
        "grass" -> Icons.Default.Grass
        "smartphone" -> Icons.Default.Smartphone
        "laptopmac" -> Icons.Default.LaptopMac
        "videocam" -> Icons.Default.Videocam
        "wifi" -> Icons.Default.Wifi
        "settingsremote" -> Icons.Default.SettingsRemote
        "contentcut" -> Icons.Default.ContentCut
        "faceretouchingnatural" -> Icons.Default.FaceRetouchingNatural
        "invertcolors" -> Icons.Default.InvertColors
        "selfimprovement" -> Icons.Default.SelfImprovement
        "spa" -> Icons.Default.Spa
        "brush" -> Icons.Default.Brush
        "autofixnormal" -> Icons.Default.AutoFixNormal
        "celebration" -> Icons.Default.Celebration
        "draw" -> Icons.Default.Draw
        "close" -> Icons.Default.Close
        "search" -> Icons.Default.Search
        "arrowback" -> Icons.Default.ArrowBack
        else -> Icons.Default.Category // Default icon
    }
}
