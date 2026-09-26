package com.cravexa.domain.repository

import com.cravexa.core.common.Resource
import com.cravexa.domain.model.CustomerProfile
import kotlinx.coroutines.flow.Flow

interface CustomerRepository {
    val currentCustomerProfile: Flow<CustomerProfile?>

    suspend fun getCustomerProfile(): Resource<CustomerProfile>

    suspend fun updateCustomerProfile(profile: CustomerProfile): Resource<CustomerProfile>
}

