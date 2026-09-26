package com.cravexa.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.cravexa.domain.model.UserRole
import com.cravexa.presentation.admin.login.AdminLoginScreen
import com.cravexa.presentation.admin.main.AdminMainScreen
import com.cravexa.presentation.admin.password.AdminChangePasswordScreen
import com.cravexa.presentation.auth.forgotpassword.ForgotPasswordScreen
import com.cravexa.presentation.auth.login.LoginScreen
import com.cravexa.presentation.auth.phone.PhoneLoginScreen
import com.cravexa.presentation.auth.profile.ProfileSetupScreen
import com.cravexa.presentation.auth.signup.SignupScreen
import com.cravexa.presentation.category.CategoryProductsScreen
import com.cravexa.presentation.customer.address.AddEditAddressScreen
import com.cravexa.presentation.customer.address.AddressScreen
import com.cravexa.presentation.customer.address.AddressViewModel
import com.cravexa.presentation.customer.main.CustomerMainScreen
import com.cravexa.presentation.customer.orders.OrderDetailScreen
import com.cravexa.presentation.customer.orders.OrderTrackingScreen
import com.cravexa.presentation.customer.orders.OrdersScreen
import com.cravexa.presentation.customer.profile.CustomerProfileScreen
import com.cravexa.presentation.customer.profile.EditProfileScreen
import com.cravexa.presentation.home.HomeScreen
import com.cravexa.presentation.onboarding.OnboardingScreen
import com.cravexa.presentation.product.ProductDetailScreen
import com.cravexa.presentation.search.SearchScreen
import com.cravexa.presentation.seller.main.SellerMainScreen
import com.cravexa.presentation.seller.notifications.SellerNotificationsScreen
import com.cravexa.presentation.seller.orders.SellerOrderDetailScreen
import com.cravexa.presentation.seller.products.AddEditProductScreen
import com.cravexa.presentation.seller.profile.SellerProfileScreen
import com.cravexa.presentation.seller.publicprofile.PublicSellerProfileScreen
import com.cravexa.presentation.seller.reviews.SellerReviewsScreen
import com.cravexa.presentation.cart.CartScreen
import com.cravexa.presentation.checkout.CheckoutScreen
import com.cravexa.presentation.checkout.OrderSuccessScreen
import com.cravexa.presentation.welcome.WelcomeScreen
import com.cravexa.presentation.splash.SplashScreen

@Composable
fun CravexaNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = Screen.Splash.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // Splash Screen
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToNext = { destination ->
                    navController.navigate(destination.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        // Welcome Screen
        composable(Screen.Welcome.route) {
            WelcomeScreen(
                onGetStarted = {
                    navController.navigate(Screen.Onboarding.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                },
                onSignIn = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                }
            )
        }

        // Onboarding Screen
        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onFinishOnboarding = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        // Authentication Screens
        composable(Screen.Login.route) {
            LoginScreen(
                onNavigate = { destination ->
                    when (destination) {
                        is Screen.Home, is Screen.CustomerMain -> {
                            navController.navigate(Screen.CustomerMain.route) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        }
                        is Screen.SellerHome, is Screen.SellerMain -> {
                            navController.navigate(Screen.SellerHome.route) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        }
                        is Screen.AdminHome, is Screen.AdminMain -> {
                            navController.navigate(Screen.AdminMain.route) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        }
                        is Screen.ProfileSetup -> {
                            navController.navigate(Screen.ProfileSetup.route) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        }
                        else -> {
                            navController.navigate(destination.route)
                        }
                    }
                }
            )
        }

        composable(Screen.Signup.route) {
            SignupScreen(
                onNavigate = { destination ->
                    navController.navigate(destination.route) {
                        popUpTo(Screen.Signup.route) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen(
                onNavigate = { destination ->
                    navController.navigate(destination.route) {
                        popUpTo(Screen.ForgotPassword.route) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.PhoneLogin.route) {
            PhoneLoginScreen(
                onNavigate = { destination ->
                    when (destination) {
                        is Screen.Home, is Screen.CustomerMain -> {
                            navController.navigate(Screen.CustomerMain.route) {
                                popUpTo(Screen.PhoneLogin.route) { inclusive = true }
                            }
                        }
                        is Screen.SellerHome, is Screen.SellerMain -> {
                            navController.navigate(Screen.SellerHome.route) {
                                popUpTo(Screen.PhoneLogin.route) { inclusive = true }
                            }
                        }
                        is Screen.ProfileSetup -> {
                            navController.navigate(Screen.ProfileSetup.route) {
                                popUpTo(Screen.PhoneLogin.route) { inclusive = true }
                            }
                        }
                        else -> {
                            navController.navigate(destination.route)
                        }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.ProfileSetup.route) {
            ProfileSetupScreen(
                onFinish = { role ->
                    val destination = when (role) {
                        UserRole.SELLER -> Screen.SellerHome
                        UserRole.ADMIN -> Screen.AdminHome
                        UserRole.CUSTOMER -> Screen.CustomerMain
                    }
                    navController.navigate(destination.route) {
                        popUpTo(Screen.ProfileSetup.route) { inclusive = true }
                    }
                }
            )
        }

        // Customer Main Container (Bottom Navigation: Home, Explore, Orders, Wishlist, Profile)
        composable(Screen.CustomerMain.route) {
            CustomerMainScreen(
                onNavigate = { destination ->
                    navController.navigate(destination.route)
                },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.CustomerMain.route) { inclusive = true }
                    }
                }
            )
        }

        // Standalone Home fallback route
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToSearch = { query ->
                    navController.navigate(Screen.Search.createRoute(query ?: ""))
                },
                onNavigateToCategory = { catId, catName ->
                    navController.navigate(Screen.CategoryProducts.createRoute(catId, catName))
                },
                onNavigateToProduct = { prodId ->
                    navController.navigate(Screen.ProductDetail.createRoute(prodId))
                },
                onNavigateToSeller = { selId ->
                    navController.navigate(Screen.PublicSellerProfile.createRoute(selId))
                },
                onNavigateToCart = {
                    navController.navigate(Screen.Cart.route)
                },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }

        // Cart Screen
        composable(Screen.Cart.route) {
            CartScreen(
                onBack = { navController.popBackStack() },
                onNavigateToProduct = { productId ->
                    navController.navigate(Screen.ProductDetail.createRoute(productId))
                },
                onExploreFood = {
                    navController.navigate(Screen.CustomerMain.route) {
                        popUpTo(Screen.CustomerMain.route) { inclusive = false }
                    }
                },
                onProceedToCheckout = {
                    navController.navigate(Screen.Checkout.route)
                }
            )
        }

        // Checkout & Order Review Screen
        composable(Screen.Checkout.route) {
            CheckoutScreen(
                onOrderPlaced = { orderId ->
                    navController.navigate(Screen.OrderSuccess.createRoute(orderId)) {
                        popUpTo(Screen.Cart.route) { inclusive = true }
                    }
                },
                onNavigateToAddAddress = {
                    navController.navigate(Screen.AddEditAddress.route)
                },
                onRequireLogin = {
                    navController.navigate(Screen.Login.route)
                },
                onBack = { navController.popBackStack() }
            )
        }

        // Order Success Screen
        composable(
            route = Screen.OrderSuccess.ROUTE_TEMPLATE,
            arguments = listOf(navArgument("orderId") { type = NavType.StringType })
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
            OrderSuccessScreen(
                orderId = orderId,
                onTrackOrder = { id ->
                    navController.navigate(Screen.OrderTracking.createRoute(id)) {
                        popUpTo(Screen.CustomerMain.route) { inclusive = false }
                    }
                },
                onViewOrder = { id ->
                    navController.navigate(Screen.OrderDetail.createRoute(id)) {
                        popUpTo(Screen.CustomerMain.route) { inclusive = false }
                    }
                },
                onContinueShopping = {
                    navController.navigate(Screen.CustomerMain.route) {
                        popUpTo(Screen.CustomerMain.route) { inclusive = true }
                    }
                }
            )
        }

        // Category Products Screen
        composable(
            route = Screen.CategoryProducts.ROUTE_TEMPLATE,
            arguments = listOf(
                navArgument("categoryId") { type = NavType.StringType },
                navArgument("categoryName") { type = NavType.StringType }
            )
        ) {
            CategoryProductsScreen(
                onNavigateToProduct = { productId ->
                    navController.navigate(Screen.ProductDetail.createRoute(productId))
                },
                onBack = { navController.popBackStack() }
            )
        }

        // Product Details Screen
        composable(
            route = Screen.ProductDetail.ROUTE_TEMPLATE,
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) {
            ProductDetailScreen(
                onNavigateToSeller = { sellerId ->
                    navController.navigate(Screen.PublicSellerProfile.createRoute(sellerId))
                },
                onNavigateToProduct = { productId ->
                    navController.navigate(Screen.ProductDetail.createRoute(productId))
                },
                onBack = { navController.popBackStack() }
            )
        }

        // Search Screen
        composable(
            route = Screen.Search.ROUTE_TEMPLATE,
            arguments = listOf(navArgument("query") {
                type = NavType.StringType
                defaultValue = ""
            })
        ) {
            SearchScreen(
                onNavigateToProduct = { productId ->
                    navController.navigate(Screen.ProductDetail.createRoute(productId))
                },
                onBack = { navController.popBackStack() }
            )
        }

        // Public Seller Discovery Profile Screen
        composable(
            route = Screen.PublicSellerProfile.ROUTE_TEMPLATE,
            arguments = listOf(navArgument("sellerId") { type = NavType.StringType })
        ) {
            PublicSellerProfileScreen(
                onNavigateToProduct = { productId ->
                    navController.navigate(Screen.ProductDetail.createRoute(productId))
                },
                onBack = { navController.popBackStack() }
            )
        }

        // Customer Profile Screen
        composable(Screen.Profile.route) {
            CustomerProfileScreen(
                onNavigate = { destination ->
                    navController.navigate(destination.route)
                },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.CustomerMain.route) { inclusive = true }
                    }
                }
            )
        }

        // Edit Profile Screen
        composable(Screen.EditProfile.route) {
            EditProfileScreen(
                onBack = { navController.popBackStack() }
            )
        }

        // Address Management Screens
        composable(Screen.AddressList.route) { backStackEntry ->
            val addressViewModel: AddressViewModel = hiltViewModel(backStackEntry)
            AddressScreen(
                onAddNewAddress = { navController.navigate(Screen.AddEditAddress.route) },
                onEditAddress = { navController.navigate(Screen.AddEditAddress.route) },
                onBack = { navController.popBackStack() },
                viewModel = addressViewModel
            )
        }

        composable(Screen.AddEditAddress.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                try {
                    navController.getBackStackEntry(Screen.AddressList.route)
                } catch (e: Exception) {
                    backStackEntry
                }
            }
            val addressViewModel: AddressViewModel = hiltViewModel(parentEntry)
            AddEditAddressScreen(
                onBack = { navController.popBackStack() },
                viewModel = addressViewModel
            )
        }

        // Customer Orders Screen
        composable(Screen.Orders.route) {
            OrdersScreen(
                onViewOrder = { orderId ->
                    navController.navigate(Screen.OrderDetail.createRoute(orderId))
                },
                onTrackOrder = { orderId ->
                    navController.navigate(Screen.OrderTracking.createRoute(orderId))
                },
                showBackButton = true,
                onBack = { navController.popBackStack() }
            )
        }

        // Order Details Screen
        composable(
            route = Screen.OrderDetail.ROUTE_TEMPLATE,
            arguments = listOf(navArgument("orderId") { type = NavType.StringType })
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
            OrderDetailScreen(
                orderId = orderId,
                onTrackOrder = { id ->
                    navController.navigate(Screen.OrderTracking.createRoute(id))
                },
                onBack = { navController.popBackStack() }
            )
        }

        // Order Tracking Screen
        composable(
            route = Screen.OrderTracking.ROUTE_TEMPLATE,
            arguments = listOf(navArgument("orderId") { type = NavType.StringType })
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
            OrderTrackingScreen(
                orderId = orderId,
                onBack = { navController.popBackStack() }
            )
        }

        // --- Phase 5 Seller Screens ---
        composable(Screen.SellerHome.route) {
            SellerMainScreen(
                onNavigateToAddProduct = {
                    navController.navigate(Screen.AddProduct.route)
                },
                onNavigateToEditProduct = { productId ->
                    navController.navigate(Screen.EditProduct.createRoute(productId))
                },
                onNavigateToOrderDetail = { orderId ->
                    navController.navigate(Screen.SellerOrderDetail.createRoute(orderId))
                },
                onNavigateToNotifications = {
                    navController.navigate(Screen.SellerNotifications.route)
                },
                onNavigateToReviews = {
                    navController.navigate(Screen.SellerReviews.route)
                },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.SellerHome.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.AddProduct.route) {
            AddEditProductScreen(
                onBack = { navController.popBackStack() },
                onProductSaved = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.EditProduct.ROUTE_TEMPLATE,
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) {
            AddEditProductScreen(
                onBack = { navController.popBackStack() },
                onProductSaved = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.SellerOrderDetail.ROUTE_TEMPLATE,
            arguments = listOf(navArgument("orderId") { type = NavType.StringType })
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
            SellerOrderDetailScreen(
                orderId = orderId,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.SellerNotifications.route) {
            SellerNotificationsScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.SellerReviews.route) {
            SellerReviewsScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.SellerProfile.route) {
            SellerProfileScreen(
                showBackButton = true,
                onBack = { navController.popBackStack() },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.SellerHome.route) { inclusive = true }
                    }
                }
            )
        }

        // Phase 6 Admin Screens
        composable(Screen.AdminLogin.route) {
            AdminLoginScreen(
                onNavigateToDashboard = {
                    navController.navigate(Screen.AdminMain.route) {
                        popUpTo(Screen.AdminLogin.route) { inclusive = true }
                    }
                },
                onNavigateToChangePassword = { isForced ->
                    navController.navigate(Screen.AdminChangePassword.createRoute(isForced)) {
                        popUpTo(Screen.AdminLogin.route) { inclusive = true }
                    }
                },
                onBackToPublicLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.AdminMain.route) {
            AdminMainScreen(
                onNavigateToChangePassword = {
                    navController.navigate(Screen.AdminChangePassword.createRoute(false))
                },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.AdminMain.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.AdminHome.route) {
            AdminMainScreen(
                onNavigateToChangePassword = {
                    navController.navigate(Screen.AdminChangePassword.createRoute(false))
                },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.AdminHome.route) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Screen.AdminChangePassword.ROUTE_TEMPLATE,
            arguments = listOf(
                navArgument("isForced") {
                    type = NavType.BoolType
                    defaultValue = false
                }
            )
        ) { backStackEntry ->
            val isForced = backStackEntry.arguments?.getBoolean("isForced") ?: false
            AdminChangePasswordScreen(
                isForced = isForced,
                onSuccess = {
                    navController.navigate(Screen.AdminMain.route) {
                        popUpTo(Screen.AdminChangePassword.ROUTE_TEMPLATE) { inclusive = true }
                    }
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
