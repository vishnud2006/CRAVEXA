package com.cravexa.core.di

import com.cravexa.data.repository.AddressRepositoryImpl
import com.cravexa.data.repository.CustomerRepositoryImpl
import com.cravexa.data.repository.OrderRepositoryImpl
import com.cravexa.data.repository.SellerRepositoryImpl
import com.cravexa.domain.repository.AddressRepository
import com.cravexa.domain.repository.CustomerRepository
import com.cravexa.domain.repository.OrderRepository
import com.cravexa.domain.repository.SellerRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class Phase3Module {

    @Binds
    @Singleton
    abstract fun bindCustomerRepository(
        customerRepositoryImpl: CustomerRepositoryImpl
    ): CustomerRepository

    @Binds
    @Singleton
    abstract fun bindAddressRepository(
        addressRepositoryImpl: AddressRepositoryImpl
    ): AddressRepository

    @Binds
    @Singleton
    abstract fun bindOrderRepository(
        orderRepositoryImpl: OrderRepositoryImpl
    ): OrderRepository

    @Binds
    @Singleton
    abstract fun bindSellerRepository(
        sellerRepositoryImpl: SellerRepositoryImpl
    ): SellerRepository
}

