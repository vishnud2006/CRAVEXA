package com.cravexa.presentation.admin.orders

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cravexa.core.designsystem.theme.CravexaOrange500
import com.cravexa.core.designsystem.theme.CravexaPurple800
import com.cravexa.domain.model.AdminOrder
import com.cravexa.domain.model.OrderStatus

@Composable
fun AdminOrdersScreen(
    viewModel: AdminOrdersViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(containerColor = Color(0xFFF9FAFB)) { innerPadding ->
        when (val state = uiState) {
            is AdminOrdersUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = CravexaPurple800)
                }
            }
            is AdminOrdersUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = state.message, color = MaterialTheme.colorScheme.error)
                }
            }
            is AdminOrdersUiState.Success -> {
                AdminOrdersContent(
                    state = state,
                    onStatusFilterSelected = viewModel::onStatusFilterSelected,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

@Composable
private fun AdminOrdersContent(
    state: AdminOrdersUiState.Success,
    onStatusFilterSelected: (OrderStatus?) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedOrderForDetail by remember { mutableStateOf<AdminOrder?>(null) }

    if (selectedOrderForDetail != null) {
        val order = selectedOrderForDetail!!
        AlertDialog(
            onDismissRequest = { selectedOrderForDetail = null },
            title = {
                Text(text = "Order Details • ${order.orderNumber}", fontWeight = FontWeight.Bold)
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Customer: ${order.customerName} (${order.customerPhone})", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                    Text(text = "Kitchen: ${order.sellerName}", style = MaterialTheme.typography.bodyMedium)
                    Text(text = "Date: ${order.date}", style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray))
                    Text(text = "Total Paid: ₹${order.totalAmount.toInt()} (${order.paymentStatus.displayTitle})", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = CravexaPurple800))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Status: ${order.orderStatus.displayTitle}", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = CravexaOrange500))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Delivery Address:", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                    Text(text = order.deliveryAddressSummary, style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF4B5563)))
                }
            },
            confirmButton = {
                TextButton(onClick = { selectedOrderForDetail = null }) {
                    Text("Close")
                }
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Status Filter Chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                FilterChip(
                    selected = state.selectedStatusFilter == null,
                    onClick = { onStatusFilterSelected(null) },
                    label = { Text("All Orders (${state.orders.size})") }
                )
            }
            item {
                FilterChip(
                    selected = state.selectedStatusFilter == OrderStatus.PAYMENT_CONFIRMED,
                    onClick = { onStatusFilterSelected(OrderStatus.PAYMENT_CONFIRMED) },
                    label = { Text("Confirmed / New") }
                )
            }
            item {
                FilterChip(
                    selected = state.selectedStatusFilter == OrderStatus.PREPARING,
                    onClick = { onStatusFilterSelected(OrderStatus.PREPARING) },
                    label = { Text("Preparing") }
                )
            }
            item {
                FilterChip(
                    selected = state.selectedStatusFilter == OrderStatus.READY_FOR_PICKUP,
                    onClick = { onStatusFilterSelected(OrderStatus.READY_FOR_PICKUP) },
                    label = { Text("Ready for Pickup") }
                )
            }
            item {
                FilterChip(
                    selected = state.selectedStatusFilter == OrderStatus.DELIVERED,
                    onClick = { onStatusFilterSelected(OrderStatus.DELIVERED) },
                    label = { Text("Delivered") }
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (state.filteredOrders.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No orders found in this view.",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(state.filteredOrders, key = { it.id }) { order ->
                    AdminOrderCard(
                        order = order,
                        onClick = { selectedOrderForDetail = order }
                    )
                }
            }
        }
    }
}

@Composable
private fun AdminOrderCard(
    order: AdminOrder,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEDE7F6)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Receipt,
                        contentDescription = null,
                        tint = CravexaPurple800,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = order.orderNumber,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF1E1E2E)
                    )
                    Text(
                        text = "${order.customerName} → ${order.sellerName}",
                        style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF6B7280), fontSize = 11.sp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFFFFF8E1)
                ) {
                    Text(
                        text = order.orderStatus.displayTitle,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFFE65100)),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = Color(0xFFF3F4F6))
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${order.itemCount} items • ${order.date}",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray, fontSize = 11.sp)
                )
                Text(
                    text = "₹${order.totalAmount.toInt()}",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.ExtraBold, color = CravexaPurple800)
                )
            }
        }
    }
}
