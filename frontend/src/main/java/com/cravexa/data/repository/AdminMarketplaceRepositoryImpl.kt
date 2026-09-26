package com.cravexa.data.repository

import com.cravexa.core.common.Resource
import com.cravexa.domain.model.AdminBanner
import com.cravexa.domain.model.AdminCategory
import com.cravexa.domain.model.AdminCoupon
import com.cravexa.domain.repository.AdminMarketplaceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdminMarketplaceRepositoryImpl @Inject constructor() : AdminMarketplaceRepository {

    private val initialCategories = mutableListOf(
        AdminCategory("cat_1", "Pickles", "Authentic sun-cured regional pickles and avakayas", productCount = 14, enabled = true),
        AdminCategory("cat_2", "Spices", "Hand-ground whole spices, masalas, and podis", productCount = 8, enabled = true),
        AdminCategory("cat_3", "Snacks", "Homemade roasted khakhras, namkeens, and mixtures", productCount = 19, enabled = true),
        AdminCategory("cat_4", "Sweets", "Traditional desi ghee mithais, laddoos, and pedas", productCount = 12, enabled = true),
        AdminCategory("cat_5", "Regional Foods", "Authentic geographical delicacies across India", productCount = 9, enabled = true),
        AdminCategory("cat_6", "Traditional Foods", "Century-old ancestral recipes and batters", productCount = 7, enabled = true),
        AdminCategory("cat_7", "Baked Goods", "Home-baked breads, rusks, and tea cakes", productCount = 5, enabled = true),
        AdminCategory("cat_8", "Healthy Homemade Foods", "Millets, gluten-free, and organic pantry", productCount = 11, enabled = true),
        AdminCategory("cat_9", "Festival Specials", "Festive combos for Diwali, Rakhi, and Pongal", productCount = 6, enabled = true),
        AdminCategory("cat_10", "Gift Hampers", "Artisanal handcrafted gift boxes", productCount = 4, enabled = true)
    )

    private val initialCoupons = mutableListOf(
        AdminCoupon("c_1", "CRAVEFIRST", 20, maxDiscountAmount = 100.0, minOrderAmount = 299.0, startDate = "01 Aug 2026", endDate = "31 Dec 2026", usageLimit = 1000, usedCount = 142, active = true),
        AdminCoupon("c_2", "HOMEMADE50", 15, maxDiscountAmount = 150.0, minOrderAmount = 499.0, startDate = "15 Aug 2026", endDate = "30 Sep 2026", usageLimit = 500, usedCount = 68, active = true),
        AdminCoupon("c_3", "FESTIVE100", 25, maxDiscountAmount = 200.0, minOrderAmount = 799.0, startDate = "20 Aug 2026", endDate = "15 Nov 2026", usageLimit = 200, usedCount = 35, active = true)
    )

    private val initialBanners = mutableListOf(
        AdminBanner("b_1", "Authentic Homemade Treasures", "Handmade by verified home food creators across India", actionRoute = "category/Pickles", active = true, displayOrder = 1),
        AdminBanner("b_2", "Festive Delicacies & Hampers", "Pure desi ghee sweets delivered straight from kitchen", actionRoute = "category/Sweets", active = true, displayOrder = 2),
        AdminBanner("b_3", "100% Verified FSSAI Kitchens", "Safe, hygienic, and lab-tested homemade delicacies", actionRoute = "fssai_info", active = true, displayOrder = 3)
    )

    private val _categoriesFlow = MutableStateFlow<List<AdminCategory>>(initialCategories)
    private val _couponsFlow = MutableStateFlow<List<AdminCoupon>>(initialCoupons)
    private val _bannersFlow = MutableStateFlow<List<AdminBanner>>(initialBanners)

    override val categoriesFlow: Flow<List<AdminCategory>> = _categoriesFlow.asStateFlow()
    override val couponsFlow: Flow<List<AdminCoupon>> = _couponsFlow.asStateFlow()
    override val bannersFlow: Flow<List<AdminBanner>> = _bannersFlow.asStateFlow()

    override fun getCategories(): Flow<Resource<List<AdminCategory>>> = flow {
        emit(Resource.Loading())
        emit(Resource.Success(_categoriesFlow.value.toList()))
    }

    override suspend fun addCategory(category: AdminCategory): Resource<Unit> {
        val list = _categoriesFlow.value.toMutableList()
        val newCategory = if (category.id.isBlank()) category.copy(id = "cat_${UUID.randomUUID().toString().take(6)}") else category
        list.add(newCategory)
        _categoriesFlow.value = list
        return Resource.Success(Unit)
    }

    override suspend fun toggleCategory(categoryId: String, enabled: Boolean): Resource<Unit> {
        val list = _categoriesFlow.value.toMutableList()
        val index = list.indexOfFirst { it.id == categoryId }
        if (index != -1) {
            list[index] = list[index].copy(enabled = enabled)
            _categoriesFlow.value = list
            return Resource.Success(Unit)
        }
        return Resource.Error("Category not found.")
    }

    override fun getCoupons(): Flow<Resource<List<AdminCoupon>>> = flow {
        emit(Resource.Loading())
        emit(Resource.Success(_couponsFlow.value.toList()))
    }

    override suspend fun addCoupon(coupon: AdminCoupon): Resource<Unit> {
        val list = _couponsFlow.value.toMutableList()
        val newCoupon = if (coupon.id.isBlank()) coupon.copy(id = "c_${UUID.randomUUID().toString().take(6)}") else coupon
        list.add(newCoupon)
        _couponsFlow.value = list
        return Resource.Success(Unit)
    }

    override suspend fun toggleCoupon(couponId: String, active: Boolean): Resource<Unit> {
        val list = _couponsFlow.value.toMutableList()
        val index = list.indexOfFirst { it.id == couponId }
        if (index != -1) {
            list[index] = list[index].copy(active = active)
            _couponsFlow.value = list
            return Resource.Success(Unit)
        }
        return Resource.Error("Coupon not found.")
    }

    override fun getBanners(): Flow<Resource<List<AdminBanner>>> = flow {
        emit(Resource.Loading())
        emit(Resource.Success(_bannersFlow.value.toList()))
    }

    override suspend fun toggleBanner(bannerId: String, active: Boolean): Resource<Unit> {
        val list = _bannersFlow.value.toMutableList()
        val index = list.indexOfFirst { it.id == bannerId }
        if (index != -1) {
            list[index] = list[index].copy(active = active)
            _bannersFlow.value = list
            return Resource.Success(Unit)
        }
        return Resource.Error("Banner not found.")
    }
}
