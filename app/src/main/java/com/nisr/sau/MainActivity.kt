package com.nisr.sau

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nisr.sau.utils.UserSession

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppNavigation()
        }
    }
}

@Composable
fun AppNavigation() {

    val navController = rememberNavController()
    val forgotPasswordViewModel: ForgotPasswordViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen(navController)
        }

        composable("logo") {
            LogoScreen(navController)
        }

        composable("onboarding") {
            OnboardingScreen(navController)
        }

        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onRegisterClick = {
                    navController.navigate("register")
                },
                onForgotClick = {
                    navController.navigate("forgot_password")
                }
            )
        }

        composable("register") {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                },
                onSignInClick = {
                    navController.navigate("login")
                }
            )
        }

        composable("forgot_password") {
            ForgotPasswordEmailScreen(
                viewModel = forgotPasswordViewModel,
                onBackClick = { navController.popBackStack() },
                onCloseClick = { navController.navigate("login") { popUpTo("login") { inclusive = true } } },
                onNext = { navController.navigate("recovery_options") }
            )
        }

        composable("recovery_options") {
            RecoveryOptionScreen(
                viewModel = forgotPasswordViewModel,
                onBackClick = { navController.popBackStack() },
                onCloseClick = { navController.navigate("login") { popUpTo("login") { inclusive = true } } },
                onNext = { navController.navigate("otp_verification") }
            )
        }

        composable("otp_verification") {
            OtpVerificationScreen(
                viewModel = forgotPasswordViewModel,
                onBackClick = { navController.popBackStack() },
                onCloseClick = { navController.navigate("login") { popUpTo("login") { inclusive = true } } },
                onVerifySuccess = {
                    navController.navigate("login") { popUpTo("login") { inclusive = true } }
                }
            )
        }

        composable("home") {
            HomeScreen(navController, UserSession.username, UserSession.location)
        }

        composable(
            "all_categories?search={search}",
            arguments = listOf(navArgument("search") { defaultValue = "" })
        ) { backStackEntry ->
            val search = backStackEntry.arguments?.getString("search") ?: ""
            AllCategoriesScreen(navController, initialSearch = search)
        }

        composable(
            "product_list/{subcategory}",
            arguments = listOf(navArgument("subcategory") { defaultValue = "Vegetables" })
        ) { backStackEntry ->
            val subcategory = backStackEntry.arguments?.getString("subcategory") ?: "Vegetables"
            ProductListScreen(navController, subcategory)
        }

        composable("product_detail/{productId}") { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            ProductDetailScreen(navController, productId)
        }

        composable(
            "bookings?serviceName={serviceName}",
            arguments = listOf(navArgument("serviceName") { defaultValue = "AC Deep Cleaning" })
        ) { backStackEntry ->
            val serviceName = backStackEntry.arguments?.getString("serviceName") ?: "AC Deep Cleaning"
            BookingScreen(navController, serviceName)
        }

        composable("booking_success") {
            BookingSuccessScreen(navController)
        }

        composable("profile") {
            ProfileScreen(
                onEditProfile = {
                    navController.navigate("edit_profile")
                },
                onCartClick = {
                    navController.navigate("cart")
                },
                onLogout = {
                    UserSession.clear()
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                navController = navController
            )
        }

        composable("edit_profile") {
            EditProfileScreen()
        }

        composable("cart") {
            CartScreen(navController)
        }
        
        composable("orders") {
            OrdersScreen(navController)
        }
        
        composable("wishlist") {
            WishlistScreen(navController)
        }
        
        composable("my_bookings") {
            BookingsScreen(navController)
        }
    }
}
