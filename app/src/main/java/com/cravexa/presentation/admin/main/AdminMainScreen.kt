package com.cravexa.presentation.admin.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cravexa.R
import com.cravexa.core.designsystem.theme.CravexaOrange500
import com.cravexa.core.designsystem.theme.CravexaPurple800
import com.cravexa.presentation.admin.complaints.AdminComplaintsScreen
import com.cravexa.presentation.admin.dashboard.AdminDashboardScreen
import com.cravexa.presentation.admin.fssai.AdminFssaiScreen
import com.cravexa.presentation.admin.marketplace.AdminMarketplaceScreen
import com.cravexa.presentation.admin.orders.AdminOrdersScreen
import com.cravexa.presentation.admin.payments.AdminPaymentsScreen
import com.cravexa.presentation.admin.products.AdminProductsScreen
import com.cravexa.presentation.admin.sellers.AdminSellersScreen
import com.cravexa.presentation.admin.settings.AdminSettingsScreen
import com.cravexa.presentation.admin.users.AdminUsersScreen

enum class AdminSection(val title: String, val icon: ImageVector) {
    DASHBOARD("Dashboard", Icons.Default.Dashboard),
    USERS("Users", Icons.Default.People),
    SELLERS("Sellers", Icons.Default.Storefront),
    FSSAI("FSSAI", Icons.Default.VerifiedUser),
    PRODUCTS("Products", Icons.Default.Inventory2),
    ORDERS("Orders", Icons.Default.ReceiptLong),
    PAYMENTS("Payments", Icons.Default.AccountBalanceWallet),
    MARKETPLACE("Marketplace", Icons.Default.Category),
    COMPLAINTS("Complaints", Icons.Default.SupportAgent),
    SETTINGS("Settings", Icons.Default.Settings)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminMainScreen(
    onNavigateToChangePassword: () -> Unit,
    onLogout: () -> Unit
) {
    var currentSection by remember { mutableStateOf(AdminSection.DASHBOARD) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_cravexa_logo),
                            contentDescription = "CRAVEXA Logo",
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "CRAVEXA ADMIN",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 1.sp,
                                    color = Color.White
                                )
                            )
                            Text(
                                text = currentSection.title,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = CravexaOrange500,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(
                            imageVector = Icons.Default.Logout,
                            contentDescription = "Logout Admin",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CravexaPurple800,
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            ScrollableTabRow(
                selectedTabIndex = currentSection.ordinal,
                containerColor = Color.White,
                contentColor = CravexaPurple800,
                edgePadding = 8.dp
            ) {
                AdminSection.entries.forEach { section ->
                    val isSelected = currentSection == section
                    Tab(
                        selected = isSelected,
                        onClick = { currentSection = section },
                        text = {
                            Text(
                                text = section.title,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) CravexaPurple800 else Color.Gray,
                                fontSize = 12.sp
                            )
                        },
                        icon = {
                            Icon(
                                imageVector = section.icon,
                                contentDescription = section.title,
                                tint = if (isSelected) CravexaOrange500 else Color.Gray,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentSection) {
                AdminSection.DASHBOARD -> {
                    AdminDashboardScreen(
                        onNavigateToSellers = { currentSection = AdminSection.SELLERS },
                        onNavigateToFssai = { currentSection = AdminSection.FSSAI },
                        onNavigateToProducts = { currentSection = AdminSection.PRODUCTS },
                        onNavigateToOrders = { currentSection = AdminSection.ORDERS },
                        onNavigateToPayments = { currentSection = AdminSection.PAYMENTS },
                        onNavigateToComplaints = { currentSection = AdminSection.COMPLAINTS }
                    )
                }
                AdminSection.USERS -> AdminUsersScreen()
                AdminSection.SELLERS -> AdminSellersScreen()
                AdminSection.FSSAI -> AdminFssaiScreen()
                AdminSection.PRODUCTS -> AdminProductsScreen()
                AdminSection.ORDERS -> AdminOrdersScreen()
                AdminSection.PAYMENTS -> AdminPaymentsScreen()
                AdminSection.MARKETPLACE -> AdminMarketplaceScreen()
                AdminSection.COMPLAINTS -> AdminComplaintsScreen()
                AdminSection.SETTINGS -> {
                    AdminSettingsScreen(
                        onNavigateToChangePassword = onNavigateToChangePassword,
                        onLogout = onLogout
                    )
                }
            }
        }
    }
}
