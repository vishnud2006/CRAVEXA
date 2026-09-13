package com.cravexa.domain.usecase

import com.cravexa.core.common.Resource
import com.cravexa.domain.model.Address
import com.cravexa.domain.repository.AddressRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAddressesUseCase @Inject constructor(
    private val addressRepository: AddressRepository
) {
    suspend operator fun invoke(): Resource<List<Address>> {
        return addressRepository.getAddresses()
    }

    fun observe(): Flow<List<Address>> {
        return addressRepository.addresses
    }
}

class AddAddressUseCase @Inject constructor(
    private val addressRepository: AddressRepository
) {
    suspend operator fun invoke(address: Address): Resource<Address> {
        return addressRepository.addAddress(address)
    }
}

class UpdateAddressUseCase @Inject constructor(
    private val addressRepository: AddressRepository
) {
    suspend operator fun invoke(address: Address): Resource<Address> {
        return addressRepository.updateAddress(address)
    }
}

class DeleteAddressUseCase @Inject constructor(
    private val addressRepository: AddressRepository
) {
    suspend operator fun invoke(addressId: String): Resource<Unit> {
        return addressRepository.deleteAddress(addressId)
    }
}

class SetDefaultAddressUseCase @Inject constructor(
    private val addressRepository: AddressRepository
) {
    suspend operator fun invoke(addressId: String): Resource<Unit> {
        return addressRepository.setDefaultAddress(addressId)
    }
}

