package com.cravexa.presentation.seller.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.cravexa.core.designsystem.components.SellerBottomBar
import com.cravexa.core.designsystem.components.SellerTab
import com.cravexa.presentation.seller.dashboard.SellerDashboardScreen
import com.cravexa.presentation.seller.dashboard.SellerDashboardUiState
import com.cravexa.presentation.seller.dashboard.SellerDashboardViewModel
import com.cravexa.presentation.seller.earnings.SellerEarningsScreen
import com.cravexa.presentation.seller.orders.SellerOrdersScreen
import com.cravexa.presentation.seller.products.SellerProductsScreen
import com.cravexa.presentation.seller.profile.SellerProfileScreen

@Composable
fun SellerMainScreen(
    onNavigateToAddProduct: () -> Unit,
    onNavigateToEditProduct: (String) -> Unit,
    onNavigateToOrderDetail: (String) -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateToReviews: () -> Unit,
    onLogout: () -> Unit,
    dashboardViewModel: SellerDashboardViewModel = hiltViewModel()
) {
    var selectedTab by remember { mutableStateOf(SellerTab.DASHBOARD) }
    val dashboardUiState by dashboardViewModel.uiState.collectAsState()

    val pendingOrdersCount = (dashboardUiState as? SellerDashboardUiState.Success)?.stats?.pendingOrders ?: 0
    val lowStockCount = (dashboardUiState as? SellerDashboardUiState.Success)?.stats?.lowStockCount ?: 0

    Scaffold(
        bottomBar = {
            SellerBottomBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it },
                pendingOrdersCount = pendingOrdersCount,
                lowStockCount = lowStockCount
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                SellerTab.DASHBOARD -> {
                    SellerDashboardScreen(
                        onNavigateToAddProduct = onNavigateToAddProduct,
                        onNavigateToOrders = { selectedTab = SellerTab.ORDERS },
                        onNavigateToProducts = { selectedTab = SellerTab.PRODUCTS },
                        onNavigateToEarnings = { selectedTab = SellerTab.EARNINGS },
                        onNavigateToReviews = onNavigateToReviews,
                        onNavigateToProfile = { selectedTab = SellerTab.PROFILE },
                        onNavigateToNotifications = onNavigateToNotifications,
                        onNavigateToOrderDetail = onNavigateToOrderDetail,
                        viewModel = dashboardViewModel
                    )
                }
                SellerTab.ORDERS -> {
                    SellerOrdersScreen(
                        onNavigateToOrderDetail = onNavigateToOrderDetail
                    )
                }
                SellerTab.PRODUCTS -> {
                    SellerProductsScreen(
                        onNavigateToAddProduct = onNavigateToAddProduct,
                        onNavigateToEditProduct = onNavigateToEditProduct
                    )
                }
                SellerTab.EARNINGS -> {
                    SellerEarningsScreen()
                }
                SellerTab.PROFILE -> {
                    SellerProfileScreen(
                        onLogout = onLogout
                    )
                }
            }
        }
    }
}

