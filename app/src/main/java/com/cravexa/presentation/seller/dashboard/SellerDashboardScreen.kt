package com.cravexa.presentation.seller.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cravexa.R
import com.cravexa.core.designsystem.components.CravexaBadge
import com.cravexa.core.designsystem.components.CravexaBadgeType
import com.cravexa.core.designsystem.components.CravexaEmptyState
import com.cravexa.core.designsystem.components.CravexaErrorState
import com.cravexa.core.designsystem.components.CravexaLoadingIndicator
import com.cravexa.core.designsystem.components.SellerStatCard
import com.cravexa.core.designsystem.components.SellerStatusBanner
import com.cravexa.core.designsystem.theme.CravexaOrange500
import com.cravexa.core.designsystem.theme.CravexaPurple800
import com.cravexa.core.designsystem.theme.CravexaPurple900
import com.cravexa.domain.model.FssaiStatus
import com.cravexa.domain.model.Order
import com.cravexa.domain.model.OrderStatus
import com.cravexa.domain.model.SellerAccountStatus
import com.cravexa.domain.model.SellerDashboardStats

@Composable
fun SellerDashboardScreen(
    onNavigateToAddProduct: () -> Unit,
    onNavigateToOrders: () -> Unit,
    onNavigateToProducts: () -> Unit,
    onNavigateToEarnings: () -> Unit,
    onNavigateToReviews: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateToOrderDetail: (String) -> Unit,
    viewModel: SellerDashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = Color(0xFFF9F9FB)
    ) { innerPadding ->
        when (val state = uiState) {
            is SellerDashboardUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CravexaLoadingIndicator(size = 48.dp)
                }
            }
            is SellerDashboardUiState.Error -> {
                CravexaErrorState(
                    message = state.message,
                    onRetry = { viewModel.refresh() },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }
            is SellerDashboardUiState.Success -> {
                SellerDashboardContent(
                    stats = state.stats,
                    profile = state.profile,
                    recentOrders = state.recentOrders,
                    onNavigateToAddProduct = onNavigateToAddProduct,
                    onNavigateToOrders = onNavigateToOrders,
                    onNavigateToProducts = onNavigateToProducts,
                    onNavigateToEarnings = onNavigateToEarnings,
                    onNavigateToReviews = onNavigateToReviews,
                    onNavigateToProfile = onNavigateToProfile,
                    onNavigateToNotifications = onNavigateToNotifications,
                    onNavigateToOrderDetail = onNavigateToOrderDetail,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

@Composable
private fun SellerDashboardContent(
    stats: SellerDashboardStats,
    profile: com.cravexa.domain.model.SellerProfile?,
    recentOrders: List<Order>,
    onNavigateToAddProduct: () -> Unit,
    onNavigateToOrders: () -> Unit,
    onNavigateToProducts: () -> Unit,
    onNavigateToEarnings: () -> Unit,
    onNavigateToReviews: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateToOrderDetail: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Top Creator Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(CravexaPurple900)
                .padding(start = 20.dp, end = 16.dp, top = 20.dp, bottom = 24.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.cravexa_logo),
                            contentDescription = "CRAVEXA Logo",
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "CRAVEXA CREATOR",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    letterSpacing = 1.sp
                                )
                            )
                            Text(
                                text = "KITCHEN STUDIO",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = CravexaOrange500,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.5.sp
                                )
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onNavigateToNotifications) {
                            BadgedBox(
                                badge = {
                                    Badge(
                                        containerColor = CravexaOrange500,
                                        contentColor = Color.White
                                    ) {
                                        Text(text = "2", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_notifications),
                                    contentDescription = "Notifications",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }

                        IconButton(onClick = onNavigateToProfile) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_store),
                                    contentDescription = "Kitchen Profile",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Kitchen Greeting
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(CravexaOrange500.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = (profile?.businessName?.take(1) ?: "L").uppercase(),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = CravexaOrange500
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = profile?.businessName?.ifBlank { "My Home Kitchen" } ?: "Lakshmi's Home Kitchen",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                        Text(
                            text = "Creator: ${profile?.sellerName?.ifBlank { "Home Chef" } ?: "Lakshmi Devi"}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            // Seller Review Status & FSSAI Compliance Banner
            SellerStatusBanner(
                accountStatus = profile?.accountStatus ?: SellerAccountStatus.PENDING,
                fssaiStatus = profile?.fssaiStatus ?: FssaiStatus.NOT_PROVIDED,
                onCompleteFssaiClick = onNavigateToProfile
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Quick Actions Hub
            Text(
                text = "Studio Quick Actions",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E1E2E)
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(horizontal = 2.dp)
            ) {
                item {
                    QuickActionButton(
                        title = "Add Product",
                        iconRes = R.drawable.ic_add,
                        accentColor = CravexaOrange500,
                        onClick = onNavigateToAddProduct
                    )
                }
                item {
                    QuickActionButton(
                        title = "View Orders",
                        iconRes = R.drawable.ic_orders,
                        accentColor = CravexaPurple800,
                        badge = if (stats.pendingOrders > 0) "${stats.pendingOrders}" else null,
                        onClick = onNavigateToOrders
                    )
                }
                item {
                    QuickActionButton(
                        title = "Inventory",
                        iconRes = R.drawable.ic_inventory,
                        accentColor = Color(0xFF2E7D32),
                        badge = if (stats.lowStockCount > 0) "${stats.lowStockCount} Low" else null,
                        onClick = onNavigateToProducts
                    )
                }
                item {
                    QuickActionButton(
                        title = "Earnings",
                        iconRes = R.drawable.ic_earnings,
                        accentColor = Color(0xFF1565C0),
                        onClick = onNavigateToEarnings
                    )
                }
                item {
                    QuickActionButton(
                        title = "Reviews",
                        iconRes = R.drawable.ic_reviews,
                        accentColor = Color(0xFFE65100),
                        onClick = onNavigateToReviews
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Section: Today's Kitchen Performance
            Text(
                text = "Kitchen Overview & Performance",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E1E2E)
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Grid of 4 Core Metric Cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SellerStatCard(
                    title = "Today's Sales",
                    value = if (stats.todaySales > 0) "₹${stats.todaySales.toInt()}" else "₹0",
                    subtitle = if (stats.todaySales > 0) "Live orders today" else "No sales data yet",
                    iconPainter = painterResource(id = R.drawable.ic_earnings),
                    accentColor = CravexaOrange500,
                    modifier = Modifier.weight(1f)
                )

                SellerStatCard(
                    title = "Total Orders",
                    value = stats.totalOrders.toString(),
                    subtitle = "${stats.pendingOrders} pending prep",
                    iconPainter = painterResource(id = R.drawable.ic_orders),
                    accentColor = CravexaPurple800,
                    onClick = onNavigateToOrders,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SellerStatCard(
                    title = "Kitchen Catalog",
                    value = "${stats.totalProducts} Dishes",
                    subtitle = if (stats.lowStockCount > 0) "${stats.lowStockCount} Low stock" else "${stats.activeProducts} Active dishes",
                    iconPainter = painterResource(id = R.drawable.ic_inventory),
                    accentColor = if (stats.lowStockCount > 0) Color(0xFFE65100) else Color(0xFF2E7D32),
                    onClick = onNavigateToProducts,
                    modifier = Modifier.weight(1f)
                )

                SellerStatCard(
                    title = "Customer Rating",
                    value = "${stats.averageRating} ★",
                    subtitle = "${stats.totalReviews} creator reviews",
                    iconPainter = painterResource(id = R.drawable.ic_star),
                    accentColor = Color(0xFFFF8F00),
                    onClick = onNavigateToReviews,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Payout Balance Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onNavigateToEarnings() },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CravexaPurple800)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Available Payout Balance",
                            style = MaterialTheme.typography.bodySmall.copy(color = Color.White.copy(alpha = 0.8f))
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "₹${stats.availableBalance.toInt()}",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Next settlement scheduled via NEFT",
                            style = MaterialTheme.typography.labelSmall.copy(color = CravexaOrange500)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.White.copy(alpha = 0.15f))
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "View Payouts →",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Section: Recent Live Orders
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Active Kitchen Orders",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E1E2E)
                    )
                )

                TextButton(onClick = onNavigateToOrders) {
                    Text(
                        text = "View All (${stats.totalOrders})",
                        color = CravexaOrange500,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (recentOrders.isEmpty()) {
                CravexaEmptyState(
                    title = "No orders yet",
                    description = "When customers purchase your handmade delicacies, new orders will appear here in real-time."
                )
            } else {
                recentOrders.forEach { order ->
                    RecentOrderCard(
                        order = order,
                        onClick = { onNavigateToOrderDetail(order.id) }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun QuickActionButton(
    title: String,
    iconRes: Int,
    accentColor: Color,
    onClick: () -> Unit,
    badge: String? = null
) {
    Card(
        modifier = Modifier
            .width(106.dp)
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(accentColor.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = title,
                        tint = accentColor,
                        modifier = Modifier.size(22.dp)
                    )
                }

                if (badge != null) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .clip(RoundedCornerShape(6.dp))
                            .background(CravexaOrange500)
                            .padding(horizontal = 4.dp, vertical = 1.dp)
                    ) {
                        Text(
                            text = badge,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF212121)
                ),
                maxLines = 1
            )
        }
    }
}

@Composable
private fun RecentOrderCard(
    order: Order,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Order #${order.orderNumber}",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    val statusBadge = when (order.orderStatus) {
                        OrderStatus.ORDER_PLACED, OrderStatus.PAYMENT_CONFIRMED -> CravexaBadgeType.WARNING
                        OrderStatus.PREPARING -> CravexaBadgeType.FOOD_TAG
                        OrderStatus.READY_FOR_PICKUP -> CravexaBadgeType.PRIMARY
                        OrderStatus.DELIVERED -> CravexaBadgeType.SUCCESS
                        else -> CravexaBadgeType.PRIMARY
                    }
                    CravexaBadge(
                        text = order.orderStatus.displayTitle.uppercase(),
                        type = statusBadge
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = order.itemsSummary,
                    style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF616161)),
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "${order.orderDate} • ₹${order.totalAmount.toInt()}",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = CravexaPurple800,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }

            Icon(
                painter = painterResource(id = R.drawable.ic_chevron_right),
                contentDescription = "Order Details",
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
