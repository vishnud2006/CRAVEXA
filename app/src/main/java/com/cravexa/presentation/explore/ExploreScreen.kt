package com.cravexa.presentation.explore

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.cravexa.R
import com.cravexa.core.common.Resource
import com.cravexa.core.designsystem.components.CategoryCard
import com.cravexa.core.designsystem.components.CravexaCard
import com.cravexa.core.designsystem.components.CravexaChip
import com.cravexa.core.designsystem.components.CravexaTopAppBar
import com.cravexa.core.designsystem.components.ProductCard
import com.cravexa.core.designsystem.theme.CravexaOrange50
import com.cravexa.core.designsystem.theme.CravexaOrange500
import com.cravexa.core.designsystem.theme.CravexaPurple100
import com.cravexa.core.designsystem.theme.CravexaPurple800
import com.cravexa.core.designsystem.theme.CravexaShapes
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ExploreScreen(
    onNavigateToSearch: (String?) -> Unit = {},
    onNavigateToCategory: (String, String) -> Unit = { _, _ -> },
    onNavigateToProduct: (String) -> Unit = {},
    onNavigateToSeller: (String) -> Unit = {},
    viewModel: ExploreViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val categoriesState by viewModel.categories.collectAsState()
    val regionalState by viewModel.regionalSpecialties.collectAsState()
    val trendingState by viewModel.trendingProducts.collectAsState()
    val wishlistIds by viewModel.wishlistIds.collectAsState()

    var selectedRegion by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.userMessage.collectLatest { msg ->
            snackbarHostState.showSnackbar(msg)
        }
    }

    val regions = listOf("All Regions", "Andhra Pradesh", "Karnataka", "Kerala", "Tamil Nadu", "Rajasthan", "Gujarat", "West Bengal")

    Scaffold(
        topBar = {
            CravexaTopAppBar(
                title = "Explore Delicacies",
                actions = {
                    androidx.compose.material3.IconButton(onClick = { onNavigateToSearch(null) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_search),
                            contentDescription = "Search",
                            tint = Color.White
                        )
                    }
                }
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
            // Search Input Trigger
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(CravexaShapes.medium)
                    .background(Color.White)
                    .clickable { onNavigateToSearch(null) }
                    .padding(horizontal = 16.dp, vertical = 14.dp)
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
                            text = "Search by region, chef, or ingredient...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    }
                }
            }

            // 1. Regional Cuisines Chips
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Culinary Heritage of India",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Text(
                    text = "Explore traditional recipes preserved across states",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(regions) { region ->
                        val isSelected = if (region == "All Regions") selectedRegion == null else selectedRegion == region
                        CravexaChip(
                            text = region,
                            selected = isSelected,
                            onClick = {
                                selectedRegion = if (region == "All Regions" || selectedRegion == region) null else region
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 2. All Categories Grid
            when (val state = categoriesState) {
                is Resource.Success -> {
                    val categories = state.data ?: emptyList()
                    if (categories.isNotEmpty()) {
                        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                            Text(
                                text = "Marketplace Categories",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = "Browse everything from spicy pickles to heritage bakes",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            // 2-column Category Cards
                            categories.chunked(2).forEach { rowCats ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    rowCats.forEach { category ->
                                        CravexaCard(
                                            modifier = Modifier
                                                .weight(1f)
                                                .clickable { onNavigateToCategory(category.id, category.name) },
                                            containerColor = MaterialTheme.colorScheme.surface
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(44.dp)
                                                        .clip(CircleShape)
                                                        .background(CravexaOrange50),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Image(
                                                        painter = painterResource(id = category.iconRes ?: R.drawable.ic_onboarding_homemade),
                                                        contentDescription = category.name,
                                                        modifier = Modifier.size(26.dp)
                                                    )
                                                }

                                                Spacer(modifier = Modifier.width(10.dp))

                                                Column(modifier = Modifier.weight(1f)) {
                                                    Text(
                                                        text = category.name,
                                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                                        color = MaterialTheme.colorScheme.onSurface,
                                                        maxLines = 1
                                                    )
                                                    Text(
                                                        text = "${category.itemCount} items",
                                                        style = MaterialTheme.typography.labelSmall,
                                                        color = CravexaOrange500
                                                    )
                                                }
                                            }
                                        }
                                    }
                                    if (rowCats.size == 1) {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
                else -> Unit
            }

            // 3. Filtered Regional Delicacies Carousel
            when (val state = regionalState) {
                is Resource.Success -> {
                    val allRegional = state.data ?: emptyList()
                    val filteredRegional = if (selectedRegion != null) {
                        allRegional.filter { it.region.equals(selectedRegion, ignoreCase = true) }
                    } else {
                        allRegional
                    }

                    if (filteredRegional.isNotEmpty()) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = if (selectedRegion != null) "$selectedRegion Delicacies" else "Authentic Regional Specialties",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                            Text(
                                text = "Made with indigenous family techniques",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            LazyRow(
                                contentPadding = PaddingValues(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(filteredRegional) { product ->
                                    ProductCard(
                                        product = product,
                                        onClick = { onNavigateToProduct(product.id) },
                                        onAddToCart = { viewModel.addToCart(product) },
                                        onToggleWishlist = { viewModel.toggleWishlist(product) },
                                        isWishlisted = wishlistIds.contains(product.id),
                                        modifier = Modifier.width(200.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }
                }
                else -> Unit
            }

            // 4. Trending Food Discoveries
            when (val state = trendingState) {
                is Resource.Success -> {
                    val trending = state.data ?: emptyList()
                    if (trending.isNotEmpty()) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "Trending This Month",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                            Text(
                                text = "Handmade specialties gaining popularity nationwide",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            LazyRow(
                                contentPadding = PaddingValues(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(trending) { product ->
                                    ProductCard(
                                        product = product,
                                        onClick = { onNavigateToProduct(product.id) },
                                        onAddToCart = { viewModel.addToCart(product) },
                                        onToggleWishlist = { viewModel.toggleWishlist(product) },
                                        isWishlisted = wishlistIds.contains(product.id),
                                        modifier = Modifier.width(200.dp)
                                    )
                                }
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

