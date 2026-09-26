package com.cravexa.presentation.admin.marketplace

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cravexa.core.common.Resource
import com.cravexa.domain.model.AdminBanner
import com.cravexa.domain.model.AdminCategory
import com.cravexa.domain.model.AdminCoupon
import com.cravexa.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface AdminMarketplaceUiState {
    data object Loading : AdminMarketplaceUiState
    data class Success(
        val categories: List<AdminCategory>,
        val coupons: List<AdminCoupon>,
        val banners: List<AdminBanner>,
        val selectedSubTab: Int // 0 Categories, 1 Coupons, 2 Banners
    ) : AdminMarketplaceUiState
    data class Error(val message: String) : AdminMarketplaceUiState
}

@HiltViewModel
class AdminMarketplaceViewModel @Inject constructor(
    private val getAdminCategoriesUseCase: GetAdminCategoriesUseCase,
    private val addAdminCategoryUseCase: AddAdminCategoryUseCase,
    private val toggleAdminCategoryUseCase: ToggleAdminCategoryUseCase,
    private val getAdminCouponsUseCase: GetAdminCouponsUseCase,
    private val addAdminCouponUseCase: AddAdminCouponUseCase,
    private val toggleAdminCouponUseCase: ToggleAdminCouponUseCase,
    private val getAdminBannersUseCase: GetAdminBannersUseCase,
    private val toggleAdminBannerUseCase: ToggleAdminBannerUseCase
) : ViewModel() {

    private val _selectedSubTab = MutableStateFlow(0)
    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    val uiState: StateFlow<AdminMarketplaceUiState> = combine(
        getAdminCategoriesUseCase.categoriesFlow,
        getAdminCouponsUseCase.couponsFlow,
        getAdminBannersUseCase.bannersFlow,
        _selectedSubTab
    ) { categories, coupons, banners, tab ->
        AdminMarketplaceUiState.Success(
            categories = categories,
            coupons = coupons,
            banners = banners,
            selectedSubTab = tab
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AdminMarketplaceUiState.Loading
    )

    init {
        getAdminCategoriesUseCase().launchIn(viewModelScope)
        getAdminCouponsUseCase().launchIn(viewModelScope)
        getAdminBannersUseCase().launchIn(viewModelScope)
    }

    fun onSubTabSelected(tab: Int) {
        _selectedSubTab.value = tab
    }

    fun addCategory(name: String, description: String) {
        viewModelScope.launch {
            val result = addAdminCategoryUseCase(
                AdminCategory(id = "", name = name, description = description, enabled = true)
            )
            if (result is Resource.Success) {
                _toastMessage.value = "Category \"$name\" added!"
            }
        }
    }

    fun toggleCategory(categoryId: String, enabled: Boolean) {
        viewModelScope.launch {
            toggleAdminCategoryUseCase(categoryId, enabled)
            _toastMessage.value = "Category status updated"
        }
    }

    fun addCoupon(code: String, discountPct: Int, maxDiscount: Double, minOrder: Double) {
        viewModelScope.launch {
            val result = addAdminCouponUseCase(
                AdminCoupon(
                    id = "",
                    code = code.uppercase(),
                    discountPercentage = discountPct,
                    maxDiscountAmount = maxDiscount,
                    minOrderAmount = minOrder,
                    startDate = "Today",
                    endDate = "31 Dec 2026",
                    usageLimit = 500,
                    active = true
                )
            )
            if (result is Resource.Success) {
                _toastMessage.value = "Coupon \"${code.uppercase()}\" created!"
            }
        }
    }

    fun toggleCoupon(couponId: String, active: Boolean) {
        viewModelScope.launch {
            toggleAdminCouponUseCase(couponId, active)
            _toastMessage.value = "Coupon status updated"
        }
    }

    fun toggleBanner(bannerId: String, active: Boolean) {
        viewModelScope.launch {
            toggleAdminBannerUseCase(bannerId, active)
            _toastMessage.value = "Banner visibility updated"
        }
    }

    fun clearToast() {
        _toastMessage.value = null
    }
}
