package com.cravexa.data.repository

import com.cravexa.core.common.Resource
import com.cravexa.data.local.PreferenceManager
import com.cravexa.domain.model.Address
import com.cravexa.domain.repository.AddressRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddressRepositoryImpl @Inject constructor(
    private val preferenceManager: PreferenceManager
) : AddressRepository {

    override val addresses: Flow<List<Address>> = preferenceManager.addresses

    override suspend fun getAddresses(): Resource<List<Address>> {
        val list = preferenceManager.addresses.firstOrNull() ?: emptyList()
        return Resource.Success(list)
    }

    override suspend fun addAddress(address: Address): Resource<Address> {
        return try {
            val currentList = preferenceManager.addresses.firstOrNull() ?: emptyList()
            val newId = if (address.id.isNotBlank()) address.id else "addr_${UUID.randomUUID().toString().take(8)}"
            val isFirst = currentList.isEmpty()
            val makeDefault = address.isDefault || isFirst

            val newAddress = address.copy(
                id = newId,
                isDefault = makeDefault,
                createdAt = System.currentTimeMillis()
            )

            val updatedList = if (makeDefault) {
                currentList.map { it.copy(isDefault = false) } + newAddress
            } else {
                currentList + newAddress
            }

            preferenceManager.saveAddresses(updatedList)
            Resource.Success(newAddress)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to add address.")
        }
    }

    override suspend fun updateAddress(address: Address): Resource<Address> {
        return try {
            val currentList = preferenceManager.addresses.firstOrNull() ?: emptyList()
            val updatedList = currentList.map { existing ->
                if (existing.id == address.id) {
                    address
                } else if (address.isDefault) {
                    existing.copy(isDefault = false)
                } else {
                    existing
                }
            }
            preferenceManager.saveAddresses(updatedList)
            Resource.Success(address)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to update address.")
        }
    }

    override suspend fun deleteAddress(addressId: String): Resource<Unit> {
        return try {
            val currentList = preferenceManager.addresses.firstOrNull() ?: emptyList()
            val updatedList = currentList.filterNot { it.id == addressId }
            preferenceManager.saveAddresses(updatedList)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to delete address.")
        }
    }

    override suspend fun setDefaultAddress(addressId: String): Resource<Unit> {
        return try {
            val currentList = preferenceManager.addresses.firstOrNull() ?: emptyList()
            val updatedList = currentList.map {
                it.copy(isDefault = it.id == addressId)
            }
            preferenceManager.saveAddresses(updatedList)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to set default address.")
        }
    }
}

