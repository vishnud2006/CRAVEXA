package com.cravexa.domain.repository

import com.cravexa.core.common.Resource
import com.cravexa.domain.model.SellerProfile
import kotlinx.coroutines.flow.Flow

interface SellerRepository {
    val currentSellerProfile: Flow<SellerProfile?>

    suspend fun getSellerProfile(): Resource<SellerProfile>

    suspend fun updateSellerProfile(profile: SellerProfile): Resource<SellerProfile>

    suspend fun submitFssai(fssaiNumber: String, documentUrl: String?): Resource<SellerProfile>
}

