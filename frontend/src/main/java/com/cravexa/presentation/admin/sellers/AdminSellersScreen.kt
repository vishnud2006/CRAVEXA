package com.cravexa.presentation.admin.sellers

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
import com.cravexa.domain.model.AdminSeller
import com.cravexa.domain.model.FssaiStatus
import com.cravexa.domain.model.SellerAccountStatus

@Composable
fun AdminSellersScreen(
    viewModel: AdminSellersViewModel = hiltViewModel()
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
            is AdminSellersUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = CravexaPurple800)
                }
            }
            is AdminSellersUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = state.message, color = MaterialTheme.colorScheme.error)
                }
            }
            is AdminSellersUiState.Success -> {
                AdminSellersContent(
                    state = state,
                    onStatusFilterSelected = viewModel::onStatusFilterSelected,
                    onApproveSeller = viewModel::approveSeller,
                    onRejectSeller = viewModel::rejectSeller,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

@Composable
private fun AdminSellersContent(
    state: AdminSellersUiState.Success,
    onStatusFilterSelected: (SellerAccountStatus?) -> Unit,
    onApproveSeller: (String) -> Unit,
    onRejectSeller: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedSellerForReview by remember { mutableStateOf<AdminSeller?>(null) }

    if (selectedSellerForReview != null) {
        val seller = selectedSellerForReview!!
        AlertDialog(
            onDismissRequest = { selectedSellerForReview = null },
            title = {
                Text(text = "Creator Application Review", fontWeight = FontWeight.Bold)
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(text = seller.businessName, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = CravexaPurple800))
                    Text(text = "Creator: ${seller.sellerName}", style = MaterialTheme.typography.bodyMedium)
                    Text(text = "Email: ${seller.email} • Phone: ${seller.phone}", style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray))
                    Text(text = "Location: ${seller.location}", style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray))
                    Text(text = "Category: ${seller.categoryName}", style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "FSSAI Status: ${seller.fssaiStatus.displayTitle} (${seller.fssaiNumber ?: "None"})", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Kitchen Story / Bio:", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                    Text(text = seller.bio.ifBlank { "No bio provided." }, style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF374151)))
                }
            },
            confirmButton = {
                if (seller.status == SellerAccountStatus.PENDING) {
                    Row {
                        Button(
                            onClick = {
                                onApproveSeller(seller.id)
                                selectedSellerForReview = null
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                        ) {
                            Text("Approve", color = Color.White)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                onRejectSeller(seller.id)
                                selectedSellerForReview = null
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                        ) {
                            Text("Reject", color = Color.White)
                        }
                    }
                } else {
                    TextButton(onClick = { selectedSellerForReview = null }) {
                        Text("Close")
                    }
                }
            },
            dismissButton = {
                if (seller.status == SellerAccountStatus.PENDING) {
                    TextButton(onClick = { selectedSellerForReview = null }) {
                        Text("Cancel")
                    }
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
                    selected = state.selectedStatus == null,
                    onClick = { onStatusFilterSelected(null) },
                    label = { Text("All Sellers (${state.sellers.size})") }
                )
            }
            item {
                FilterChip(
                    selected = state.selectedStatus == SellerAccountStatus.PENDING,
                    onClick = { onStatusFilterSelected(SellerAccountStatus.PENDING) },
                    label = { Text("Pending Review (${state.sellers.count { it.status == SellerAccountStatus.PENDING }})") }
                )
            }
            item {
                FilterChip(
                    selected = state.selectedStatus == SellerAccountStatus.APPROVED,
                    onClick = { onStatusFilterSelected(SellerAccountStatus.APPROVED) },
                    label = { Text("Approved") }
                )
            }
            item {
                FilterChip(
                    selected = state.selectedStatus == SellerAccountStatus.REJECTED,
                    onClick = { onStatusFilterSelected(SellerAccountStatus.REJECTED) },
                    label = { Text("Rejected") }
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (state.filteredSellers.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No creators found in this view.",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(state.filteredSellers, key = { it.id }) { seller ->
                    AdminSellerCard(
                        seller = seller,
                        onClick = { selectedSellerForReview = seller },
                        onApprove = { onApproveSeller(seller.id) },
                        onReject = { onRejectSeller(seller.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun AdminSellerCard(
    seller: AdminSeller,
    onClick: () -> Unit,
    onApprove: () -> Unit,
    onReject: () -> Unit
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
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(CravexaOrange500.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Storefront,
                        contentDescription = null,
                        tint = CravexaOrange500,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = seller.businessName,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF1E1E2E)
                    )
                    Text(
                        text = "by ${seller.sellerName} • ${seller.categoryName}",
                        style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF6B7280), fontSize = 11.sp)
                    )
                    Text(
                        text = seller.location,
                        style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray, fontSize = 11.sp)
                    )
                }

                // Account Status Badge
                val (statusColor, statusBg) = when (seller.status) {
                    SellerAccountStatus.PENDING -> Pair(Color(0xFFE65100), Color(0xFFFFF8E1))
                    SellerAccountStatus.APPROVED -> Pair(Color(0xFF2E7D32), Color(0xFFE8F5E9))
                    SellerAccountStatus.REJECTED -> Pair(Color(0xFFC62828), Color(0xFFFFEBEE))
                    SellerAccountStatus.SUSPENDED -> Pair(Color(0xFFC62828), Color(0xFFFFEBEE))
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = statusBg
                ) {
                    Text(
                        text = seller.status.displayTitle,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = statusColor),
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
                    text = "FSSAI: ${seller.fssaiStatus.displayTitle}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = if (seller.fssaiStatus == FssaiStatus.VERIFIED) Color(0xFF2E7D32) else Color(0xFFE65100),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 11.sp
                    )
                )

                if (seller.status == SellerAccountStatus.PENDING) {
                    Row {
                        TextButton(onClick = onReject) {
                            Text("Reject", color = Color(0xFFD32F2F), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Button(
                            onClick = onApprove,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Text("Approve", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                } else {
                    Text(
                        text = "Tap to review details →",
                        style = MaterialTheme.typography.bodySmall.copy(color = CravexaPurple800, fontSize = 11.sp)
                    )
                }
            }
        }
    }
}
