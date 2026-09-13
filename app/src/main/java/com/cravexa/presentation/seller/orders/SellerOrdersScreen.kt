package com.cravexa.presentation.seller.orders

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.cravexa.core.designsystem.components.CravexaButton
import com.cravexa.core.designsystem.components.CravexaButtonStyle
import com.cravexa.core.designsystem.components.CravexaChip
import com.cravexa.core.designsystem.components.CravexaEmptyState
import com.cravexa.core.designsystem.components.CravexaLoadingIndicator
import com.cravexa.core.designsystem.components.CravexaTopAppBar
import com.cravexa.core.designsystem.theme.CravexaOrange500
import com.cravexa.core.designsystem.theme.CravexaPurple800
import com.cravexa.domain.model.Order
import com.cravexa.domain.model.OrderStatus

@Composable
fun SellerOrdersScreen(
    onNavigateToOrderDetail: (String) -> Unit,
    viewModel: SellerOrdersViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val toastMessage by viewModel.toastMessage.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(toastMessage) {
        toastMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearToast()
        }
    }

    Scaffold(
        topBar = {
            CravexaTopAppBar(
                title = "Kitchen Orders & Dispatch"
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color(0xFFF9F9FB)
    ) { innerPadding ->
        when (val state = uiState) {
            is SellerOrdersUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CravexaLoadingIndicator(size = 44.dp)
                }
            }
            is SellerOrdersUiState.Error -> {
                CravexaEmptyState(
                    title = "Failed to load orders",
                    description = state.message,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }
            is SellerOrdersUiState.Success -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    // Filter Chips Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SellerOrderFilter.entries.forEach { filter ->
                            val count = when (filter) {
                                SellerOrderFilter.ALL -> state.orders.size
                                SellerOrderFilter.PENDING -> state.orders.count {
                                    it.orderStatus == OrderStatus.ORDER_PLACED || it.orderStatus == OrderStatus.PAYMENT_CONFIRMED
                                }
                                SellerOrderFilter.PREPARING -> state.orders.count { it.orderStatus == OrderStatus.PREPARING }
                                SellerOrderFilter.READY_FOR_PICKUP -> state.orders.count { it.orderStatus == OrderStatus.READY_FOR_PICKUP }
                                SellerOrderFilter.COMPLETED -> state.orders.count { it.orderStatus == OrderStatus.DELIVERED }
                                SellerOrderFilter.CANCELLED -> state.orders.count { it.orderStatus == OrderStatus.CANCELLED }
                            }
                            CravexaChip(
                                text = "${filter.title} ($count)",
                                selected = state.activeFilter == filter,
                                onClick = { viewModel.onFilterSelected(filter) }
                            )
                        }
                    }

                    // Orders List
                    if (state.filteredOrders.isEmpty()) {
                        CravexaEmptyState(
                            title = "No orders in this state",
                            description = "No customer orders currently match the \"${state.activeFilter.title}\" filter.",
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 6.dp, bottom = 80.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            items(
                                items = state.filteredOrders,
                                key = { it.id }
                            ) { order ->
                                SellerOrderCard(
                                    order = order,
                                    onClick = { onNavigateToOrderDetail(order.id) },
                                    onUpdateStatus = { newStatus ->
                                        viewModel.updateStatus(order.id, newStatus)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SellerOrderCard(
    order: Order,
    onClick: () -> Unit,
    onUpdateStatus: (OrderStatus) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Order Number & Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Order #${order.orderNumber}",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = order.orderDate,
                        style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                    )
                }

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

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color(0xFFF0F0F0))
            Spacer(modifier = Modifier.height(12.dp))

            // Order Items Breakdown
            order.items.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(CravexaPurple800.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${item.quantity}x",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = CravexaPurple800
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = item.productName,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                            maxLines = 1
                        )
                    }

                    Text(
                        text = "₹${(item.unitPrice * item.quantity).toInt()}",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color(0xFFF0F0F0))
            Spacer(modifier = Modifier.height(12.dp))

            // Total Amount & Fast Status Progression Action
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Total Payout",
                        style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray)
                    )
                    Text(
                        text = "₹${order.totalAmount.toInt()}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = CravexaPurple800
                        )
                    )
                }

                when (order.orderStatus) {
                    OrderStatus.ORDER_PLACED, OrderStatus.PAYMENT_CONFIRMED -> {
                        CravexaButton(
                            text = "Start Preparing",
                            onClick = { onUpdateStatus(OrderStatus.PREPARING) },
                            style = CravexaButtonStyle.PRIMARY,
                            modifier = Modifier.width(150.dp)
                        )
                    }
                    OrderStatus.PREPARING -> {
                        CravexaButton(
                            text = "Ready for Courier",
                            onClick = { onUpdateStatus(OrderStatus.READY_FOR_PICKUP) },
                            style = CravexaButtonStyle.SECONDARY,
                            modifier = Modifier.width(160.dp)
                        )
                    }
                    OrderStatus.READY_FOR_PICKUP -> {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFEDE7F6))
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "Awaiting Courier Pickup",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = CravexaPurple800
                                )
                            )
                        }
                    }
                    else -> {
                        Text(
                            text = "View Details →",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = CravexaOrange500
                            )
                        )
                    }
                }
            }
        }
    }
}
