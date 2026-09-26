package com.cravexa.presentation.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
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
import com.cravexa.core.designsystem.components.CravexaEmptyState
import com.cravexa.core.designsystem.components.CravexaTopAppBar
import com.cravexa.core.designsystem.theme.CravexaOrange50
import com.cravexa.core.designsystem.theme.CravexaOrange500
import com.cravexa.core.designsystem.theme.CravexaPurple100
import com.cravexa.core.designsystem.theme.CravexaPurple800
import com.cravexa.core.designsystem.theme.CravexaPurple900
import com.cravexa.domain.model.CartItem
import kotlinx.coroutines.flow.collectLatest

@Composable
fun CartScreen(
    onBack: () -> Unit,
    onNavigateToProduct: (String) -> Unit = {},
    onExploreFood: () -> Unit = {},
    onProceedToCheckout: () -> Unit = {},
    viewModel: CartViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.userMessage.collectLatest { msg ->
            snackbarHostState.showSnackbar(msg)
        }
    }

    Scaffold(
        topBar = {
            CravexaTopAppBar(
                title = "My Cart (${uiState.itemCount})",
                showBackButton = true,
                onBackClick = onBack,
                actions = {
                    if (uiState.items.isNotEmpty()) {
                        IconButton(onClick = { viewModel.clearCart() }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_delete),
                                contentDescription = "Clear Cart",
                                tint = Color.White
                            )
                        }
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (uiState.items.isNotEmpty()) {
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
                                text = "Grand Total",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "₹${uiState.total.toInt()}",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Black,
                                    color = CravexaPurple900
                                )
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        CravexaButton(
                            text = "Proceed to Checkout",
                            onClick = {
                                snackbarHostState.currentSnackbarData?.dismiss()
                                onProceedToCheckout()
                            },
                            style = CravexaButtonStyle.PRIMARY,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        if (uiState.items.isEmpty()) {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CravexaEmptyState(
                    title = "Your Cart is Empty",
                    description = "Explore homemade pickles, traditional sweets, freshly ground spices, and snacks handcrafted by passionate home creators.",
                    actionButtonText = "Browse Homemade Delicacies",
                    onActionClick = onExploreFood
                )
            }
        } else {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Free Delivery threshold banner
                item {
                    if (uiState.subtotal < 499) {
                        val needed = 499 - uiState.subtotal
                        CravexaCard(
                            modifier = Modifier.fillMaxWidth(),
                            containerColor = CravexaOrange50,
                            contentPadding = 12.dp
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_track),
                                    contentDescription = null,
                                    tint = CravexaOrange500,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "Add items worth ₹${needed.toInt()} more for FREE Delivery!",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = CravexaPurple900
                                )
                            }
                        }
                    } else {
                        CravexaCard(
                            modifier = Modifier.fillMaxWidth(),
                            containerColor = CravexaPurple100,
                            contentPadding = 12.dp
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_verified),
                                    contentDescription = null,
                                    tint = CravexaPurple800,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "Congratulations! You unlocked FREE Delivery 🎉",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = CravexaPurple900
                                )
                            }
                        }
                    }
                }

                // Section header
                item {
                    Text(
                        text = "Delicacy Items (${uiState.itemCount})",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                // Cart item cards
                items(uiState.items, key = { it.product.id }) { item ->
                    CartItemCard(
                        item = item,
                        onItemClick = { onNavigateToProduct(item.product.id) },
                        onIncrement = { viewModel.incrementQuantity(item) },
                        onDecrement = { viewModel.decrementQuantity(item) },
                        onRemove = { viewModel.removeItem(item) },
                        onSaveForLater = { viewModel.saveForLater(item) }
                    )
                }

                // Bill Details Summary Card
                item {
                    Spacer(modifier = Modifier.height(6.dp))
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
                            BillRow(label = "Item Total", value = "₹${uiState.subtotal.toInt()}")

                            BillRow(
                                label = "Delivery Fee",
                                value = if (uiState.isFreeDelivery) "FREE" else "₹${uiState.deliveryFee.toInt()}",
                                isHighlighted = uiState.isFreeDelivery
                            )

                            BillRow(label = "Platform & Safe Packing Fee", value = "₹${uiState.platformFee.toInt()}")

                            if (uiState.promoDiscount > 0) {
                                BillRow(
                                    label = "Coupon Discount (${uiState.appliedCoupon})",
                                    value = "-₹${uiState.promoDiscount.toInt()}",
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
                                    text = "To Pay",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = CravexaPurple900
                                )
                                Text(
                                    text = "₹${uiState.total.toInt()}",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black),
                                    color = CravexaPurple900
                                )
                            }
                        }
                    }
                }

                // Safety Assurance Badge
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_verified),
                            contentDescription = null,
                            tint = CravexaOrange500,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "100% Authentic Homemade • Strict Hygiene Standards",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
private fun CartItemCard(
    item: CartItem,
    onItemClick: () -> Unit,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onRemove: () -> Unit,
    onSaveForLater: () -> Unit,
    modifier: Modifier = Modifier
) {
    CravexaCard(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onItemClick),
        containerColor = MaterialTheme.colorScheme.surface,
        contentPadding = 12.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Product image
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(10.dp))
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
                        modifier = Modifier.size(44.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Details
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.product.name,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "By ${item.product.sellerName} • ${item.product.weight}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "₹${item.product.price.toInt()}",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = CravexaPurple900
                        )
                    )
                    if (item.product.originalPrice != null && item.product.originalPrice > item.product.price) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "₹${item.product.originalPrice.toInt()}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                textDecoration = TextDecoration.LineThrough
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Stepper & Action buttons
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onSaveForLater,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_heart),
                            contentDescription = "Save to Wishlist",
                            tint = CravexaOrange500,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    IconButton(
                        onClick = onRemove,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_delete),
                            contentDescription = "Remove",
                            tint = MaterialTheme.colorScheme.error.copy(alpha = 0.8f),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(CravexaPurple100)
                        .padding(horizontal = 2.dp, vertical = 1.dp)
                ) {
                    IconButton(
                        onClick = onDecrement,
                        modifier = Modifier.size(26.dp)
                    ) {
                        Text(text = "-", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = CravexaPurple900)
                    }

                    Text(
                        text = "${item.quantity}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = CravexaPurple900,
                        modifier = Modifier.padding(horizontal = 6.dp)
                    )

                    IconButton(
                        onClick = onIncrement,
                        modifier = Modifier.size(26.dp)
                    ) {
                        Text(text = "+", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = CravexaPurple900)
                    }
                }
            }
        }
    }
}

@Composable
private fun BillRow(
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
