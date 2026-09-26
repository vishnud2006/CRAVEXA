package com.cravexa.presentation.admin.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cravexa.core.designsystem.theme.CravexaOrange500
import com.cravexa.core.designsystem.theme.CravexaPurple800
import com.cravexa.domain.model.AdminDashboardStats

@Composable
fun AdminDashboardScreen(
    onNavigateToSellers: () -> Unit,
    onNavigateToFssai: () -> Unit,
    onNavigateToProducts: () -> Unit,
    onNavigateToOrders: () -> Unit,
    onNavigateToPayments: () -> Unit,
    onNavigateToComplaints: () -> Unit,
    viewModel: AdminDashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    when (val state = uiState) {
        is AdminDashboardUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = CravexaPurple800)
            }
        }
        is AdminDashboardUiState.Error -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = state.message,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
            }
        }
        is AdminDashboardUiState.Success -> {
            AdminDashboardContent(
                stats = state.stats,
                onNavigateToSellers = onNavigateToSellers,
                onNavigateToFssai = onNavigateToFssai,
                onNavigateToProducts = onNavigateToProducts,
                onNavigateToOrders = onNavigateToOrders,
                onNavigateToPayments = onNavigateToPayments,
                onNavigateToComplaints = onNavigateToComplaints
            )
        }
    }
}

@Composable
private fun AdminDashboardContent(
    stats: AdminDashboardStats,
    onNavigateToSellers: () -> Unit,
    onNavigateToFssai: () -> Unit,
    onNavigateToProducts: () -> Unit,
    onNavigateToOrders: () -> Unit,
    onNavigateToPayments: () -> Unit,
    onNavigateToComplaints: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Environment Disclaimer
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFEDE7F6),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = null,
                    tint = CravexaPurple800,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "CRAVEXA Operations Control • Active Admin Console",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = CravexaPurple800
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Key Action Alerts Queue
        if (stats.pendingSellerApprovals > 0 || stats.fssaiVerificationRequests > 0 || stats.pendingProductApprovals > 0) {
            Text(
                text = "Action Items Required",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF1E1E2E)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (stats.pendingSellerApprovals > 0) {
                    AdminAlertCard(
                        title = "${stats.pendingSellerApprovals} New Seller Applications",
                        subtitle = "Food creators awaiting onboarding review",
                        icon = Icons.Default.Storefront,
                        color = CravexaOrange500,
                        onClick = onNavigateToSellers
                    )
                }
                if (stats.fssaiVerificationRequests > 0) {
                    AdminAlertCard(
                        title = "${stats.fssaiVerificationRequests} FSSAI Certifications Submitted",
                        subtitle = "Document verification pending approval",
                        icon = Icons.Default.VerifiedUser,
                        color = Color(0xFF1565C0),
                        onClick = onNavigateToFssai
                    )
                }
                if (stats.pendingProductApprovals > 0) {
                    AdminAlertCard(
                        title = "${stats.pendingProductApprovals} Dishes Awaiting Moderation",
                        subtitle = "Inspect ingredients and hygiene compliance",
                        icon = Icons.Default.Fastfood,
                        color = Color(0xFF2E7D32),
                        onClick = onNavigateToProducts
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        // Platform KPI Metrics Grid
        Text(
            text = "Platform Analytics & Overview",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = Color(0xFF1E1E2E)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            AdminKpiCard(
                title = "Total Users",
                value = "${stats.totalCustomers + stats.totalSellers}",
                subtitle = "${stats.totalCustomers} Cust • ${stats.totalSellers} Sellers",
                icon = Icons.Default.People,
                color = CravexaPurple800,
                modifier = Modifier.weight(1f)
            )
            AdminKpiCard(
                title = "Total Revenue",
                value = "₹${stats.totalRevenue.toInt()}",
                subtitle = "Gross GMV",
                icon = Icons.Default.AccountBalanceWallet,
                color = Color(0xFF2E7D32),
                modifier = Modifier.weight(1f),
                onClick = onNavigateToPayments
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            AdminKpiCard(
                title = "Total Orders",
                value = "${stats.totalOrders}",
                subtitle = "Platform orders",
                icon = Icons.Default.ReceiptLong,
                color = CravexaOrange500,
                modifier = Modifier.weight(1f),
                onClick = onNavigateToOrders
            )
            AdminKpiCard(
                title = "Live Catalog",
                value = "${stats.totalProducts}",
                subtitle = "${stats.pendingProductApprovals} in review",
                icon = Icons.Default.Inventory2,
                color = Color(0xFFE65100),
                modifier = Modifier.weight(1f),
                onClick = onNavigateToProducts
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            AdminKpiCard(
                title = "Pending Refunds",
                value = "${stats.pendingRefunds}",
                subtitle = if (stats.pendingRefunds > 0) "Requires review" else "All settled",
                icon = Icons.Default.CurrencyExchange,
                color = if (stats.pendingRefunds > 0) Color(0xFFD32F2F) else Color.Gray,
                modifier = Modifier.weight(1f),
                onClick = onNavigateToPayments
            )
            AdminKpiCard(
                title = "Complaints",
                value = "${stats.pendingComplaints}",
                subtitle = if (stats.pendingComplaints > 0) "Open tickets" else "Zero disputes",
                icon = Icons.Default.SupportAgent,
                color = if (stats.pendingComplaints > 0) Color(0xFFC2185B) else Color.Gray,
                modifier = Modifier.weight(1f),
                onClick = onNavigateToComplaints
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun AdminAlertCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.08f)),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E1E2E)
                    )
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF6B7280),
                        fontSize = 12.sp
                    )
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun AdminKpiCard(
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    Card(
        modifier = modifier
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF6B7280),
                        fontWeight = FontWeight.Medium
                    )
                )
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(color.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = color,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1E1E2E)
                )
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color.Gray,
                    fontSize = 11.sp
                )
            )
        }
    }
}
