package com.cravexa.presentation.admin.payments

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.cravexa.core.designsystem.theme.CravexaPurple800
import com.cravexa.domain.model.AdminPayment
import com.cravexa.domain.model.AdminRefund
import com.cravexa.domain.model.AdminRefundStatus

@Composable
fun AdminPaymentsScreen(
    viewModel: AdminPaymentsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val toastMessage by viewModel.toastMessage.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearToast()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color(0xFFF9FAFB)
    ) { innerPadding ->
        when (val state = uiState) {
            is AdminPaymentsUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = CravexaPurple800)
                }
            }
            is AdminPaymentsUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = state.message, color = MaterialTheme.colorScheme.error)
                }
            }
            is AdminPaymentsUiState.Success -> {
                AdminPaymentsContent(
                    state = state,
                    onTabSelected = viewModel::onSubTabSelected,
                    onApproveRefund = viewModel::approveRefund,
                    onRejectRefund = viewModel::rejectRefund,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

@Composable
private fun AdminPaymentsContent(
    state: AdminPaymentsUiState.Success,
    onTabSelected: (Int) -> Unit,
    onApproveRefund: (String) -> Unit,
    onRejectRefund: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TabRow(
            selectedTabIndex = state.selectedSubTab,
            containerColor = Color.White,
            contentColor = CravexaPurple800
        ) {
            Tab(
                selected = state.selectedSubTab == 0,
                onClick = { onTabSelected(0) },
                text = { Text("Payments Log (${state.payments.size})", fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = state.selectedSubTab == 1,
                onClick = { onTabSelected(1) },
                text = { Text("Refunds Queue (${state.refunds.size})", fontWeight = FontWeight.Bold) }
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (state.selectedSubTab == 0) {
            // Payments List
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(state.payments, key = { it.id }) { payment ->
                    AdminPaymentCard(payment = payment)
                }
            }
        } else {
            // Refunds List
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(state.refunds, key = { it.id }) { refund ->
                    AdminRefundCard(
                        refund = refund,
                        onApprove = { onApproveRefund(refund.id) },
                        onReject = { onRejectRefund(refund.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun AdminPaymentCard(payment: AdminPayment) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
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
                        .background(Color(0xFFE8F5E9)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF2E7D32),
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "₹${payment.amount.toInt()} • ${payment.orderNumber}",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF1E1E2E)
                    )
                    Text(
                        text = "${payment.customerName} • ${payment.paymentMethod}",
                        style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF6B7280), fontSize = 11.sp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFFE8F5E9)
                ) {
                    Text(
                        text = payment.status.displayTitle,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32)),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = Color(0xFFF3F4F6))
            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Ref: ${payment.referenceId} • ${payment.date}",
                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray, fontSize = 10.sp)
            )
        }
    }
}

@Composable
private fun AdminRefundCard(
    refund: AdminRefund,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
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
                        .background(Color(0xFFFFF8E1)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CurrencyExchange,
                        contentDescription = null,
                        tint = Color(0xFFE65100),
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "₹${refund.amount.toInt()} • ${refund.orderNumber}",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF1E1E2E)
                    )
                    Text(
                        text = "${refund.customerName} → ${refund.sellerName}",
                        style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF6B7280), fontSize = 11.sp)
                    )
                }

                // Status Badge
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (refund.status == AdminRefundStatus.APPROVED || refund.status == AdminRefundStatus.COMPLETED) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
                ) {
                    Text(
                        text = refund.status.displayTitle,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (refund.status == AdminRefundStatus.APPROVED || refund.status == AdminRefundStatus.COMPLETED) Color(0xFF2E7D32) else Color(0xFFC62828)
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Reason: ${refund.reason}",
                style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF4B5563))
            )

            if (refund.status == AdminRefundStatus.REQUESTED || refund.status == AdminRefundStatus.UNDER_REVIEW) {
                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = Color(0xFFF3F4F6))
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onReject) {
                        Text("Reject", color = Color(0xFFD32F2F), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = onApprove,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text("Approve Refund", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
