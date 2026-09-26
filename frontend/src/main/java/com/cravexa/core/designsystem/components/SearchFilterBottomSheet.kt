package com.cravexa.core.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cravexa.core.designsystem.theme.CravexaOrange500
import com.cravexa.core.designsystem.theme.CravexaPurple800
import com.cravexa.domain.model.Category
import com.cravexa.domain.model.SearchFilter

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SearchFilterBottomSheet(
    sheetState: SheetState,
    initialFilter: SearchFilter,
    categories: List<Category>,
    onDismiss: () -> Unit,
    onApply: (SearchFilter) -> Unit
) {
    var selectedCategoryId by remember { mutableStateOf(initialFilter.categoryId) }
    var selectedPriceRange by remember {
        mutableStateOf(
            when {
                initialFilter.maxPrice == 250.0 -> 1
                initialFilter.minPrice == 250.0 && initialFilter.maxPrice == 500.0 -> 2
                initialFilter.minPrice == 500.0 -> 3
                else -> 0
            }
        )
    }
    var selectedRating by remember { mutableStateOf(initialFilter.minRating) }
    var selectedRegion by remember { mutableStateOf(initialFilter.region) }
    var inStockOnly by remember { mutableStateOf(initialFilter.inStockOnly) }

    val regions = listOf("Andhra Pradesh", "Kerala", "Tamil Nadu", "Karnataka", "Rajasthan", "Gujarat", "West Bengal", "Uttar Pradesh")

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Filter Delicacies",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )
                TextButton(
                    onClick = {
                        selectedCategoryId = null
                        selectedPriceRange = 0
                        selectedRating = null
                        selectedRegion = null
                        inStockOnly = false
                    }
                ) {
                    Text(
                        text = "Reset All",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Category Filter
            Text(
                text = "Category",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { category ->
                    CravexaChip(
                        text = category.name,
                        selected = selectedCategoryId == category.id,
                        onClick = {
                            selectedCategoryId = if (selectedCategoryId == category.id) null else category.id
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Price Range
            Text(
                text = "Price Range",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CravexaChip(
                    text = "Under ₹250",
                    selected = selectedPriceRange == 1,
                    onClick = { selectedPriceRange = if (selectedPriceRange == 1) 0 else 1 }
                )
                CravexaChip(
                    text = "₹250 - ₹500",
                    selected = selectedPriceRange == 2,
                    onClick = { selectedPriceRange = if (selectedPriceRange == 2) 0 else 2 }
                )
                CravexaChip(
                    text = "Above ₹500",
                    selected = selectedPriceRange == 3,
                    onClick = { selectedPriceRange = if (selectedPriceRange == 3) 0 else 3 }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Minimum Rating
            Text(
                text = "Rating",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(4.5, 4.0, 3.5).forEach { rating ->
                    CravexaChip(
                        text = "$rating★ & above",
                        selected = selectedRating == rating,
                        onClick = {
                            selectedRating = if (selectedRating == rating) null else rating
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Regional Heritage Cuisines
            Text(
                text = "Regional Heritage",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                regions.forEach { region ->
                    CravexaChip(
                        text = region,
                        selected = selectedRegion == region,
                        onClick = {
                            selectedRegion = if (selectedRegion == region) null else region
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // In Stock Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "In Stock Only",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Show only items ready for immediate dispatch",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = inStockOnly,
                    onCheckedChange = { inStockOnly = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = CravexaOrange500,
                        checkedTrackColor = CravexaPurple800
                    )
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Action Button
            CravexaButton(
                text = "Apply Filters",
                onClick = {
                    val (minP, maxP) = when (selectedPriceRange) {
                        1 -> Pair(null, 250.0)
                        2 -> Pair(250.0, 500.0)
                        3 -> Pair(500.0, null)
                        else -> Pair(null, null)
                    }
                    val filter = SearchFilter(
                        categoryId = selectedCategoryId,
                        minPrice = minP,
                        maxPrice = maxP,
                        minRating = selectedRating,
                        region = selectedRegion,
                        inStockOnly = inStockOnly
                    )
                    onApply(filter)
                },
                style = CravexaButtonStyle.PRIMARY,
                fullWidth = true
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

