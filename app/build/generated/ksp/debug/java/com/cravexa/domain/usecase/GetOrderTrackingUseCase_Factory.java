package com.cravexa.domain.usecase;

import com.cravexa.domain.repository.OrderRepository;
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
public final class GetOrderTrackingUseCase_Factory implements Factory<GetOrderTrackingUseCase> {
  private final Provider<OrderRepository> orderRepositoryProvider;

  public GetOrderTrackingUseCase_Factory(Provider<OrderRepository> orderRepositoryProvider) {
    this.orderRepositoryProvider = orderRepositoryProvider;
  }

  @Override
  public GetOrderTrackingUseCase get() {
    return newInstance(orderRepositoryProvider.get());
  }

  public static GetOrderTrackingUseCase_Factory create(
      Provider<OrderRepository> orderRepositoryProvider) {
    return new GetOrderTrackingUseCase_Factory(orderRepositoryProvider);
  }

  public static GetOrderTrackingUseCase newInstance(OrderRepository orderRepository) {
    return new GetOrderTrackingUseCase(orderRepository);
  }
}
