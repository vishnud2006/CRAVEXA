package com.cravexa.presentation.admin.products

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
import com.cravexa.domain.model.AdminProduct
import com.cravexa.domain.model.AdminProductStatus

@Composable
fun AdminProductsScreen(
    viewModel: AdminProductsViewModel = hiltViewModel()
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
            is AdminProductsUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = CravexaPurple800)
                }
            }
            is AdminProductsUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = state.message, color = MaterialTheme.colorScheme.error)
                }
            }
            is AdminProductsUiState.Success -> {
                AdminProductsContent(
                    state = state,
                    onStatusFilterSelected = viewModel::onStatusFilterSelected,
                    onApprove = viewModel::approveProduct,
                    onReject = viewModel::rejectProduct,
                    onToggleDisable = viewModel::toggleProductDisable,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

@Composable
private fun AdminProductsContent(
    state: AdminProductsUiState.Success,
    onStatusFilterSelected: (AdminProductStatus?) -> Unit,
    onApprove: (String) -> Unit,
    onReject: (String) -> Unit,
    onToggleDisable: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var productToInspect by remember { mutableStateOf<AdminProduct?>(null) }

    if (productToInspect != null) {
        val prod = productToInspect!!
        AlertDialog(
            onDismissRequest = { productToInspect = null },
            title = {
                Text(text = "Delicacy Moderation", fontWeight = FontWeight.Bold)
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(text = prod.name, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = CravexaPurple800))
                    Text(text = "Kitchen: ${prod.sellerName}", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                    Text(text = "Category: ${prod.categoryName} • Unit: ${prod.weight} • Price: ₹${prod.price.toInt()}", style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray))
                    Text(text = "Shelf Life: ${prod.shelfLife}", style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Ingredients List:", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                    Text(text = prod.ingredients.joinToString(", ").ifBlank { "None declared" }, style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF374151)))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Description:", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                    Text(text = prod.description, style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF4B5563)))
                }
            },
            confirmButton = {
                if (prod.status == AdminProductStatus.PENDING) {
                    Row {
                        Button(
                            onClick = {
                                onApprove(prod.id)
                                productToInspect = null
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                        ) {
                            Text("Approve Dish", color = Color.White)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                onReject(prod.id)
                                productToInspect = null
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                        ) {
                            Text("Reject", color = Color.White)
                        }
                    }
                } else {
                    TextButton(onClick = { productToInspect = null }) {
                        Text("Close")
                    }
                }
            },
            dismissButton = {
                if (prod.status == AdminProductStatus.PENDING) {
                    TextButton(onClick = { productToInspect = null }) {
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
                    selected = state.selectedStatusFilter == null,
                    onClick = { onStatusFilterSelected(null) },
                    label = { Text("All Dishes (${state.products.size})") }
                )
            }
            item {
                FilterChip(
                    selected = state.selectedStatusFilter == AdminProductStatus.PENDING,
                    onClick = { onStatusFilterSelected(AdminProductStatus.PENDING) },
                    label = { Text("Pending Review (${state.products.count { it.status == AdminProductStatus.PENDING }})") }
                )
            }
            item {
                FilterChip(
                    selected = state.selectedStatusFilter == AdminProductStatus.APPROVED,
                    onClick = { onStatusFilterSelected(AdminProductStatus.APPROVED) },
                    label = { Text("Approved & Live") }
                )
            }
            item {
                FilterChip(
                    selected = state.selectedStatusFilter == AdminProductStatus.DISABLED,
                    onClick = { onStatusFilterSelected(AdminProductStatus.DISABLED) },
                    label = { Text("Disabled") }
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (state.filteredProducts.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No delicacies found for this filter.",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(state.filteredProducts, key = { it.id }) { product ->
                    AdminProductCard(
                        product = product,
                        onClick = { productToInspect = product },
                        onApprove = { onApprove(product.id) },
                        onReject = { onReject(product.id) },
                        onToggleDisable = { onToggleDisable(product.id, product.status == AdminProductStatus.DISABLED) }
                    )
                }
            }
        }
    }
}

@Composable
private fun AdminProductCard(
    product: AdminProduct,
    onClick: () -> Unit,
    onApprove: () -> Unit,
    onReject: () -> Unit,
    onToggleDisable: () -> Unit
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
                        .background(Color(0xFFEDE7F6)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Fastfood,
                        contentDescription = null,
                        tint = CravexaPurple800,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF1E1E2E)
                    )
                    Text(
                        text = "${product.sellerName} • ₹${product.price.toInt()} • ${product.weight}",
                        style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF6B7280), fontSize = 11.sp)
                    )
                }

                // Status Badge
                val (color, bg) = when (product.status) {
                    AdminProductStatus.APPROVED -> Pair(Color(0xFF2E7D32), Color(0xFFE8F5E9))
                    AdminProductStatus.PENDING -> Pair(Color(0xFFE65100), Color(0xFFFFF8E1))
                    AdminProductStatus.REJECTED, AdminProductStatus.DISABLED -> Pair(Color(0xFFC62828), Color(0xFFFFEBEE))
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = bg
                ) {
                    Text(
                        text = product.status.displayTitle,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = color),
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
                    text = "Shelf Life: ${product.shelfLife}",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray, fontSize = 11.sp)
                )

                if (product.status == AdminProductStatus.PENDING) {
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
                            Text("Approve", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                } else {
                    TextButton(onClick = onToggleDisable) {
                        Text(
                            text = if (product.status == AdminProductStatus.DISABLED) "Enable Dish" else "Disable Dish",
                            color = if (product.status == AdminProductStatus.DISABLED) Color(0xFF2E7D32) else Color(0xFFD32F2F),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}
