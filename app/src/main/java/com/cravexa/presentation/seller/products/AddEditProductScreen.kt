package com.cravexa.presentation.seller.products

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cravexa.R
import com.cravexa.core.designsystem.components.CravexaButton
import com.cravexa.core.designsystem.components.CravexaButtonStyle
import com.cravexa.core.designsystem.components.CravexaCard
import com.cravexa.core.designsystem.components.CravexaChip
import com.cravexa.core.designsystem.components.CravexaLoadingIndicator
import com.cravexa.core.designsystem.components.CravexaTextField
import com.cravexa.core.designsystem.components.CravexaTopAppBar
import com.cravexa.core.designsystem.theme.CravexaOrange500
import com.cravexa.core.designsystem.theme.CravexaPurple800

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddEditProductScreen(
    onBack: () -> Unit,
    onProductSaved: () -> Unit,
    viewModel: AddEditProductViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val name by viewModel.name.collectAsState()
    val description by viewModel.description.collectAsState()
    val price by viewModel.price.collectAsState()
    val originalPrice by viewModel.originalPrice.collectAsState()
    val categoryId by viewModel.categoryId.collectAsState()
    val categoryName by viewModel.categoryName.collectAsState()
    val foodType by viewModel.foodType.collectAsState()
    val weight by viewModel.weight.collectAsState()
    val ingredients by viewModel.ingredients.collectAsState()
    val shelfLife by viewModel.shelfLife.collectAsState()
    val storageInstructions by viewModel.storageInstructions.collectAsState()
    val region by viewModel.region.collectAsState()
    val stock by viewModel.stock.collectAsState()
    val available by viewModel.available.collectAsState()
    val selectedImageUris by viewModel.selectedImageUris.collectAsState()

    val nameError by viewModel.nameError.collectAsState()
    val descriptionError by viewModel.descriptionError.collectAsState()
    val priceError by viewModel.priceError.collectAsState()
    val stockError by viewModel.stockError.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val isSaving = uiState is AddEditProductUiState.Saving
    var categoryDropdownExpanded by remember { mutableStateOf(false) }

    // Image Picker Launcher
    val multiplePhotoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = 4)
    ) { uris ->
        if (uris.isNotEmpty()) {
            viewModel.onImagesPicked(uris)
        }
    }

    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is AddEditProductUiState.Success -> {
                snackbarHostState.showSnackbar(state.message)
                onProductSaved()
            }
            is AddEditProductUiState.Error -> {
                snackbarHostState.showSnackbar(state.message)
                viewModel.resetError()
            }
            else -> Unit
        }
    }

    Scaffold(
        topBar = {
            CravexaTopAppBar(
                title = if (viewModel.isEditMode) "Edit Delicacy" else "Add New Delicacy",
                showBackButton = true,
                onBackClick = onBack
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color(0xFFF9F9FB)
    ) { innerPadding ->
        if (uiState is AddEditProductUiState.Loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CravexaLoadingIndicator(size = 44.dp)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                // Section 1: Basic Information
                Text(
                    text = "Dish Details",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E1E2E)
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                CravexaCard(modifier = Modifier.fillMaxWidth()) {
                    CravexaTextField(
                        value = name,
                        onValueChange = viewModel::onNameChanged,
                        label = "Dish / Product Name *",
                        placeholder = "e.g. Grandma's Avakaya Mango Pickle",
                        errorMessage = nameError,
                        enabled = !isSaving
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Category Dropdown
                    ExposedDropdownMenuBox(
                        expanded = categoryDropdownExpanded,
                        onExpandedChange = { if (!isSaving) categoryDropdownExpanded = it }
                    ) {
                        OutlinedTextField(
                            value = categoryName,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Category *") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryDropdownExpanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                        )

                        ExposedDropdownMenu(
                            expanded = categoryDropdownExpanded,
                            onDismissRequest = { categoryDropdownExpanded = false }
                        ) {
                            viewModel.availableCategories.forEach { (catId, catName) ->
                                DropdownMenuItem(
                                    text = { Text(catName) },
                                    onClick = {
                                        viewModel.onCategorySelected(catId, catName)
                                        categoryDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    CravexaTextField(
                        value = description,
                        onValueChange = viewModel::onDescriptionChanged,
                        label = "Description & Heritage Story *",
                        placeholder = "Describe your traditional family recipe, process, and taste profile...",
                        errorMessage = descriptionError,
                        enabled = !isSaving,
                        singleLine = false,
                        maxLines = 4
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    CravexaTextField(
                        value = region,
                        onValueChange = viewModel::onRegionChanged,
                        label = "Culinary Region of Origin",
                        placeholder = "e.g. Andhra Pradesh, Coorg, Rajasthan",
                        enabled = !isSaving
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Section 2: Pricing & Inventory
                Text(
                    text = "Pricing & Available Stock",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E1E2E)
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                CravexaCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        CravexaTextField(
                            value = price,
                            onValueChange = viewModel::onPriceChanged,
                            label = "Price (₹) *",
                            placeholder = "280",
                            errorMessage = priceError,
                            enabled = !isSaving,
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )

                        CravexaTextField(
                            value = originalPrice,
                            onValueChange = viewModel::onOriginalPriceChanged,
                            label = "MRP / Orig Price (₹)",
                            placeholder = "320",
                            enabled = !isSaving,
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        CravexaTextField(
                            value = stock,
                            onValueChange = viewModel::onStockChanged,
                            label = "Available Units (Stock) *",
                            placeholder = "25",
                            errorMessage = stockError,
                            enabled = !isSaving,
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )

                        CravexaTextField(
                            value = weight,
                            onValueChange = viewModel::onWeightChanged,
                            label = "Unit / Weight *",
                            placeholder = "500g Glass Jar",
                            enabled = !isSaving,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Availability Switch
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Marketplace Listing Status",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                            )
                            Text(
                                text = if (available) "Dish is visible to customers" else "Dish is hidden from marketplace",
                                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                            )
                        }

                        Switch(
                            checked = available,
                            onCheckedChange = viewModel::onAvailabilityChanged,
                            enabled = !isSaving,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = CravexaOrange500
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Section 3: Food Safety & Specifications
                Text(
                    text = "Food Specifications & Shelf Life",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E1E2E)
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                CravexaCard(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Food Diet Category",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, color = Color.Gray)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        viewModel.foodTypes.forEach { type ->
                            CravexaChip(
                                text = type,
                                selected = foodType == type,
                                onClick = { viewModel.onFoodTypeSelected(type) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    CravexaTextField(
                        value = ingredients,
                        onValueChange = viewModel::onIngredientsChanged,
                        label = "Key Ingredients (comma-separated)",
                        placeholder = "Raw Mangoes, Guntur Chilli, Mustard, Cold-Pressed Oil, Rock Salt",
                        enabled = !isSaving,
                        singleLine = false,
                        maxLines = 3
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    CravexaTextField(
                        value = shelfLife,
                        onValueChange = viewModel::onShelfLifeChanged,
                        label = "Shelf Life",
                        placeholder = "e.g. 12 Months",
                        enabled = !isSaving
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    CravexaTextField(
                        value = storageInstructions,
                        onValueChange = viewModel::onStorageInstructionsChanged,
                        label = "Storage Instructions",
                        placeholder = "e.g. Store in a cool, dry place. Use only dry spoons.",
                        enabled = !isSaving,
                        singleLine = false,
                        maxLines = 2
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Section 4: Product Photos
                Text(
                    text = "Dish Photos",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E1E2E)
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                CravexaCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Upload Handmade Dish Photos",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                            )
                            Text(
                                text = if (selectedImageUris.isEmpty()) "Select up to 4 high-quality photos" else "${selectedImageUris.size} photos selected",
                                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                            )
                        }

                        CravexaButton(
                            text = if (selectedImageUris.isEmpty()) "Pick Photos" else "Change Photos",
                            onClick = {
                                multiplePhotoPicker.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                            style = CravexaButtonStyle.SECONDARY,
                            modifier = Modifier.width(140.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Submit Button
                CravexaButton(
                    text = if (viewModel.isEditMode) "Save Changes" else "Publish Dish to Kitchen",
                    onClick = viewModel::saveProduct,
                    isLoading = isSaving,
                    enabled = !isSaving,
                    style = CravexaButtonStyle.PRIMARY
                )

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
