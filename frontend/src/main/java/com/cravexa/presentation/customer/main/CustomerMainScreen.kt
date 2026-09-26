package com.cravexa.presentation.customer.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cravexa.R
import com.cravexa.core.designsystem.theme.CravexaOrange500
import com.cravexa.core.designsystem.theme.CravexaPurple100
import com.cravexa.core.navigation.Screen
import com.cravexa.presentation.customer.orders.OrdersScreen
import com.cravexa.presentation.customer.profile.CustomerProfileScreen
import com.cravexa.presentation.explore.ExploreScreen
import com.cravexa.presentation.home.HomeScreen
import com.cravexa.presentation.wishlist.WishlistScreen

enum class CustomerTab(val title: String) {
    HOME("Home"),
    EXPLORE("Explore"),
    ORDERS("Orders"),
    WISHLIST("Wishlist"),
    PROFILE("Profile")
}

@Composable
fun CustomerMainScreen(
    onNavigate: (Screen) -> Unit,
    onLogout: () -> Unit
) {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                CustomerTab.entries.forEachIndexed { index, tab ->
                    val icon: Painter = when (tab) {
                        CustomerTab.HOME -> painterResource(id = R.drawable.ic_home)
                        CustomerTab.EXPLORE -> painterResource(id = R.drawable.ic_search)
                        CustomerTab.ORDERS -> painterResource(id = R.drawable.ic_orders)
                        CustomerTab.WISHLIST -> painterResource(id = R.drawable.ic_heart)
                        CustomerTab.PROFILE -> painterResource(id = R.drawable.ic_person)
                    }

                    val isSelected = selectedTab == index

                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { selectedTab = index },
                        icon = {
                            Icon(
                                painter = icon,
                                contentDescription = tab.title,
                                modifier = Modifier.size(22.dp)
                            )
                        },
                        label = {
                            Text(
                                text = tab.title,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = CravexaOrange500,
                            selectedTextColor = CravexaOrange500,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            indicatorColor = CravexaPurple100
                        )
                    )
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (CustomerTab.entries[selectedTab]) {
                CustomerTab.HOME -> {
                    HomeScreen(
                        onNavigateToSearch = { query ->
                            onNavigate(Screen.Search(query ?: ""))
                        },
                        onNavigateToCategory = { catId, catName ->
                            onNavigate(Screen.CategoryProducts.createRoute(catId, catName).let {
                                Screen.CategoryProducts(catId, catName)
                            })
                        },
                        onNavigateToProduct = { prodId ->
                            onNavigate(Screen.ProductDetail(prodId))
                        },
                        onNavigateToSeller = { selId ->
                            onNavigate(Screen.PublicSellerProfile(selId))
                        },
                        onNavigateToCart = {
                            onNavigate(Screen.Cart)
                        },
                        onLogout = onLogout
                    )
                }
                CustomerTab.EXPLORE -> {
                    ExploreScreen(
                        onNavigateToSearch = { query ->
                            onNavigate(Screen.Search(query ?: ""))
                        },
                        onNavigateToCategory = { catId, catName ->
                            onNavigate(Screen.CategoryProducts(catId, catName))
                        },
                        onNavigateToProduct = { prodId ->
                            onNavigate(Screen.ProductDetail(prodId))
                        },
                        onNavigateToSeller = { selId ->
                            onNavigate(Screen.PublicSellerProfile(selId))
                        }
                    )
                }
                CustomerTab.ORDERS -> {
                    OrdersScreen(
                        onViewOrder = { orderId -> onNavigate(Screen.OrderDetail(orderId)) },
                        onTrackOrder = { orderId -> onNavigate(Screen.OrderTracking(orderId)) },
                        onExploreFood = { selectedTab = 0 }
                    )
                }
                CustomerTab.WISHLIST -> {
                    WishlistScreen(
                        onNavigateToProduct = { prodId ->
                            onNavigate(Screen.ProductDetail(prodId))
                        },
                        onExploreFood = { selectedTab = 0 }
                    )
                }
                CustomerTab.PROFILE -> {
                    CustomerProfileScreen(
                        onNavigate = onNavigate,
                        onLogout = onLogout
                    )
                }
            }
        }
    }
}
