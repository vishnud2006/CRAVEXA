package com.cravexa.presentation.seller.orders

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.cravexa.core.designsystem.components.CravexaCard
import com.cravexa.core.designsystem.components.CravexaEmptyState
import com.cravexa.core.designsystem.components.CravexaLoadingIndicator
import com.cravexa.core.designsystem.components.CravexaTopAppBar
import com.cravexa.core.designsystem.theme.CravexaOrange500
import com.cravexa.core.designsystem.theme.CravexaPurple800
import com.cravexa.domain.model.Order
import com.cravexa.domain.model.OrderStatus

@Composable
fun SellerOrderDetailScreen(
    orderId: String,
    onBack: () -> Unit,
    viewModel: SellerOrderDetailViewModel = hiltViewModel()
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
                title = "Order #${if (orderId.startsWith("CRV-")) orderId else "Details"}",
                showBackButton = true,
                onBackClick = onBack
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color(0xFFF9F9FB)
    ) { innerPadding ->
        when (val state = uiState) {
            is SellerOrderDetailUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CravexaLoadingIndicator(size = 44.dp)
                }
            }
            is SellerOrderDetailUiState.Error -> {
                CravexaEmptyState(
                    title = "Order Not Found",
                    description = state.message,
                    actionButtonText = "Back to Orders",
                    onActionClick = onBack,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }
            is SellerOrderDetailUiState.Success -> {
                SellerOrderDetailContent(
                    order = state.order,
                    onUpdateStatus = viewModel::updateStatus,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

@Composable
private fun SellerOrderDetailContent(
    order: Order,
    onUpdateStatus: (OrderStatus) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Status Card
        CravexaCard(modifier = Modifier.fillMaxWidth()) {
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
                        text = "Placed: ${order.orderDate}",
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

            Spacer(modifier = Modifier.height(16.dp))

            // Action Button for Pipeline
            when (order.orderStatus) {
                OrderStatus.ORDER_PLACED, OrderStatus.PAYMENT_CONFIRMED -> {
                    CravexaButton(
                        text = "Mark as Preparing in Kitchen",
                        onClick = { onUpdateStatus(OrderStatus.PREPARING) },
                        style = CravexaButtonStyle.PRIMARY
                    )
                }
                OrderStatus.PREPARING -> {
                    CravexaButton(
                        text = "Mark Packed & Ready for Courier",
                        onClick = { onUpdateStatus(OrderStatus.READY_FOR_PICKUP) },
                        style = CravexaButtonStyle.SECONDARY
                    )
                }
                OrderStatus.READY_FOR_PICKUP -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFEDE7F6))
                            .padding(14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Awaiting Courier Pickup (CRAVEXA Express)",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = CravexaPurple800
                            )
                        )
                    }
                }
                OrderStatus.DELIVERED -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFE8F5E9))
                            .padding(14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Delivered to Customer",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2E7D32)
                            )
                        )
                    }
                }
                else -> Unit
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Order Items Card
        Text(
            text = "Ordered Delicacies (${order.items.size})",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E1E2E)
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        CravexaCard(modifier = Modifier.fillMaxWidth()) {
            order.items.forEachIndexed { index, item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(CravexaOrange500.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${item.quantity}x",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = CravexaOrange500
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Text(
                                text = item.productName,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                            )
                            Text(
                                text = item.packageWeight,
                                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                            )
                        }
                    }

                    Text(
                        text = "₹${(item.unitPrice * item.quantity).toInt()}",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = CravexaPurple800
                        )
                    )
                }

                if (index < order.items.size - 1) {
                    HorizontalDivider(
                        color = Color(0xFFF5F5F5),
                        modifier = Modifier.padding(vertical = 6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Payment & Payout Summary
        Text(
            text = "Payment & Settlement",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E1E2E)
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        CravexaCard(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Payment Status", style = MaterialTheme.typography.bodyMedium)
                CravexaBadge(text = order.paymentStatus.name, type = CravexaBadgeType.SUCCESS)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Items Subtotal", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                Text(text = "₹${order.subtotal.toInt()}", style = MaterialTheme.typography.bodyMedium)
            }

            if (order.discount > 0) {
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Platform Discount", style = MaterialTheme.typography.bodyMedium, color = Color(0xFF2E7D32))
                    Text(text = "-₹${order.discount.toInt()}", style = MaterialTheme.typography.bodyMedium, color = Color(0xFF2E7D32))
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = Color(0xFFF0F0F0))
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total Order Value",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "₹${order.totalAmount.toInt()}",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = CravexaPurple800
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Delivery Dispatch Info
        Text(
            text = "Delivery Location (Fulfillment)",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E1E2E)
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        CravexaCard(modifier = Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.Top) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_location),
                    contentDescription = null,
                    tint = CravexaOrange500,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Customer: ${order.deliveryAddress?.fullName ?: "Verified Customer"}",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${order.deliveryAddress?.area ?: "Indiranagar"}, ${order.deliveryAddress?.city ?: "Bengaluru"} - ${order.deliveryAddress?.pincode ?: "560038"}",
                        style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}
