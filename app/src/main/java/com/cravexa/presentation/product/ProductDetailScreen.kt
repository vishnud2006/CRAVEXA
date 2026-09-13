package com.cravexa.presentation.product

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.cravexa.R
import com.cravexa.core.common.Resource
import com.cravexa.core.designsystem.components.CravexaBadge
import com.cravexa.core.designsystem.components.CravexaBadgeType
import com.cravexa.core.designsystem.components.CravexaButton
import com.cravexa.core.designsystem.components.CravexaButtonStyle
import com.cravexa.core.designsystem.components.CravexaCard
import com.cravexa.core.designsystem.components.CravexaEmptyState
import com.cravexa.core.designsystem.components.CravexaErrorState
import com.cravexa.core.designsystem.components.CravexaLoadingIndicator
import com.cravexa.core.designsystem.components.CravexaTopAppBar
import com.cravexa.core.designsystem.components.ProductCard
import com.cravexa.core.designsystem.theme.CravexaOrange50
import com.cravexa.core.designsystem.theme.CravexaOrange500
import com.cravexa.core.designsystem.theme.CravexaPurple100
import com.cravexa.core.designsystem.theme.CravexaPurple800
import com.cravexa.core.designsystem.theme.CravexaPurple900
import com.cravexa.core.designsystem.theme.CravexaShapes
import com.cravexa.domain.model.Product
import com.cravexa.domain.model.ProductAvailability
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProductDetailScreen(
    onNavigateToSeller: (String) -> Unit,
    onNavigateToProduct: (String) -> Unit,
    onBack: () -> Unit,
    viewModel: ProductDetailViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val productState by viewModel.productState.collectAsState()
    val relatedState by viewModel.relatedProducts.collectAsState()
    val quantity by viewModel.quantity.collectAsState()
    val isWishlisted by viewModel.isWishlisted.collectAsState()

    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.userMessage.collectLatest { msg ->
            snackbarHostState.showSnackbar(msg)
        }
    }

    Scaffold(
        topBar = {
            CravexaTopAppBar(
                title = "Delicacy Details",
                showBackButton = true,
                onBackClick = onBack,
                actions = {
                    val currentProduct = (productState as? Resource.Success)?.data
                    currentProduct?.let { product ->
                        // Share action
                        IconButton(
                            onClick = {
                                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(
                                        Intent.EXTRA_TEXT,
                                        "Discover authentic homemade ${product.name} by ${product.sellerName} on CRAVEXA — CRAVE BETTER!\nPrice: ₹${product.price.toInt()} / ${product.weight}"
                                    )
                                }
                                context.startActivity(Intent.createChooser(shareIntent, "Share Delicacy"))
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_share),
                                contentDescription = "Share",
                                tint = Color.White
                            )
                        }

                        // Wishlist toggle action
                        IconButton(onClick = { viewModel.toggleWishlist() }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_heart),
                                contentDescription = "Wishlist",
                                tint = if (isWishlisted) CravexaOrange500 else Color.White
                            )
                        }
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            val currentProduct = (productState as? Resource.Success)?.data
            if (currentProduct != null) {
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
                                text = "Total Price",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "₹${(currentProduct.price * quantity).toInt()}",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Black,
                                    color = CravexaPurple900
                                )
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        if (currentProduct.availability.isPurchasable) {
                            CravexaButton(
                                text = "Add to Cart ($quantity)",
                                onClick = { viewModel.addToCart() },
                                style = CravexaButtonStyle.PRIMARY,
                                modifier = Modifier.weight(1f)
                            )
                        } else {
                            CravexaButton(
                                text = "Currently Unavailable",
                                onClick = {},
                                enabled = false,
                                style = CravexaButtonStyle.SECONDARY,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        when (val state = productState) {
            is Resource.Loading -> {
                CravexaLoadingIndicator()
            }
            is Resource.Error -> {
                CravexaErrorState(
                    message = state.message ?: "Unable to load product details.",
                    onRetry = { viewModel.loadProduct() }
                )
            }
            is Resource.Success -> {
                val product = state.data
                if (product == null) {
                    CravexaEmptyState(
                        title = "Product Not Found",
                        description = "This homemade delicacy is no longer available.",
                        actionButtonText = "Back to Home",
                        onActionClick = onBack
                    )
                } else {
                    Column(
                        modifier = modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .verticalScroll(rememberScrollState())
                    ) {
                        // 1. Hero Image / Gallery View
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(260.dp)
                                .background(CravexaOrange50),
                            contentAlignment = Alignment.Center
                        ) {
                            if (!product.imageUrl.isNullOrBlank()) {
                                AsyncImage(
                                    model = product.imageUrl,
                                    contentDescription = product.name,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                Image(
                                    painter = painterResource(
                                        id = when (product.categoryId) {
                                            "cat_pickles" -> R.drawable.ic_onboarding_homemade
                                            "cat_spices" -> R.drawable.ic_onboarding_regional
                                            "cat_snacks" -> R.drawable.ic_onboarding_creators
                                            "cat_sweets" -> R.drawable.ic_onboarding_welcome
                                            else -> R.drawable.ic_onboarding_homemade
                                        }
                                    ),
                                    contentDescription = product.name,
                                    modifier = Modifier.size(140.dp)
                                )
                            }

                            // Tag Badge
                            product.tag?.let { tag ->
                                CravexaBadge(
                                    text = tag,
                                    type = CravexaBadgeType.FOOD_TAG,
                                    modifier = Modifier
                                        .align(Alignment.TopStart)
                                        .padding(16.dp)
                                )
                            }
                        }

                        // 2. Product Core Info Section
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CravexaBadge(
                                    text = product.categoryName.uppercase(),
                                    type = CravexaBadgeType.PRIMARY
                                )
                                CravexaBadge(
                                    text = "${product.rating} ★ (${product.reviewCount} reviews)",
                                    type = CravexaBadgeType.RATING
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = product.name,
                                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onBackground
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Pricing & Discount
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "₹${product.price.toInt()}",
                                    style = MaterialTheme.typography.headlineMedium.copy(
                                        fontWeight = FontWeight.Black,
                                        color = CravexaPurple900
                                    )
                                )

                                if (product.originalPrice != null && product.originalPrice > product.price) {
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = "₹${product.originalPrice.toInt()}",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            textDecoration = TextDecoration.LineThrough
                                        ),
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    product.discountPercentage?.let { discount ->
                                        CravexaBadge(
                                            text = "$discount% OFF",
                                            type = CravexaBadgeType.SUCCESS
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "(${product.weight})",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Quantity Stepper
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Select Quantity",
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "${product.availability.displayTitle} (${product.stock} available)",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = if (product.stock <= 5) MaterialTheme.colorScheme.error else CravexaPurple800
                                    )
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(CravexaPurple100)
                                        .padding(horizontal = 4.dp, vertical = 2.dp)
                                ) {
                                    IconButton(
                                        onClick = { viewModel.decrementQuantity() },
                                        enabled = quantity > 1,
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Text(text = "-", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = CravexaPurple900)
                                    }

                                    Text(
                                        text = "$quantity",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = CravexaPurple900,
                                        modifier = Modifier.padding(horizontal = 12.dp)
                                    )

                                    IconButton(
                                        onClick = { viewModel.incrementQuantity() },
                                        enabled = quantity < product.stock,
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Text(text = "+", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = CravexaPurple900)
                                    }
                                }
                            }
                        }

                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

                        // 3. Creator Kitchen Card
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Crafted By Food Creator",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onBackground
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            CravexaCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onNavigateToSeller(product.sellerId) },
                                containerColor = MaterialTheme.colorScheme.surface
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(52.dp)
                                            .clip(CircleShape)
                                            .background(CravexaPurple100),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_store),
                                            contentDescription = null,
                                            tint = CravexaPurple800,
                                            modifier = Modifier.size(26.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(14.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = product.sellerName,
                                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Icon(
                                                painter = painterResource(id = R.drawable.ic_verified),
                                                contentDescription = "Verified Kitchen",
                                                tint = CravexaOrange500,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                        Text(
                                            text = product.sellerLocation,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_chevron_right),
                                        contentDescription = "View Profile",
                                        tint = CravexaPurple800,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }

                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

                        // 4. Description & Specifications
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "About This Delicacy",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onBackground
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = product.description,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 22.sp
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Ingredients chips
                            if (product.ingredients.isNotEmpty()) {
                                Text(
                                    text = "Pure Ingredients",
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                FlowRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    product.ingredients.forEach { ing ->
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(CravexaPurple100)
                                                .padding(horizontal = 10.dp, vertical = 5.dp)
                                        ) {
                                            Text(
                                                text = ing,
                                                style = MaterialTheme.typography.labelSmall,
                                                color = CravexaPurple900
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                            }

                            // Heritage & Storage Specifications
                            CravexaCard(
                                modifier = Modifier.fillMaxWidth(),
                                containerColor = CravexaOrange50
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    SpecificationRow("Regional Origin", product.region)
                                    SpecificationRow("Dietary Type", product.foodType)
                                    SpecificationRow("Shelf Life", product.shelfLife)
                                    SpecificationRow("Storage", product.storageInstructions)
                                    SpecificationRow("Delivery Estimate", "Delivery estimate available at checkout.")
                                }
                            }
                        }

                        // 5. Related Homemade Delights
                        when (val relState = relatedState) {
                            is Resource.Success -> {
                                val related = relState.data ?: emptyList()
                                if (related.isNotEmpty()) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 8.dp, bottom = 24.dp)
                                    ) {
                                        Text(
                                            text = "More From ${product.categoryName}",
                                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                            color = MaterialTheme.colorScheme.onBackground,
                                            modifier = Modifier.padding(horizontal = 16.dp)
                                        )
                                        Spacer(modifier = Modifier.height(12.dp))
                                        LazyRow(
                                            contentPadding = PaddingValues(horizontal = 16.dp),
                                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                                        ) {
                                            items(related) { relProduct ->
                                                ProductCard(
                                                    product = relProduct,
                                                    onClick = { onNavigateToProduct(relProduct.id) },
                                                    onAddToCart = { viewModel.addToCartProduct(relProduct) },
                                                    onToggleWishlist = { viewModel.toggleWishlistProduct(relProduct) },
                                                    isWishlisted = false,
                                                    modifier = Modifier.width(200.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                            else -> Unit
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun SpecificationRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
            color = CravexaPurple900,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1.6f)
        )
    }
}
