package com.cravexa.domain.usecase

import com.cravexa.core.common.Resource
import com.cravexa.domain.model.CustomerProfile
import com.cravexa.domain.repository.CustomerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCustomerProfileUseCase @Inject constructor(
    private val customerRepository: CustomerRepository
) {
    suspend operator fun invoke(): Resource<CustomerProfile> {
        return customerRepository.getCustomerProfile()
    }

    fun observe(): Flow<CustomerProfile?> {
        return customerRepository.currentCustomerProfile
    }
}

class UpdateCustomerProfileUseCase @Inject constructor(
    private val customerRepository: CustomerRepository
) {
    suspend operator fun invoke(profile: CustomerProfile): Resource<CustomerProfile> {
        return customerRepository.updateCustomerProfile(profile)
    }
}

