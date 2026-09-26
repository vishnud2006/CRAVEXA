package com.cravexa.presentation.customer.orders

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cravexa.core.designsystem.components.CravexaBadge
import com.cravexa.core.designsystem.components.CravexaBadgeType
import com.cravexa.core.designsystem.components.CravexaButton
import com.cravexa.core.designsystem.components.CravexaButtonStyle
import com.cravexa.core.designsystem.components.CravexaCard
import com.cravexa.core.designsystem.components.CravexaEmptyState
import com.cravexa.core.designsystem.components.CravexaErrorState
import com.cravexa.core.designsystem.components.CravexaLoadingIndicator
import com.cravexa.core.designsystem.components.CravexaTopAppBar
import com.cravexa.core.designsystem.theme.CravexaPurple800
import com.cravexa.domain.model.Order
import com.cravexa.domain.model.OrderStatus

@Composable
fun OrdersScreen(
    onViewOrder: (String) -> Unit,
    onTrackOrder: (String) -> Unit,
    onExploreFood: () -> Unit = {},
    showBackButton: Boolean = false,
    onBack: () -> Unit = {},
    viewModel: OrdersViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            CravexaTopAppBar(
                title = "My Orders",
                showBackButton = showBackButton,
                onBackClick = onBack
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        when (val state = uiState) {
            is OrdersUiState.Loading -> {
                CravexaLoadingIndicator(modifier = Modifier.padding(innerPadding))
            }
            is OrdersUiState.Error -> {
                CravexaErrorState(
                    message = state.message,
                    onRetry = viewModel::loadOrders,
                    modifier = Modifier.padding(innerPadding)
                )
            }
            is OrdersUiState.Success -> {
                if (state.orders.isEmpty()) {
                    CravexaEmptyState(
                        title = "No Orders Yet",
                        description = "When you order authentic homemade pickles, spices, or sweets, they will appear here.",
                        actionButtonText = "Explore Homemade Foods",
                        onActionClick = onExploreFood,
                        modifier = Modifier.padding(innerPadding)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        items(state.orders, key = { it.id }) { order ->
                            OrderItemCard(
                                order = order,
                                onViewOrder = { onViewOrder(order.id) },
                                onTrackOrder = { onTrackOrder(order.id) }
                            )
                        }
                        item {
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun OrderItemCard(
    order: Order,
    onViewOrder: () -> Unit,
    onTrackOrder: () -> Unit
) {
    CravexaCard(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = 16.dp
    ) {
        // Order Header (ID, Date & Status)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Order #${order.orderNumber}",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = order.orderDate,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            val badgeType = when (order.orderStatus) {
                OrderStatus.DELIVERED -> CravexaBadgeType.SUCCESS
                OrderStatus.CANCELLED, OrderStatus.REFUNDED -> CravexaBadgeType.WARNING
                else -> CravexaBadgeType.FOOD_TAG
            }

            CravexaBadge(
                text = order.orderStatus.displayTitle.uppercase(),
                type = badgeType
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(10.dp))

        // Kitchen Name
        Text(
            text = "From: ${order.sellerName}",
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
            color = CravexaPurple800
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Items Summary
        Text(
            text = order.itemsSummary,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Price & Payment
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Total: ₹${String.format("%.2f", order.totalAmount)}",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Black,
                    color = CravexaPurple800
                )
            )

            Text(
                text = "• ${order.paymentStatus.displayTitle}",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Actions
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CravexaButton(
                text = "View Details",
                onClick = onViewOrder,
                style = CravexaButtonStyle.OUTLINED,
                height = 40.dp,
                modifier = Modifier.weight(1f)
            )

            if (!order.orderStatus.isTerminal) {
                CravexaButton(
                    text = "Track Order",
                    onClick = onTrackOrder,
                    style = CravexaButtonStyle.PRIMARY,
                    height = 40.dp,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

