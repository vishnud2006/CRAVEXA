package com.cravexa.domain.usecase;

import com.cravexa.domain.repository.SellerOrderRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class UpdateSellerOrderStatusUseCase_Factory implements Factory<UpdateSellerOrderStatusUseCase> {
  private final Provider<SellerOrderRepository> orderRepositoryProvider;

  public UpdateSellerOrderStatusUseCase_Factory(
      Provider<SellerOrderRepository> orderRepositoryProvider) {
    this.orderRepositoryProvider = orderRepositoryProvider;
  }

  @Override
  public UpdateSellerOrderStatusUseCase get() {
    return newInstance(orderRepositoryProvider.get());
  }

  public static UpdateSellerOrderStatusUseCase_Factory create(
      Provider<SellerOrderRepository> orderRepositoryProvider) {
    return new UpdateSellerOrderStatusUseCase_Factory(orderRepositoryProvider);
  }

  public static UpdateSellerOrderStatusUseCase newInstance(SellerOrderRepository orderRepository) {
    return new UpdateSellerOrderStatusUseCase(orderRepository);
  }
}
