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
public final class GetSellerOrderDetailUseCase_Factory implements Factory<GetSellerOrderDetailUseCase> {
  private final Provider<SellerOrderRepository> orderRepositoryProvider;

  public GetSellerOrderDetailUseCase_Factory(
      Provider<SellerOrderRepository> orderRepositoryProvider) {
    this.orderRepositoryProvider = orderRepositoryProvider;
  }

  @Override
  public GetSellerOrderDetailUseCase get() {
    return newInstance(orderRepositoryProvider.get());
  }

  public static GetSellerOrderDetailUseCase_Factory create(
      Provider<SellerOrderRepository> orderRepositoryProvider) {
    return new GetSellerOrderDetailUseCase_Factory(orderRepositoryProvider);
  }

  public static GetSellerOrderDetailUseCase newInstance(SellerOrderRepository orderRepository) {
    return new GetSellerOrderDetailUseCase(orderRepository);
  }
}
