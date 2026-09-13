package com.cravexa.presentation.seller.products

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.cravexa.core.designsystem.components.CravexaChip
import com.cravexa.core.designsystem.components.CravexaEmptyState
import com.cravexa.core.designsystem.components.CravexaLoadingIndicator
import com.cravexa.core.designsystem.components.CravexaTextField
import com.cravexa.core.designsystem.components.CravexaTopAppBar
import com.cravexa.core.designsystem.components.StockUpdateDialog
import com.cravexa.core.designsystem.theme.CravexaOrange500
import com.cravexa.core.designsystem.theme.CravexaPurple800
import com.cravexa.domain.model.Product

@Composable
fun SellerProductsScreen(
    onNavigateToAddProduct: () -> Unit,
    onNavigateToEditProduct: (String) -> Unit,
    viewModel: SellerProductsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val toastMessage by viewModel.toastMessage.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    var productToUpdateStock by remember { mutableStateOf<Product?>(null) }
    var productToDelete by remember { mutableStateOf<Product?>(null) }

    LaunchedEffect(toastMessage) {
        toastMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearToast()
        }
    }

    if (productToUpdateStock != null) {
        StockUpdateDialog(
            product = productToUpdateStock!!,
            onDismiss = { productToUpdateStock = null },
            onSaveStock = { newStock ->
                viewModel.updateStock(productToUpdateStock!!.id, newStock)
                productToUpdateStock = null
            }
        )
    }

    if (productToDelete != null) {
        AlertDialog(
            onDismissRequest = { productToDelete = null },
            title = {
                Text(text = "Delete Dish?", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            },
            text = {
                Text(
                    text = "Are you sure you want to remove \"${productToDelete?.name}\" from your kitchen catalog?",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    productToDelete?.let { viewModel.deleteProduct(it.id) }
                    productToDelete = null
                }) {
                    Text(text = "Delete", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { productToDelete = null }) {
                    Text(text = "Cancel", color = CravexaPurple800)
                }
            }
        )
    }

    Scaffold(
        topBar = {
            CravexaTopAppBar(
                title = "Kitchen Catalog & Stock"
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAddProduct,
                containerColor = CravexaOrange500,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(painter = painterResource(id = R.drawable.ic_add), contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Add Dish", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color(0xFFF9F9FB)
    ) { innerPadding ->
        when (val state = uiState) {
            is SellerProductsUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CravexaLoadingIndicator(size = 44.dp)
                }
            }
            is SellerProductsUiState.Error -> {
                CravexaEmptyState(
                    title = "Failed to load dishes",
                    description = state.message,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }
            is SellerProductsUiState.Success -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    // Search Bar
                    Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
                        CravexaTextField(
                            value = state.searchQuery,
                            onValueChange = viewModel::onSearchQueryChanged,
                            label = "Search Catalog",
                            placeholder = "Search dishes by name or category..."
                        )
                    }

                    // Filter Chips Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ProductFilter.entries.forEach { filter ->
                            val count = when (filter) {
                                ProductFilter.ALL -> state.products.size
                                ProductFilter.ACTIVE -> state.products.count { it.available && it.stock > 0 }
                                ProductFilter.LOW_STOCK -> state.products.count { it.stock in 1..5 }
                                ProductFilter.OUT_OF_STOCK -> state.products.count { it.stock == 0 }
                                ProductFilter.DISABLED -> state.products.count { !it.available }
                            }
                            CravexaChip(
                                text = "${filter.title} ($count)",
                                selected = state.activeFilter == filter,
                                onClick = { viewModel.onFilterSelected(filter) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Products List
                    if (state.filteredProducts.isEmpty()) {
                        CravexaEmptyState(
                            title = "No dishes found",
                            description = if (state.searchQuery.isNotBlank()) "No dishes match \"${state.searchQuery}\"" else "No dishes under ${state.activeFilter.title}. Tap 'Add Dish' to list a new homemade delicacy.",
                            actionButtonText = "Add First Dish",
                            onActionClick = onNavigateToAddProduct,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 88.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(
                                items = state.filteredProducts,
                                key = { it.id }
                            ) { product ->
                                SellerProductCard(
                                    product = product,
                                    onEdit = { onNavigateToEditProduct(product.id) },
                                    onUpdateStock = { productToUpdateStock = product },
                                    onToggleAvailability = { isAvailable ->
                                        viewModel.toggleAvailability(product.id, isAvailable)
                                    },
                                    onDelete = { productToDelete = product }
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
private fun SellerProductCard(
    product: Product,
    onEdit: () -> Unit,
    onUpdateStock: () -> Unit,
    onToggleAvailability: (Boolean) -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
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
                verticalAlignment = Alignment.Top
            ) {
                // Dish Icon Box
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(CravexaPurple800.copy(alpha = 0.08f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_onboarding_homemade),
                        contentDescription = null,
                        tint = CravexaOrange500,
                        modifier = Modifier.size(30.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        maxLines = 2
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "${product.categoryName} • ${product.weight}",
                        style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF757575))
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // Stock Status Badge
                        val (badgeType, badgeLabel) = when {
                            !product.available -> CravexaBadgeType.PRIMARY to "DISABLED"
                            product.stock == 0 -> CravexaBadgeType.WARNING to "OUT OF STOCK"
                            product.stock in 1..5 -> CravexaBadgeType.FOOD_TAG to "LOW STOCK (${product.stock})"
                            else -> CravexaBadgeType.SUCCESS to "IN STOCK (${product.stock})"
                        }

                        CravexaBadge(
                            text = badgeLabel,
                            type = badgeType
                        )

                        Text(
                            text = "₹${product.price.toInt()}",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = CravexaPurple800
                            )
                        )

                        if (product.originalPrice != null && product.originalPrice > product.price) {
                            Text(
                                text = "₹${product.originalPrice.toInt()}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color.Gray,
                                    textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Availability Toggle
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onToggleAvailability(!product.available) }
                ) {
                    Switch(
                        checked = product.available,
                        onCheckedChange = onToggleAvailability,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = CravexaOrange500,
                            uncheckedThumbColor = Color.White,
                            uncheckedTrackColor = Color.LightGray
                        ),
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (product.available) "Active" else "Disabled",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = if (product.available) Color(0xFF2E7D32) else Color.Gray
                        )
                    )
                }

                // Buttons: Quick Stock Edit, Edit Details, Delete
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = onUpdateStock,
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Stock (${product.stock})",
                            color = CravexaPurple800,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }

                    IconButton(
                        onClick = onEdit,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_edit),
                            contentDescription = "Edit Dish",
                            tint = CravexaPurple800,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_delete),
                            contentDescription = "Delete Dish",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

