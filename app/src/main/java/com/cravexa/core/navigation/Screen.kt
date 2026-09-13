package com.cravexa.core.navigation

sealed class Screen(val route: String) {
    data object Splash : Screen("splash_screen")
    data object Welcome : Screen("welcome_screen")
    data object Onboarding : Screen("onboarding_screen")
    data object Login : Screen("login_screen")
    data object Signup : Screen("signup_screen")
    data object ForgotPassword : Screen("forgot_password_screen")
    data object PhoneLogin : Screen("phone_login_screen")
    data object ProfileSetup : Screen("profile_setup_screen")
    data object CustomerMain : Screen("customer_main_screen")
    data object Home : Screen("home_screen")
    data object Cart : Screen("cart_screen")
    data object Checkout : Screen("checkout_screen")
    data class OrderSuccess(val orderId: String = "{orderId}") : Screen("order_success_screen/$orderId") {
        companion object {
            const val ROUTE_TEMPLATE = "order_success_screen/{orderId}"
            fun createRoute(orderId: String) = "order_success_screen/$orderId"
        }
    }
    data object Profile : Screen("profile_screen")
    data object EditProfile : Screen("edit_profile_screen")
    data object AddressList : Screen("address_list_screen")
    data object AddEditAddress : Screen("add_edit_address_screen")
    data object Orders : Screen("orders_screen")
    data class OrderDetail(val orderId: String = "{orderId}") : Screen("order_detail_screen/$orderId") {
        companion object {
            const val ROUTE_TEMPLATE = "order_detail_screen/{orderId}"
            fun createRoute(orderId: String) = "order_detail_screen/$orderId"
        }
    }
    data class OrderTracking(val orderId: String = "{orderId}") : Screen("order_tracking_screen/$orderId") {
        companion object {
            const val ROUTE_TEMPLATE = "order_tracking_screen/{orderId}"
            fun createRoute(orderId: String) = "order_tracking_screen/$orderId"
        }
    }

    // Phase 4 Customer Marketplace routes
    data class CategoryProducts(
        val categoryId: String = "{categoryId}",
        val categoryName: String = "{categoryName}"
    ) : Screen("category_products_screen/$categoryId/$categoryName") {
        companion object {
            const val ROUTE_TEMPLATE = "category_products_screen/{categoryId}/{categoryName}"
            fun createRoute(categoryId: String, categoryName: String) =
                "category_products_screen/$categoryId/$categoryName"
        }
    }

    data class ProductDetail(val productId: String = "{productId}") : Screen("product_detail_screen/$productId") {
        companion object {
            const val ROUTE_TEMPLATE = "product_detail_screen/{productId}"
            fun createRoute(productId: String) = "product_detail_screen/$productId"
        }
    }

    data class Search(val query: String = "") : Screen("search_screen?query=$query") {
        companion object {
            const val ROUTE_TEMPLATE = "search_screen?query={query}"
            fun createRoute(query: String = "") = "search_screen?query=$query"
        }
    }

    data class PublicSellerProfile(val sellerId: String = "{sellerId}") : Screen("public_seller_profile_screen/$sellerId") {
        companion object {
            const val ROUTE_TEMPLATE = "public_seller_profile_screen/{sellerId}"
            fun createRoute(sellerId: String) = "public_seller_profile_screen/$sellerId"
        }
    }

    // Phase 5 Seller Dashboard routes
    data object SellerHome : Screen("seller_home_screen")
    data object SellerMain : Screen("seller_main_screen")
    data object AddProduct : Screen("add_product_screen")
    data class EditProduct(val productId: String = "{productId}") : Screen("edit_product_screen/$productId") {
        companion object {
            const val ROUTE_TEMPLATE = "edit_product_screen/{productId}"
            fun createRoute(productId: String) = "edit_product_screen/$productId"
        }
    }
    data class SellerOrderDetail(val orderId: String = "{orderId}") : Screen("seller_order_detail_screen/$orderId") {
        companion object {
            const val ROUTE_TEMPLATE = "seller_order_detail_screen/{orderId}"
            fun createRoute(orderId: String) = "seller_order_detail_screen/$orderId"
        }
    }
    data object SellerNotifications : Screen("seller_notifications_screen")
    data object SellerReviews : Screen("seller_reviews_screen")
    data object SellerProfile : Screen("seller_profile_screen")
    // Phase 6 Admin Dashboard routes
    data object AdminHome : Screen("admin_home_screen")
    data object AdminLogin : Screen("admin_login_screen")
    data object AdminMain : Screen("admin_main_screen")
    data class AdminChangePassword(val isForced: Boolean = false) : Screen("admin_change_password_screen?isForced=$isForced") {
        companion object {
            const val ROUTE_TEMPLATE = "admin_change_password_screen?isForced={isForced}"
            fun createRoute(isForced: Boolean = false) = "admin_change_password_screen?isForced=$isForced"
        }
    }
}
