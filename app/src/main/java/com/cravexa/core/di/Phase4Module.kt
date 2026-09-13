package com.cravexa.core.di

import com.cravexa.data.repository.CartRepositoryImpl
import com.cravexa.data.repository.CategoryRepositoryImpl
import com.cravexa.data.repository.ProductRepositoryImpl
import com.cravexa.data.repository.RecentSearchRepositoryImpl
import com.cravexa.data.repository.WishlistRepositoryImpl
import com.cravexa.domain.repository.CartRepository
import com.cravexa.domain.repository.CategoryRepository
import com.cravexa.domain.repository.ProductRepository
import com.cravexa.domain.repository.RecentSearchRepository
import com.cravexa.domain.repository.WishlistRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class Phase4Module {

    @Binds
    @Singleton
    abstract fun bindCategoryRepository(
        impl: CategoryRepositoryImpl
    ): CategoryRepository

    @Binds
    @Singleton
    abstract fun bindProductRepository(
        impl: ProductRepositoryImpl
    ): ProductRepository

    @Binds
    @Singleton
    abstract fun bindWishlistRepository(
        impl: WishlistRepositoryImpl
    ): WishlistRepository

    @Binds
    @Singleton
    abstract fun bindCartRepository(
        impl: CartRepositoryImpl
    ): CartRepository

    @Binds
    @Singleton
    abstract fun bindRecentSearchRepository(
        impl: RecentSearchRepositoryImpl
    ): RecentSearchRepository
}

