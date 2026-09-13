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
public final class GetOrdersUseCase_Factory implements Factory<GetOrdersUseCase> {
  private final Provider<OrderRepository> orderRepositoryProvider;

  public GetOrdersUseCase_Factory(Provider<OrderRepository> orderRepositoryProvider) {
    this.orderRepositoryProvider = orderRepositoryProvider;
  }

  @Override
  public GetOrdersUseCase get() {
    return newInstance(orderRepositoryProvider.get());
  }

  public static GetOrdersUseCase_Factory create(Provider<OrderRepository> orderRepositoryProvider) {
    return new GetOrdersUseCase_Factory(orderRepositoryProvider);
  }

  public static GetOrdersUseCase newInstance(OrderRepository orderRepository) {
    return new GetOrdersUseCase(orderRepository);
  }
}
