package com.cravexa.presentation.search

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.cravexa.core.designsystem.components.CravexaBadge
import com.cravexa.core.designsystem.components.CravexaBadgeType
import com.cravexa.core.designsystem.components.CravexaChip
import com.cravexa.core.designsystem.components.CravexaEmptyState
import com.cravexa.core.designsystem.components.CravexaLoadingIndicator
import com.cravexa.core.designsystem.components.CravexaTextField
import com.cravexa.core.designsystem.components.ProductCard
import com.cravexa.core.designsystem.components.SearchFilterBottomSheet
import com.cravexa.core.designsystem.theme.CravexaOrange500
import com.cravexa.core.designsystem.theme.CravexaPurple100
import com.cravexa.core.designsystem.theme.CravexaPurple800
import com.cravexa.core.designsystem.theme.CravexaPurple900
import com.cravexa.domain.model.SortOption
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SearchScreen(
    onNavigateToProduct: (String) -> Unit,
    onBack: () -> Unit,
    viewModel: SearchViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val filter by viewModel.filter.collectAsState()
    val sort by viewModel.sort.collectAsState()
    val recentSearches by viewModel.recentSearches.collectAsState()
    val categories by viewModel.categories.collectAsState()
    val suggestions by viewModel.suggestions.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val wishlistIds by viewModel.wishlistIds.collectAsState()

    var showFilterSheet by remember { mutableStateOf(false) }
    var showSortMenu by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.userMessage.collectLatest { msg ->
            snackbarHostState.showSnackbar(msg)
        }
    }

    if (showFilterSheet) {
        SearchFilterBottomSheet(
            sheetState = sheetState,
            initialFilter = filter,
            categories = categories,
            onDismiss = { showFilterSheet = false },
            onApply = { newFilter ->
                showFilterSheet = false
                viewModel.applyFilter(newFilter)
            }
        )
    }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CravexaPurple800)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    CravexaTextField(
                        value = searchQuery,
                        onValueChange = { viewModel.onQueryChange(it) },
                        label = "Search Delicacies",
                        placeholder = "Search homemade pickles, spices, sweets...",
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Filter Button with Badge
                    Box {
                        IconButton(onClick = { showFilterSheet = true }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_filter),
                                contentDescription = "Filter",
                                tint = if (filter.isActive) CravexaOrange500 else Color.White
                            )
                        }
                        if (filter.isActive) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(CravexaOrange500)
                                    .align(Alignment.TopEnd)
                            )
                        }
                    }
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Active filters & Sort Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (filter.isActive) {
                    TextButton(onClick = { viewModel.applyFilter(com.cravexa.domain.model.SearchFilter()) }) {
                        Text(
                            text = "Clear active filters ✕",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                } else {
                    Text(
                        text = "Marketplace Search",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Box {
                    TextButton(onClick = { showSortMenu = true }) {
                        Text(
                            text = "Sort: ${sort.displayTitle} ▼",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = CravexaPurple800
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
                                        fontWeight = if (sort == option) FontWeight.Bold else FontWeight.Normal,
                                        color = if (sort == option) CravexaOrange500 else MaterialTheme.colorScheme.onSurface
                                    )
                                },
                                onClick = {
                                    showSortMenu = false
                                    viewModel.setSort(option)
                                }
                            )
                        }
                    }
                }
            }

            // Recent searches (when query is blank)
            if (searchQuery.isBlank() && recentSearches.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Recent Searches",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "Clear All",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.clickable { viewModel.clearRecentSearches() }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        recentSearches.forEach { search ->
                            Row(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(CravexaPurple100)
                                    .clickable { viewModel.submitSearch(search) }
                                    .padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = search,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = CravexaPurple900
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_delete),
                                    contentDescription = "Remove",
                                    tint = CravexaPurple800,
                                    modifier = Modifier
                                        .size(14.dp)
                                        .clickable { viewModel.removeRecentSearch(search) }
                                )
                            }
                        }
                    }
                }
            }

            // Search Results or Loading
            when (val state = searchResults) {
                is Resource.Loading -> {
                    CravexaLoadingIndicator()
                }
                is Resource.Error -> {
                    CravexaEmptyState(
                        title = "Search Error",
                        description = state.message ?: "Unable to complete search.",
                        actionButtonText = "Clear Search",
                        onActionClick = { viewModel.onQueryChange("") }
                    )
                }
                is Resource.Success -> {
                    val products = state.data ?: emptyList()
                    if (products.isEmpty()) {
                        CravexaEmptyState(
                            title = "No Delicacies Found",
                            description = if (searchQuery.isNotBlank()) "No homemade items matched '$searchQuery'. Try searching for 'Pickles', 'Mysore Pak', or 'Gunpowder Podi'." else "No items match the selected filters.",
                            actionButtonText = "Clear Search & Filters",
                            onActionClick = {
                                viewModel.onQueryChange("")
                                viewModel.applyFilter(com.cravexa.domain.model.SearchFilter())
                            }
                        )
                    } else {
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
                                    onClick = {
                                        if (searchQuery.isNotBlank()) {
                                            viewModel.submitSearch(searchQuery)
                                        }
                                        onNavigateToProduct(product.id)
                                    },
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
