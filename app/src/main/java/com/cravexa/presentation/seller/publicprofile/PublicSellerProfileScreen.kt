package com.cravexa.presentation.seller.publicprofile

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
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
import com.cravexa.core.common.Resource
import com.cravexa.core.designsystem.components.CravexaBadge
import com.cravexa.core.designsystem.components.CravexaBadgeType
import com.cravexa.core.designsystem.components.CravexaCard
import com.cravexa.core.designsystem.components.CravexaEmptyState
import com.cravexa.core.designsystem.components.CravexaErrorState
import com.cravexa.core.designsystem.components.CravexaLoadingIndicator
import com.cravexa.core.designsystem.components.CravexaTopAppBar
import com.cravexa.core.designsystem.components.ProductCard
import com.cravexa.core.designsystem.theme.CravexaOrange500
import com.cravexa.core.designsystem.theme.CravexaPurple100
import com.cravexa.core.designsystem.theme.CravexaPurple800
import com.cravexa.core.designsystem.theme.CravexaPurple900
import kotlinx.coroutines.flow.collectLatest

@Composable
fun PublicSellerProfileScreen(
    onNavigateToProduct: (String) -> Unit,
    onBack: () -> Unit,
    viewModel: PublicSellerProfileViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val sellerInfo by viewModel.sellerInfo.collectAsState()
    val productsState by viewModel.productsState.collectAsState()
    val wishlistIds by viewModel.wishlistIds.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.userMessage.collectLatest { msg ->
            snackbarHostState.showSnackbar(msg)
        }
    }

    Scaffold(
        topBar = {
            CravexaTopAppBar(
                title = "Creator Kitchen",
                showBackButton = true,
                onBackClick = onBack
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val state = productsState) {
                is Resource.Loading -> {
                    CravexaLoadingIndicator()
                }
                is Resource.Error -> {
                    CravexaErrorState(
                        message = state.message ?: "Unable to load seller profile.",
                        onRetry = { viewModel.loadSellerProfile() }
                    )
                }
                is Resource.Success -> {
                    val products = state.data ?: emptyList()

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // Header section in LazyGrid span
                        item(span = { GridItemSpan(2) }) {
                            sellerInfo?.let { seller ->
                                Column {
                                    CravexaCard(
                                        modifier = Modifier.fillMaxWidth(),
                                        containerColor = MaterialTheme.colorScheme.surface
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(68.dp)
                                                    .clip(CircleShape)
                                                    .background(CravexaPurple100),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.ic_store),
                                                    contentDescription = null,
                                                    tint = CravexaPurple800,
                                                    modifier = Modifier.size(36.dp)
                                                )
                                            }

                                            Spacer(modifier = Modifier.width(16.dp))

                                            Column(modifier = Modifier.weight(1f)) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Text(
                                                        text = seller.brandName,
                                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                                        color = MaterialTheme.colorScheme.onSurface
                                                    )
                                                    if (seller.isVerified) {
                                                        Spacer(modifier = Modifier.width(4.dp))
                                                        Icon(
                                                            painter = painterResource(id = R.drawable.ic_verified),
                                                            contentDescription = "Verified Kitchen",
                                                            tint = CravexaOrange500,
                                                            modifier = Modifier.size(18.dp)
                                                        )
                                                    }
                                                }

                                                Text(
                                                    text = "By ${seller.creatorName} • ${seller.location}",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )

                                                Spacer(modifier = Modifier.height(4.dp))

                                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                                    CravexaBadge(
                                                        text = "${seller.rating} ★ (${seller.reviewCount} reviews)",
                                                        type = CravexaBadgeType.RATING
                                                    )
                                                    if (seller.fssaiCompliant) {
                                                        CravexaBadge(
                                                            text = "FSSAI Registered",
                                                            type = CravexaBadgeType.SUCCESS
                                                        )
                                                    }
                                                }
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(14.dp))

                                        Text(
                                            text = seller.story,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            lineHeight = 18.sp
                                        )

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Text(
                                            text = seller.memberSince,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = CravexaPurple800
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(16.dp))

                                    Text(
                                        text = "Creations from this Kitchen (${products.size})",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }
                        }

                        if (products.isEmpty()) {
                            item(span = { GridItemSpan(2) }) {
                                CravexaEmptyState(
                                    title = "No Products Available",
                                    description = "This creator is currently resting their kitchen or restocking.",
                                    actionButtonText = "Explore Other Creators",
                                    onActionClick = onBack
                                )
                            }
                        } else {
                            items(products, key = { it.id }) { product ->
                                ProductCard(
                                    product = product,
                                    onClick = { onNavigateToProduct(product.id) },
                                    onAddToCart = { viewModel.addToCart(product) },
                                    onToggleWishlist = { viewModel.toggleWishlist(product) },
                                    isWishlisted = wishlistIds.contains(product.id)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
