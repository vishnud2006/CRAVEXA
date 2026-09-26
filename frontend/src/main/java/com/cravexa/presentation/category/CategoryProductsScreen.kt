package com.cravexa.presentation.category

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cravexa.R
import com.cravexa.core.common.Resource
import com.cravexa.core.designsystem.components.CravexaCard
import com.cravexa.core.designsystem.components.CravexaEmptyState
import com.cravexa.core.designsystem.components.CravexaErrorState
import com.cravexa.core.designsystem.components.CravexaLoadingIndicator
import com.cravexa.core.designsystem.components.CravexaTopAppBar
import com.cravexa.core.designsystem.components.ProductCard
import com.cravexa.core.designsystem.theme.CravexaOrange500
import com.cravexa.core.designsystem.theme.CravexaPurple800
import com.cravexa.core.designsystem.theme.CravexaPurple900
import com.cravexa.domain.model.SortOption
import kotlinx.coroutines.flow.collectLatest

@Composable
fun CategoryProductsScreen(
    onNavigateToProduct: (String) -> Unit,
    onBack: () -> Unit,
    viewModel: CategoryProductsViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val productsState by viewModel.productsState.collectAsState()
    val categoryInfo by viewModel.categoryInfo.collectAsState()
    val selectedSort by viewModel.selectedSort.collectAsState()
    val wishlistIds by viewModel.wishlistIds.collectAsState()

    var showSortMenu by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.userMessage.collectLatest { msg ->
            snackbarHostState.showSnackbar(msg)
        }
    }

    Scaffold(
        topBar = {
            CravexaTopAppBar(
                title = viewModel.categoryName,
                showBackButton = true,
                onBackClick = onBack,
                actions = {
                    Box {
                        IconButton(onClick = { showSortMenu = true }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_filter),
                                contentDescription = "Sort",
                                tint = Color.White
                            )
                        }
                        DropdownMenu(
                            expanded = showSortMenu,
                            onDismissRequest = { showSortMenu = false }
                        ) {
                            SortOption.entries.forEach { option ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = option.displayTitle,
                                            fontWeight = if (selectedSort == option) FontWeight.Bold else FontWeight.Normal,
                                            color = if (selectedSort == option) CravexaOrange500 else MaterialTheme.colorScheme.onSurface
                                        )
                                    },
                                    onClick = {
                                        showSortMenu = false
                                        viewModel.setSortOption(option)
                                    }
                                )
                            }
                        }
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
        ) {
            // Category info banner
            categoryInfo?.let { category ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CravexaPurple800)
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = category.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }

            // Products Content
            when (val state = productsState) {
                is Resource.Loading -> {
                    CravexaLoadingIndicator()
                }
                is Resource.Error -> {
                    CravexaErrorState(
                        message = state.message ?: "Unable to load products.",
                        onRetry = { viewModel.loadCategoryData() }
                    )
                }
                is Resource.Success -> {
                    val products = state.data ?: emptyList()
                    if (products.isEmpty()) {
                        CravexaEmptyState(
                            title = "No Products in This Category",
                            description = "Our home creators are preparing fresh batches for ${viewModel.categoryName}. Check back soon!",
                            actionButtonText = "Back to Categories",
                            onActionClick = onBack
                        )
                    } else {
                        // Product Count & Active Sort summary
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${products.size} Delicacies Found",
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = "Sorted by: ${selectedSort.displayTitle}",
                                style = MaterialTheme.typography.labelSmall,
                                color = CravexaOrange500
                            )
                        }

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
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
