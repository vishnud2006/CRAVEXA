package com.cravexa.core.di

import com.cravexa.data.repository.ImageUploadRepositoryImpl
import com.cravexa.data.repository.SellerEarningsRepositoryImpl
import com.cravexa.data.repository.SellerOrderRepositoryImpl
import com.cravexa.data.repository.SellerProductRepositoryImpl
import com.cravexa.data.repository.SellerReviewRepositoryImpl
import com.cravexa.domain.repository.ImageUploadRepository
import com.cravexa.domain.repository.SellerEarningsRepository
import com.cravexa.domain.repository.SellerOrderRepository
import com.cravexa.domain.repository.SellerProductRepository
import com.cravexa.domain.repository.SellerReviewRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class Phase5Module {

    @Binds
    @Singleton
    abstract fun bindSellerProductRepository(
        impl: SellerProductRepositoryImpl
    ): SellerProductRepository

    @Binds
    @Singleton
    abstract fun bindSellerOrderRepository(
        impl: SellerOrderRepositoryImpl
    ): SellerOrderRepository

    @Binds
    @Singleton
    abstract fun bindSellerEarningsRepository(
        impl: SellerEarningsRepositoryImpl
    ): SellerEarningsRepository

    @Binds
    @Singleton
    abstract fun bindSellerReviewRepository(
        impl: SellerReviewRepositoryImpl
    ): SellerReviewRepository

    @Binds
    @Singleton
    abstract fun bindImageUploadRepository(
        impl: ImageUploadRepositoryImpl
    ): ImageUploadRepository
}

