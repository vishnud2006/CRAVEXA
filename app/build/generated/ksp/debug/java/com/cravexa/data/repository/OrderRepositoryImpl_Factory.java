package com.cravexa.data.repository;

import com.cravexa.data.local.PreferenceManager;
import com.cravexa.domain.repository.AdminOrderManagementRepository;
import com.cravexa.domain.repository.SellerOrderRepository;
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
public final class OrderRepositoryImpl_Factory implements Factory<OrderRepositoryImpl> {
  private final Provider<PreferenceManager> preferenceManagerProvider;

  private final Provider<SellerOrderRepository> sellerOrderRepositoryProvider;

  private final Provider<AdminOrderManagementRepository> adminOrderManagementRepositoryProvider;

  public OrderRepositoryImpl_Factory(Provider<PreferenceManager> preferenceManagerProvider,
      Provider<SellerOrderRepository> sellerOrderRepositoryProvider,
      Provider<AdminOrderManagementRepository> adminOrderManagementRepositoryProvider) {
    this.preferenceManagerProvider = preferenceManagerProvider;
    this.sellerOrderRepositoryProvider = sellerOrderRepositoryProvider;
    this.adminOrderManagementRepositoryProvider = adminOrderManagementRepositoryProvider;
  }

  @Override
  public OrderRepositoryImpl get() {
    return newInstance(preferenceManagerProvider.get(), sellerOrderRepositoryProvider.get(), adminOrderManagementRepositoryProvider.get());
  }

  public static OrderRepositoryImpl_Factory create(
      Provider<PreferenceManager> preferenceManagerProvider,
      Provider<SellerOrderRepository> sellerOrderRepositoryProvider,
      Provider<AdminOrderManagementRepository> adminOrderManagementRepositoryProvider) {
    return new OrderRepositoryImpl_Factory(preferenceManagerProvider, sellerOrderRepositoryProvider, adminOrderManagementRepositoryProvider);
  }

  public static OrderRepositoryImpl newInstance(PreferenceManager preferenceManager,
      SellerOrderRepository sellerOrderRepository,
      AdminOrderManagementRepository adminOrderManagementRepository) {
    return new OrderRepositoryImpl(preferenceManager, sellerOrderRepository, adminOrderManagementRepository);
  }
}
