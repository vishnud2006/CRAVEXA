package com.cravexa.presentation.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cravexa.core.common.Resource
import com.cravexa.domain.model.Category
import com.cravexa.domain.model.Product
import com.cravexa.domain.model.SearchFilter
import com.cravexa.domain.model.SortOption
import com.cravexa.domain.usecase.AddToCartUseCase
import com.cravexa.domain.usecase.GetCategoriesUseCase
import com.cravexa.domain.usecase.GetWishlistUseCase
import com.cravexa.domain.usecase.RecentSearchUseCases
import com.cravexa.domain.usecase.SearchProductsUseCase
import com.cravexa.domain.usecase.ToggleWishlistUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val searchProductsUseCase: SearchProductsUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val recentSearchUseCases: RecentSearchUseCases,
    private val getWishlistUseCase: GetWishlistUseCase,
    private val toggleWishlistUseCase: ToggleWishlistUseCase,
    private val addToCartUseCase: AddToCartUseCase
) : ViewModel() {

    private val initialQuery: String = savedStateHandle["query"] ?: ""

    private val _searchQuery = MutableStateFlow(initialQuery)
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _filter = MutableStateFlow(SearchFilter())
    val filter: StateFlow<SearchFilter> = _filter.asStateFlow()

    private val _sort = MutableStateFlow(SortOption.RELEVANCE)
    val sort: StateFlow<SortOption> = _sort.asStateFlow()

    val recentSearches: StateFlow<List<String>> = recentSearchUseCases.getRecentSearches()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val categories: StateFlow<List<Category>> = getCategoriesUseCase()
        .map { (it as? Resource.Success)?.data ?: emptyList() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val suggestions: StateFlow<List<String>> = _searchQuery
        .debounce(200)
        .flatMapLatest { q -> searchProductsUseCase.getSuggestions(q) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val searchResults: StateFlow<Resource<List<Product>>> = combine(
        _searchQuery.debounce(300).distinctUntilChanged(),
        _filter,
        _sort
    ) { query, currentFilter, currentSort ->
        Triple(query, currentFilter, currentSort)
    }.flatMapLatest { (query, currentFilter, currentSort) ->
        searchProductsUseCase(query, currentFilter, currentSort)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = Resource.Loading()
    )

    val wishlistIds: StateFlow<Set<String>> = getWishlistUseCase()
        .map { list -> list.map { it.id }.toSet() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptySet()
        )

    private val _userMessage = MutableSharedFlow<String>()
    val userMessage: SharedFlow<String> = _userMessage.asSharedFlow()

    fun onQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun submitSearch(query: String) {
        _searchQuery.value = query
        viewModelScope.launch {
            recentSearchUseCases.addRecentSearch(query)
        }
    }

    fun removeRecentSearch(query: String) {
        viewModelScope.launch {
            recentSearchUseCases.removeRecentSearch(query)
        }
    }

    fun clearRecentSearches() {
        viewModelScope.launch {
            recentSearchUseCases.clearRecentSearches()
        }
    }

    fun applyFilter(newFilter: SearchFilter) {
        _filter.value = newFilter
    }

    fun setSort(newSort: SortOption) {
        _sort.value = newSort
    }

    fun toggleWishlist(product: Product) {
        viewModelScope.launch {
            val result = toggleWishlistUseCase(product)
            if (result is Resource.Success) {
                val added = result.data == true
                _userMessage.emit(if (added) "Saved to your Wishlist ❤️" else "Removed from Wishlist")
            }
        }
    }

    fun addToCart(product: Product) {
        viewModelScope.launch {
            val result = addToCartUseCase(product, 1)
            if (result is Resource.Success) {
                _userMessage.emit("Added ${product.name} to Cart")
            }
        }
    }
}

