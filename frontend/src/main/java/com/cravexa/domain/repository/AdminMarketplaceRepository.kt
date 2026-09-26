package com.cravexa.domain.repository

import com.cravexa.core.common.Resource
import com.cravexa.domain.model.AdminBanner
import com.cravexa.domain.model.AdminCategory
import com.cravexa.domain.model.AdminCoupon
import kotlinx.coroutines.flow.Flow

interface AdminMarketplaceRepository {
    val categoriesFlow: Flow<List<AdminCategory>>
    val couponsFlow: Flow<List<AdminCoupon>>
    val bannersFlow: Flow<List<AdminBanner>>

    fun getCategories(): Flow<Resource<List<AdminCategory>>>
    suspend fun addCategory(category: AdminCategory): Resource<Unit>
    suspend fun toggleCategory(categoryId: String, enabled: Boolean): Resource<Unit>

    fun getCoupons(): Flow<Resource<List<AdminCoupon>>>
    suspend fun addCoupon(coupon: AdminCoupon): Resource<Unit>
    suspend fun toggleCoupon(couponId: String, active: Boolean): Resource<Unit>

    fun getBanners(): Flow<Resource<List<AdminBanner>>>
    suspend fun toggleBanner(bannerId: String, active: Boolean): Resource<Unit>
}
