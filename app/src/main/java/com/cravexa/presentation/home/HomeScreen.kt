package com.cravexa.presentation.home

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cravexa.R
import com.cravexa.core.common.Resource
import com.cravexa.core.designsystem.components.CategoryCard
import com.cravexa.core.designsystem.components.CravexaBadge
import com.cravexa.core.designsystem.components.CravexaBadgeType
import com.cravexa.core.designsystem.components.CravexaCard
import com.cravexa.core.designsystem.components.CravexaHomeHeader
import com.cravexa.core.designsystem.components.HeroBannerCard
import com.cravexa.core.designsystem.components.ProductCard
import com.cravexa.core.designsystem.theme.CravexaOrange50
import com.cravexa.core.designsystem.theme.CravexaOrange500
import com.cravexa.core.designsystem.theme.CravexaPurple100
import com.cravexa.core.designsystem.theme.CravexaPurple800
import com.cravexa.core.designsystem.theme.CravexaPurple900
import com.cravexa.core.designsystem.theme.CravexaShapes
import com.cravexa.domain.model.Product
import com.cravexa.domain.model.UserRole
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomeScreen(
    onNavigateToSearch: (String?) -> Unit = {},
    onNavigateToCategory: (String, String) -> Unit = { _, _ -> },
    onNavigateToProduct: (String) -> Unit = {},
    onNavigateToSeller: (String) -> Unit = {},
    onNavigateToCart: () -> Unit = {},
    onLogout: () -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val heroBanners by viewModel.heroBanners.collectAsState()
    val categoriesState by viewModel.categories.collectAsState()
    val featuredState by viewModel.featuredProducts.collectAsState()
    val trendingState by viewModel.trendingProducts.collectAsState()
    val recommendedState by viewModel.recommendedProducts.collectAsState()
    val regionalState by viewModel.regionalProducts.collectAsState()
    val wishlistIds by viewModel.wishlistIds.collectAsState()
    val cartCount by viewModel.cartItemCount.collectAsState()

    var showLogoutDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.userMessage.collectLatest { msg ->
            snackbarHostState.showSnackbar(msg)
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = {
                Text(
                    text = "Confirm Logout",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to sign out of CRAVEXA?",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        viewModel.logout(onLogout)
                    }
                ) {
                    Text(
                        text = "Logout",
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text(text = "Cancel", color = CravexaPurple800)
                }
            }
        )
    }

    Scaffold(
        topBar = {
            CravexaHomeHeader(
                locationAddress = "Home • Indiranagar, Bengaluru",
                onLocationClick = { /* Address selector in Phase 4 */ },
                cartItemCount = cartCount,
                onCartClick = onNavigateToCart
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // Header Search Bar & Greeting Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CravexaPurple800)
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            ) {
                Column {
                    // Active User Greeting Row
                    currentUser?.let { user ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Namaste, ${user.name.ifBlank { "Food Lover" }}",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                CravexaBadge(
                                    text = if (user.role == UserRole.SELLER) "CREATOR" else "CUSTOMER",
                                    type = if (user.role == UserRole.SELLER) CravexaBadgeType.FOOD_TAG else CravexaBadgeType.PRIMARY
                                )
                            }

                            IconButton(onClick = { showLogoutDialog = true }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_refresh),
                                    contentDescription = "Logout",
                                    tint = Color.White.copy(alpha = 0.8f),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }

                    // Search Trigger Bar (Clicking opens full Search screen)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .clip(CravexaShapes.medium)
                            .background(Color.White)
                            .clickable { onNavigateToSearch(null) }
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_search),
                                    contentDescription = null,
                                    tint = CravexaPurple800,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Search homemade pickles, sweets, spices...",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                )
                            }
                            Icon(
                                painter = painterResource(id = R.drawable.ic_filter),
                                contentDescription = "Filter",
                                tint = CravexaOrange500,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 1. Hero Banners Carousel
            if (heroBanners.isNotEmpty()) {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(heroBanners) { banner ->
                        HeroBannerCard(
                            banner = banner,
                            onClick = {
                                banner.categoryId?.let { catId ->
                                    onNavigateToCategory(catId, "Specialties")
                                }
                            },
                            modifier = Modifier.width(320.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            // 2. Popular Categories Carousel
            when (val state = categoriesState) {
                is Resource.Success -> {
                    val categories = state.data ?: emptyList()
                    if (categories.isNotEmpty()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Popular Categories",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = "View All (${categories.size})",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = CravexaOrange500,
                                modifier = Modifier.clickable { onNavigateToSearch(null) }
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(categories) { category ->
                                CategoryCard(
                                    category = category,
                                    onClick = { onNavigateToCategory(category.id, category.name) }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
                else -> Unit
            }

            // 3. Featured Homemade Delights (Horizontal Product Carousel)
            when (val state = featuredState) {
                is Resource.Success -> {
                    val products = state.data ?: emptyList()
                    if (products.isNotEmpty()) {
                        ProductSectionRow(
                            title = "Featured Homemade Delights",
                            subtitle = "Handcrafted with pure love by trusted food creators",
                            products = products,
                            wishlistIds = wishlistIds,
                            onProductClick = onNavigateToProduct,
                            onAddToCart = { viewModel.addToCart(it) },
                            onToggleWishlist = { viewModel.toggleWishlist(it) },
                            onSeeAll = { onNavigateToSearch(null) }
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
                else -> Unit
            }

            // 4. Spotlight on Creator Kitchens
            PopularSellersSpotlight(
                onSellerClick = onNavigateToSeller
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 5. Trending Creations (Horizontal Carousel)
            when (val state = trendingState) {
                is Resource.Success -> {
                    val products = state.data ?: emptyList()
                    if (products.isNotEmpty()) {
                        ProductSectionRow(
                            title = "Trending Delicacies 🔥",
                            subtitle = "Loved by food enthusiasts this week",
                            products = products,
                            wishlistIds = wishlistIds,
                            onProductClick = onNavigateToProduct,
                            onAddToCart = { viewModel.addToCart(it) },
                            onToggleWishlist = { viewModel.toggleWishlist(it) },
                            onSeeAll = { onNavigateToSearch(null) }
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
                else -> Unit
            }

            // 6. Regional Heritage Specialties
            when (val state = regionalState) {
                is Resource.Success -> {
                    val products = state.data ?: emptyList()
                    if (products.isNotEmpty()) {
                        ProductSectionRow(
                            title = "Regional Heritage Flavors",
                            subtitle = "Authentic secret recipes from across India",
                            products = products,
                            wishlistIds = wishlistIds,
                            onProductClick = onNavigateToProduct,
                            onAddToCart = { viewModel.addToCart(it) },
                            onToggleWishlist = { viewModel.toggleWishlist(it) },
                            onSeeAll = { onNavigateToSearch(null) }
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
                else -> Unit
            }

            // 7. Recommended For You (Grid Cards)
            when (val state = recommendedState) {
                is Resource.Success -> {
                    val products = state.data ?: emptyList()
                    if (products.isNotEmpty()) {
                        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                            Text(
                                text = "Recommended For You",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = "Top rated homemade items you might love",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            products.take(6).chunked(2).forEach { rowProducts ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    rowProducts.forEach { product ->
                                        ProductCard(
                                            product = product,
                                            onClick = { onNavigateToProduct(product.id) },
                                            onAddToCart = { viewModel.addToCart(product) },
                                            onToggleWishlist = { viewModel.toggleWishlist(product) },
                                            isWishlisted = wishlistIds.contains(product.id),
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                    if (rowProducts.size == 1) {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                        }
                    }
                }
                else -> Unit
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun ProductSectionRow(
    title: String,
    subtitle: String,
    products: List<Product>,
    wishlistIds: Set<String>,
    onProductClick: (String) -> Unit,
    onAddToCart: (Product) -> Unit,
    onToggleWishlist: (Product) -> Unit,
    onSeeAll: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = "See All",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = CravexaOrange500,
                modifier = Modifier.clickable(onClick = onSeeAll)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(products) { product ->
                ProductCard(
                    product = product,
                    onClick = { onProductClick(product.id) },
                    onAddToCart = { onAddToCart(product) },
                    onToggleWishlist = { onToggleWishlist(product) },
                    isWishlisted = wishlistIds.contains(product.id),
                    modifier = Modifier.width(200.dp)
                )
            }
        }
    }
}

@Composable
private fun PopularSellersSpotlight(
    onSellerClick: (String) -> Unit
) {
    val creators = listOf(
        Triple("sel_1", "Lakshmi's Home Kitchen", "Guntur, AP • Pickles & Podis"),
        Triple("sel_2", "Srivari Sweets & Delights", "Mysuru, KA • Ghee Sweets"),
        Triple("sel_4", "Meenakshi Ammal Podi Studio", "Madurai, TN • Heritage Masalas"),
        Triple("sel_7", "Prakriti Wholesome Bakes", "Bengaluru, KA • Sourdough & Millets")
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Popular Creator Kitchens",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "Discover beloved passionate home chefs in your region",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(creators) { (sellerId, brandName, tagline) ->
                CravexaCard(
                    modifier = Modifier
                        .width(240.dp)
                        .clickable { onSellerClick(sellerId) },
                    containerColor = MaterialTheme.colorScheme.surface
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(CravexaPurple100),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_store),
                                contentDescription = null,
                                tint = CravexaPurple800,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = brandName,
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1
                            )
                            Text(
                                text = tagline,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}
