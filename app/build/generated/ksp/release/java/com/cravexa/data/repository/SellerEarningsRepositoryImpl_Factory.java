package com.cravexa.data.repository;

import com.cravexa.domain.repository.SellerOrderRepository;
import com.cravexa.domain.repository.SellerProductRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class SellerEarningsRepositoryImpl_Factory implements Factory<SellerEarningsRepositoryImpl> {
  private final Provider<SellerProductRepository> productRepositoryProvider;

  private final Provider<SellerOrderRepository> orderRepositoryProvider;

  public SellerEarningsRepositoryImpl_Factory(
      Provider<SellerProductRepository> productRepositoryProvider,
      Provider<SellerOrderRepository> orderRepositoryProvider) {
    this.productRepositoryProvider = productRepositoryProvider;
    this.orderRepositoryProvider = orderRepositoryProvider;
  }

  @Override
  public SellerEarningsRepositoryImpl get() {
    return newInstance(productRepositoryProvider.get(), orderRepositoryProvider.get());
  }

  public static SellerEarningsRepositoryImpl_Factory create(
      Provider<SellerProductRepository> productRepositoryProvider,
      Provider<SellerOrderRepository> orderRepositoryProvider) {
    return new SellerEarningsRepositoryImpl_Factory(productRepositoryProvider, orderRepositoryProvider);
  }

  public static SellerEarningsRepositoryImpl newInstance(SellerProductRepository productRepository,
      SellerOrderRepository orderRepository) {
    return new SellerEarningsRepositoryImpl(productRepository, orderRepository);
  }
}
