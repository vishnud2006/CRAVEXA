package com.cravexa.domain.repository

import com.cravexa.core.common.Resource
import com.cravexa.domain.model.Address
import kotlinx.coroutines.flow.Flow

interface AddressRepository {
    val addresses: Flow<List<Address>>

    suspend fun getAddresses(): Resource<List<Address>>

    suspend fun addAddress(address: Address): Resource<Address>

    suspend fun updateAddress(address: Address): Resource<Address>

    suspend fun deleteAddress(addressId: String): Resource<Unit>

    suspend fun setDefaultAddress(addressId: String): Resource<Unit>
}

