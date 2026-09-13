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
public final class GetSellerOrdersUseCase_Factory implements Factory<GetSellerOrdersUseCase> {
  private final Provider<SellerOrderRepository> orderRepositoryProvider;

  public GetSellerOrdersUseCase_Factory(Provider<SellerOrderRepository> orderRepositoryProvider) {
    this.orderRepositoryProvider = orderRepositoryProvider;
  }

  @Override
  public GetSellerOrdersUseCase get() {
    return newInstance(orderRepositoryProvider.get());
  }

  public static GetSellerOrdersUseCase_Factory create(
      Provider<SellerOrderRepository> orderRepositoryProvider) {
    return new GetSellerOrdersUseCase_Factory(orderRepositoryProvider);
  }

  public static GetSellerOrdersUseCase newInstance(SellerOrderRepository orderRepository) {
    return new GetSellerOrdersUseCase(orderRepository);
  }
}
