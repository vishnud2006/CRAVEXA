package com.cravexa.core.di

import com.cravexa.data.repository.*
import com.cravexa.domain.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class Phase6Module {

    @Binds
    @Singleton
    abstract fun bindAdminDashboardRepository(
        impl: AdminDashboardRepositoryImpl
    ): AdminDashboardRepository

    @Binds
    @Singleton
    abstract fun bindAdminUserRepository(
        impl: AdminUserRepositoryImpl
    ): AdminUserRepository

    @Binds
    @Singleton
    abstract fun bindAdminSellerRepository(
        impl: AdminSellerRepositoryImpl
    ): AdminSellerRepository

    @Binds
    @Singleton
    abstract fun bindAdminProductModerationRepository(
        impl: AdminProductModerationRepositoryImpl
    ): AdminProductModerationRepository

    @Binds
    @Singleton
    abstract fun bindAdminOrderManagementRepository(
        impl: AdminOrderManagementRepositoryImpl
    ): AdminOrderManagementRepository

    @Binds
    @Singleton
    abstract fun bindAdminPaymentRefundRepository(
        impl: AdminPaymentRefundRepositoryImpl
    ): AdminPaymentRefundRepository

    @Binds
    @Singleton
    abstract fun bindAdminMarketplaceRepository(
        impl: AdminMarketplaceRepositoryImpl
    ): AdminMarketplaceRepository

    @Binds
    @Singleton
    abstract fun bindAdminComplaintRepository(
        impl: AdminComplaintRepositoryImpl
    ): AdminComplaintRepository

    @Binds
    @Singleton
    abstract fun bindAdminSettingsRepository(
        impl: AdminSettingsRepositoryImpl
    ): AdminSettingsRepository
}
