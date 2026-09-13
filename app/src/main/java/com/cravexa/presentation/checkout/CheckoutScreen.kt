package com.cravexa.presentation.checkout

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.cravexa.R
import com.cravexa.core.designsystem.components.CravexaBadge
import com.cravexa.core.designsystem.components.CravexaBadgeType
import com.cravexa.core.designsystem.components.CravexaButton
import com.cravexa.core.designsystem.components.CravexaButtonStyle
import com.cravexa.core.designsystem.components.CravexaCard
import com.cravexa.core.designsystem.components.CravexaTopAppBar
import com.cravexa.core.designsystem.theme.CravexaOrange50
import com.cravexa.core.designsystem.theme.CravexaOrange500
import com.cravexa.core.designsystem.theme.CravexaPurple100
import com.cravexa.core.designsystem.theme.CravexaPurple800
import com.cravexa.core.designsystem.theme.CravexaPurple900
import com.cravexa.core.designsystem.theme.CravexaShapes
import com.cravexa.core.designsystem.theme.CravexaSuccess
import com.cravexa.domain.model.Address
import com.cravexa.domain.model.AddressType
import com.cravexa.domain.model.CartItem
import com.cravexa.domain.model.PaymentMethod
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    onOrderPlaced: (String) -> Unit,
    onNavigateToAddAddress: () -> Unit,
    onRequireLogin: () -> Unit,
    onBack: () -> Unit,
    viewModel: CheckoutViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var showAddressSheet by remember { mutableStateOf(false) }
    var couponInput by remember { mutableStateOf("") }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is CheckoutEvent.OrderPlacedSuccess -> {
                    onOrderPlaced(event.orderId)
                }
                is CheckoutEvent.ShowMessage -> {
                    snackbarHostState.showSnackbar(event.message)
                }
                is CheckoutEvent.RequireLogin -> {
                    onRequireLogin()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            CravexaTopAppBar(
                title = "Checkout & Review",
                showBackButton = true,
                onBackClick = onBack
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "To Pay",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = uiState.pricing.formattedTotal,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Black,
                                color = CravexaPurple900
                            )
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    CravexaButton(
                        text = if (uiState.isPlacingOrder) "Placing Order..." else "Place Order • ${uiState.pricing.formattedTotal}",
                        onClick = { viewModel.placeOrder() },
                        enabled = !uiState.isPlacingOrder && uiState.items.isNotEmpty() && uiState.selectedAddress != null,
                        style = CravexaButtonStyle.PRIMARY,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Delivery Address Section
            item {
                Text(
                    text = "Delivery Address",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(8.dp))

                val address = uiState.selectedAddress
                if (address != null) {
                    CravexaCard(
                        modifier = Modifier.fillMaxWidth(),
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentPadding = 16.dp
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                val icon = when (address.addressType) {
                                    AddressType.HOME -> painterResource(id = R.drawable.ic_home)
                                    AddressType.WORK -> painterResource(id = R.drawable.ic_work)
                                    AddressType.OTHER -> painterResource(id = R.drawable.ic_location)
                                }
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .background(CravexaPurple100, shape = CravexaShapes.small),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        painter = icon,
                                        contentDescription = null,
                                        tint = CravexaPurple800,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = address.addressType.name,
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                    color = CravexaPurple800
                                )
                            }

                            TextButton(onClick = { showAddressSheet = true }) {
                                Text(
                                    text = "Change",
                                    fontWeight = FontWeight.Bold,
                                    color = CravexaOrange500
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = address.fullName,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = address.formattedAddress,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Phone: +91 ${address.phone}",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    CravexaCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNavigateToAddAddress() },
                        containerColor = CravexaOrange50,
                        contentPadding = 16.dp
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column {
                                Text(
                                    text = "No Delivery Address Selected",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = CravexaPurple900
                                )
                                Text(
                                    text = "Add your address to proceed with checkout",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            CravexaButton(
                                text = "+ Add",
                                onClick = onNavigateToAddAddress,
                                style = CravexaButtonStyle.PRIMARY,
                                height = 36.dp
                            )
                        }
                    }
                }
            }

            // 2. Delivery Logistics & Hygiene Pledge
            item {
                CravexaCard(
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = CravexaPurple800,
                    contentPadding = 14.dp
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_track),
                            contentDescription = null,
                            tint = CravexaOrange500,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "🚚 Expected Delivery: Tomorrow by 6:00 PM",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                            Text(
                                text = "Handcrafted fresh • Tamper-proof hygienic packaging",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            }

            // 3. Items Review (Grouped by Seller)
            item {
                val sellerName = uiState.items.firstOrNull()?.product?.sellerName ?: "Home Creator Kitchen"
                Text(
                    text = "Specialties from $sellerName (${uiState.items.size} items)",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            items(uiState.items, key = { it.product.id }) { item ->
                CheckoutItemRow(item = item)
            }

            // 4. Payment Method Selection
            item {
                Text(
                    text = "Payment Method",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(8.dp))

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    PaymentMethodOption(
                        method = PaymentMethod.ONLINE,
                        isSelected = uiState.selectedPaymentMethod == PaymentMethod.ONLINE,
                        onSelect = { viewModel.selectPaymentMethod(PaymentMethod.ONLINE) }
                    )

                    PaymentMethodOption(
                        method = PaymentMethod.COD,
                        isSelected = uiState.selectedPaymentMethod == PaymentMethod.COD,
                        onSelect = { viewModel.selectPaymentMethod(PaymentMethod.COD) }
                    )
                }
            }

            // 5. Coupon Code Section
            item {
                Text(
                    text = "Coupons & Offers",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(8.dp))

                CravexaCard(
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentPadding = 12.dp
                ) {
                    if (uiState.appliedCoupon != null) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_verified),
                                    contentDescription = null,
                                    tint = CravexaSuccess,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "${uiState.appliedCoupon} Applied!",
                                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                        color = CravexaSuccess
                                    )
                                    Text(
                                        text = "Saved ₹${uiState.promoDiscount.toInt()} on this order",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            TextButton(onClick = { viewModel.removeCoupon() }) {
                                Text(text = "Remove", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                            }
                        }
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = couponInput,
                                onValueChange = { couponInput = it },
                                placeholder = { Text("Enter coupon (e.g. CRAVE10)") },
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            CravexaButton(
                                text = "Apply",
                                onClick = {
                                    if (couponInput.isNotBlank()) {
                                        viewModel.applyCoupon(couponInput)
                                        couponInput = ""
                                    }
                                },
                                style = CravexaButtonStyle.SECONDARY,
                                height = 48.dp
                            )
                        }
                    }
                }
            }

            // 6. Detailed Bill Breakdown
            item {
                Text(
                    text = "Bill Details",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(8.dp))

                CravexaCard(
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentPadding = 16.dp
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        CheckoutBillRow(label = "Item Total", value = uiState.pricing.formattedSubtotal)

                        CheckoutBillRow(
                            label = "Delivery Fee",
                            value = uiState.pricing.formattedDeliveryFee,
                            isHighlighted = uiState.pricing.isFreeDelivery
                        )

                        CheckoutBillRow(label = "Platform & Hygiene Packaging Fee", value = uiState.pricing.formattedPlatformFee)

                        if (uiState.pricing.discount > 0) {
                            CheckoutBillRow(
                                label = "Coupon Discount",
                                value = uiState.pricing.formattedDiscount,
                                isDiscount = true
                            )
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Grand Total",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = CravexaPurple900
                            )
                            Text(
                                text = uiState.pricing.formattedTotal,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black),
                                color = CravexaPurple900
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }

    // Address Selection Bottom Sheet
    if (showAddressSheet) {
        ModalBottomSheet(
            onDismissRequest = { showAddressSheet = false },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Select Delivery Address",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    TextButton(onClick = {
                        showAddressSheet = false
                        onNavigateToAddAddress()
                    }) {
                        Text(text = "+ Add New", fontWeight = FontWeight.Bold, color = CravexaOrange500)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                uiState.addresses.forEach { addr ->
                    val isSelected = uiState.selectedAddress?.id == addr.id
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) CravexaPurple100 else MaterialTheme.colorScheme.background)
                            .border(
                                width = if (isSelected) 1.5.dp else 1.dp,
                                color = if (isSelected) CravexaPurple800 else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable {
                                viewModel.selectAddress(addr)
                                scope.launch {
                                    sheetState.hide()
                                    showAddressSheet = false
                                }
                            }
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = {
                                    viewModel.selectAddress(addr)
                                    scope.launch {
                                        sheetState.hide()
                                        showAddressSheet = false
                                    }
                                },
                                colors = RadioButtonDefaults.colors(selectedColor = CravexaPurple800)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = addr.fullName,
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    CravexaBadge(text = addr.addressType.name, type = CravexaBadgeType.FOOD_TAG)
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = addr.formattedAddress,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun CheckoutItemRow(item: CartItem) {
    CravexaCard(
        modifier = Modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.surface,
        contentPadding = 12.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(CravexaOrange50),
                contentAlignment = Alignment.Center
            ) {
                if (!item.product.imageUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = item.product.imageUrl,
                        contentDescription = item.product.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Image(
                        painter = painterResource(
                            id = when (item.product.categoryId) {
                                "cat_pickles" -> R.drawable.ic_onboarding_homemade
                                "cat_spices" -> R.drawable.ic_onboarding_regional
                                "cat_snacks" -> R.drawable.ic_onboarding_creators
                                "cat_sweets" -> R.drawable.ic_onboarding_welcome
                                else -> R.drawable.ic_onboarding_homemade
                            }
                        ),
                        contentDescription = item.product.name,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.product.name,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${item.product.weight} • Qty: ${item.quantity}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = "₹${item.totalPrice.toInt()}",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = CravexaPurple900
                )
            )
        }
    }
}

@Composable
private fun PaymentMethodOption(
    method: PaymentMethod,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) CravexaPurple100 else MaterialTheme.colorScheme.surface)
            .border(
                width = if (isSelected) 1.5.dp else 1.dp,
                color = if (isSelected) CravexaPurple800 else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onSelect)
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = isSelected,
                onClick = onSelect,
                colors = RadioButtonDefaults.colors(selectedColor = CravexaPurple800)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = method.title,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (method == PaymentMethod.ONLINE) {
                        Spacer(modifier = Modifier.width(6.dp))
                        CravexaBadge(text = "RECOMMENDED", type = CravexaBadgeType.SUCCESS)
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = method.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun CheckoutBillRow(
    label: String,
    value: String,
    isHighlighted: Boolean = false,
    isDiscount: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = if (isHighlighted || isDiscount) FontWeight.Bold else FontWeight.Medium
            ),
            color = when {
                isDiscount -> CravexaOrange500
                isHighlighted -> CravexaPurple800
                else -> MaterialTheme.colorScheme.onSurface
            }
        )
    }
}

