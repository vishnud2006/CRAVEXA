package com.cravexa.presentation.admin.marketplace

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cravexa.core.designsystem.theme.CravexaOrange500
import com.cravexa.core.designsystem.theme.CravexaPurple800
import com.cravexa.domain.model.AdminBanner
import com.cravexa.domain.model.AdminCategory
import com.cravexa.domain.model.AdminCoupon

@Composable
fun AdminMarketplaceScreen(
    viewModel: AdminMarketplaceViewModel = hiltViewModel()
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
            is AdminMarketplaceUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = CravexaPurple800)
                }
            }
            is AdminMarketplaceUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                    Text(text = state.message, color = MaterialTheme.colorScheme.error)
                }
            }
            is AdminMarketplaceUiState.Success -> {
                AdminMarketplaceContent(
                    state = state,
                    onTabSelected = viewModel::onSubTabSelected,
                    onAddCategory = viewModel::addCategory,
                    onToggleCategory = viewModel::toggleCategory,
                    onAddCoupon = viewModel::addCoupon,
                    onToggleCoupon = viewModel::toggleCoupon,
                    onToggleBanner = viewModel::toggleBanner,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

@Composable
private fun AdminMarketplaceContent(
    state: AdminMarketplaceUiState.Success,
    onTabSelected: (Int) -> Unit,
    onAddCategory: (String, String) -> Unit,
    onToggleCategory: (String, Boolean) -> Unit,
    onAddCoupon: (String, Int, Double, Double) -> Unit,
    onToggleCoupon: (String, Boolean) -> Unit,
    onToggleBanner: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddCategoryDialog by remember { mutableStateOf(false) }
    var showAddCouponDialog by remember { mutableStateOf(false) }

    if (showAddCategoryDialog) {
        var catName by remember { mutableStateOf("") }
        var catDesc by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showAddCategoryDialog = false },
            title = { Text("Add New Category", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    OutlinedTextField(
                        value = catName,
                        onValueChange = { catName = it },
                        label = { Text("Category Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = catDesc,
                        onValueChange = { catDesc = it },
                        label = { Text("Description") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (catName.isNotBlank()) {
                            onAddCategory(catName.trim(), catDesc.trim())
                            showAddCategoryDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CravexaPurple800)
                ) {
                    Text("Add", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddCategoryDialog = false }) { Text("Cancel") }
            }
        )
    }

    if (showAddCouponDialog) {
        var code by remember { mutableStateOf("") }
        var discountPct by remember { mutableStateOf("15") }
        var maxDisc by remember { mutableStateOf("100") }
        var minOrd by remember { mutableStateOf("299") }
        AlertDialog(
            onDismissRequest = { showAddCouponDialog = false },
            title = { Text("Create Promo Coupon", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    OutlinedTextField(
                        value = code,
                        onValueChange = { code = it },
                        label = { Text("Promo Code (e.g. FESTIVE20)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = discountPct,
                        onValueChange = { discountPct = it },
                        label = { Text("Discount % (e.g. 20)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = minOrd,
                        onValueChange = { minOrd = it },
                        label = { Text("Min Order Amount (₹)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (code.isNotBlank()) {
                            onAddCoupon(
                                code.trim(),
                                discountPct.toIntOrNull() ?: 10,
                                maxDisc.toDoubleOrNull() ?: 100.0,
                                minOrd.toDoubleOrNull() ?: 299.0
                            )
                            showAddCouponDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CravexaPurple800)
                ) {
                    Text("Create Coupon", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddCouponDialog = false }) { Text("Cancel") }
            }
        )
    }

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
                text = { Text("Categories (${state.categories.size})", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
            )
            Tab(
                selected = state.selectedSubTab == 1,
                onClick = { onTabSelected(1) },
                text = { Text("Coupons (${state.coupons.size})", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
            )
            Tab(
                selected = state.selectedSubTab == 2,
                onClick = { onTabSelected(2) },
                text = { Text("Banners (${state.banners.size})", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        when (state.selectedSubTab) {
            0 -> {
                // Categories Tab
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Marketplace Categories", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                    Button(
                        onClick = { showAddCategoryDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = CravexaPurple800),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add Category", fontSize = 12.sp)
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(state.categories, key = { it.id }) { cat ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(cat.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                                    Text("${cat.productCount} Dishes • ${cat.description}", style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray, fontSize = 11.sp))
                                }
                                Switch(
                                    checked = cat.enabled,
                                    onCheckedChange = { onToggleCategory(cat.id, it) }
                                )
                            }
                        }
                    }
                }
            }
            1 -> {
                // Coupons Tab
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Active Promo Coupons", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                    Button(
                        onClick = { showAddCouponDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = CravexaPurple800),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("New Coupon", fontSize = 12.sp)
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(state.coupons, key = { it.id }) { coupon ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(coupon.code, fontWeight = FontWeight.ExtraBold, color = CravexaOrange500)
                                    Text("${coupon.discountPercentage}% OFF (Min ₹${coupon.minOrderAmount.toInt()})", style = MaterialTheme.typography.bodySmall)
                                    Text("Used: ${coupon.usedCount}/${coupon.usageLimit} • Exp: ${coupon.endDate}", style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray, fontSize = 11.sp))
                                }
                                Switch(
                                    checked = coupon.active,
                                    onCheckedChange = { onToggleCoupon(coupon.id, it) }
                                )
                            }
                        }
                    }
                }
            }
            2 -> {
                // Banners Tab
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(state.banners, key = { it.id }) { banner ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(banner.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                                    Text(banner.subtitle, style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray, fontSize = 11.sp))
                                    Text("Route: ${banner.actionRoute}", style = MaterialTheme.typography.bodySmall.copy(color = CravexaPurple800, fontSize = 10.sp))
                                }
                                Switch(
                                    checked = banner.active,
                                    onCheckedChange = { onToggleBanner(banner.id, it) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
