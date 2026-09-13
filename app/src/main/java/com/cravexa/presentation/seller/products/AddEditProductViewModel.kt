package com.cravexa.presentation.seller.products

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cravexa.core.common.Resource
import com.cravexa.domain.model.Product
import com.cravexa.domain.usecase.AddSellerProductUseCase
import com.cravexa.domain.usecase.GetCategoriesUseCase
import com.cravexa.domain.usecase.GetSellerProductByIdUseCase
import com.cravexa.domain.usecase.GetSellerProfileUseCase
import com.cravexa.domain.usecase.UpdateSellerProductUseCase
import com.cravexa.domain.usecase.UploadProductImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface AddEditProductUiState {
    data object Idle : AddEditProductUiState
    data object Loading : AddEditProductUiState
    data object Saving : AddEditProductUiState
    data class Success(val message: String) : AddEditProductUiState
    data class Error(val message: String) : AddEditProductUiState
}

@HiltViewModel
class AddEditProductViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getSellerProductByIdUseCase: GetSellerProductByIdUseCase,
    private val addSellerProductUseCase: AddSellerProductUseCase,
    private val updateSellerProductUseCase: UpdateSellerProductUseCase,
    private val getSellerProfileUseCase: GetSellerProfileUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val uploadProductImageUseCase: UploadProductImageUseCase
) : ViewModel() {

    val productId: String? = savedStateHandle.get<String>("productId")?.takeIf { it != "{productId}" && it.isNotBlank() }
    val isEditMode: Boolean = productId != null

    private val _uiState = MutableStateFlow<AddEditProductUiState>(AddEditProductUiState.Idle)
    val uiState: StateFlow<AddEditProductUiState> = _uiState.asStateFlow()

    var name = MutableStateFlow("")
        private set

    var description = MutableStateFlow("")
        private set

    var price = MutableStateFlow("")
        private set

    var originalPrice = MutableStateFlow("")
        private set

    var categoryId = MutableStateFlow("cat_pickles")
        private set

    var categoryName = MutableStateFlow("Handmade Pickles")
        private set

    var foodType = MutableStateFlow("Vegetarian")
        private set

    var weight = MutableStateFlow("500g")
        private set

    var ingredients = MutableStateFlow("")
        private set

    var shelfLife = MutableStateFlow("6 Months")
        private set

    var storageInstructions = MutableStateFlow("Store in a cool, dry place away from direct sunlight.")
        private set

    var region = MutableStateFlow("Andhra Pradesh")
        private set

    var stock = MutableStateFlow("15")
        private set

    var available = MutableStateFlow(true)
        private set

    var selectedImageUris = MutableStateFlow<List<Uri>>(emptyList())
        private set

    // Form errors
    val nameError = MutableStateFlow<String?>(null)
    val descriptionError = MutableStateFlow<String?>(null)
    val priceError = MutableStateFlow<String?>(null)
    val stockError = MutableStateFlow<String?>(null)

    val availableCategories = listOf(
        "cat_pickles" to "Handmade Pickles",
        "cat_sweets" to "Traditional Indian Sweets",
        "cat_spices" to "Artisanal Spices & Podis",
        "cat_snacks" to "Crunchy Snacks & Namkeens",
        "cat_bakery" to "Home Bakery Delights",
        "cat_chutneys" to "Heirloom Chutneys & Dips",
        "cat_papad" to "Sun-Dried Papads & Fryums",
        "cat_beverages" to "Native Beverages & Mixes",
        "cat_flours" to "Stone-Ground Flours & Grains",
        "cat_hampers" to "Gift Hampers & Combos"
    )

    val foodTypes = listOf("Vegetarian", "Vegan", "Jain", "Non-Vegetarian")

    init {
        if (isEditMode && productId != null) {
            loadProduct(productId)
        }
    }

    private fun loadProduct(id: String) {
        viewModelScope.launch {
            _uiState.value = AddEditProductUiState.Loading
            when (val result = getSellerProductByIdUseCase(id)) {
                is Resource.Success -> {
                    result.data?.let { product ->
                        name.value = product.name
                        description.value = product.description
                        price.value = product.price.toInt().toString()
                        originalPrice.value = product.originalPrice?.toInt()?.toString() ?: ""
                        categoryId.value = product.categoryId
                        categoryName.value = product.categoryName
                        foodType.value = product.foodType
                        weight.value = product.weight
                        ingredients.value = product.ingredients.joinToString(", ")
                        shelfLife.value = product.shelfLife
                        storageInstructions.value = product.storageInstructions
                        region.value = product.region
                        stock.value = product.stock.toString()
                        available.value = product.available
                    }
                    _uiState.value = AddEditProductUiState.Idle
                }
                is Resource.Error -> {
                    _uiState.value = AddEditProductUiState.Error(result.message ?: "Failed to load product details.")
                }
                else -> Unit
            }
        }
    }

    fun onNameChanged(v: String) {
        name.value = v
        if (nameError.value != null) nameError.value = null
    }

    fun onDescriptionChanged(v: String) {
        description.value = v
        if (descriptionError.value != null) descriptionError.value = null
    }

    fun onPriceChanged(v: String) {
        price.value = v.filter { it.isDigit() || it == '.' }
        if (priceError.value != null) priceError.value = null
    }

    fun onOriginalPriceChanged(v: String) {
        originalPrice.value = v.filter { it.isDigit() || it == '.' }
    }

    fun onCategorySelected(id: String, name: String) {
        categoryId.value = id
        categoryName.value = name
    }

    fun onFoodTypeSelected(type: String) {
        foodType.value = type
    }

    fun onWeightChanged(v: String) {
        weight.value = v
    }

    fun onIngredientsChanged(v: String) {
        ingredients.value = v
    }

    fun onShelfLifeChanged(v: String) {
        shelfLife.value = v
    }

    fun onStorageInstructionsChanged(v: String) {
        storageInstructions.value = v
    }

    fun onRegionChanged(v: String) {
        region.value = v
    }

    fun onStockChanged(v: String) {
        stock.value = v.filter { it.isDigit() }
        if (stockError.value != null) stockError.value = null
    }

    fun onAvailabilityChanged(v: Boolean) {
        available.value = v
    }

    fun onImagesPicked(uris: List<Uri>) {
        selectedImageUris.value = uris
    }

    fun saveProduct() {
        val nameVal = name.value.trim()
        val descVal = description.value.trim()
        val priceVal = price.value.toDoubleOrNull()
        val origPriceVal = originalPrice.value.toDoubleOrNull()
        val stockVal = stock.value.toIntOrNull()

        var hasError = false

        if (nameVal.isBlank()) {
            nameError.value = "Dish name is required."
            hasError = true
        }

        if (descVal.isBlank()) {
            descriptionError.value = "Dish description is required."
            hasError = true
        }

        if (priceVal == null || priceVal <= 0) {
            priceError.value = "Please enter a valid price greater than 0."
            hasError = true
        }

        if (stockVal == null || stockVal < 0) {
            stockError.value = "Stock cannot be negative."
            hasError = true
        }

        if (hasError) return

        viewModelScope.launch {
            _uiState.value = AddEditProductUiState.Saving

            val profile = getSellerProfileUseCase.observe().firstOrNull()
            val sellerId = profile?.id ?: "sel_1"
            val sellerName = profile?.businessName ?: "Lakshmi's Home Kitchen"
            val sellerLocation = "${profile?.city ?: "Guntur"}, ${profile?.state ?: "Andhra Pradesh"}"

            val parsedIngredients = ingredients.value
                .split(",")
                .map { it.trim() }
                .filter { it.isNotBlank() }

            val productToSave = Product(
                id = productId ?: "prod_${System.currentTimeMillis()}",
                name = nameVal,
                description = descVal,
                price = priceVal!!,
                originalPrice = origPriceVal,
                sellerId = sellerId,
                sellerName = sellerName,
                sellerLocation = sellerLocation,
                categoryId = categoryId.value,
                categoryName = categoryName.value,
                rating = 0.0,
                reviewCount = 0,
                stock = stockVal!!,
                weight = weight.value.trim().ifBlank { "500g" },
                ingredients = parsedIngredients,
                shelfLife = shelfLife.value.trim().ifBlank { "6 Months" },
                storageInstructions = storageInstructions.value.trim(),
                region = region.value.trim().ifBlank { "India" },
                foodType = foodType.value,
                available = available.value,
                featured = false,
                trending = false
            )

            val result = if (isEditMode) {
                updateSellerProductUseCase(productToSave)
            } else {
                addSellerProductUseCase(productToSave)
            }

            when (result) {
                is Resource.Success -> {
                    _uiState.value = AddEditProductUiState.Success(
                        if (isEditMode) "Dish updated successfully!" else "New dish added to your catalog!"
                    )
                }
                is Resource.Error -> {
                    _uiState.value = AddEditProductUiState.Error(result.message ?: "Failed to save dish.")
                }
                else -> Unit
            }
        }
    }

    fun resetError() {
        _uiState.value = AddEditProductUiState.Idle
    }
}

