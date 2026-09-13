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
public final class GetOrderDetailUseCase_Factory implements Factory<GetOrderDetailUseCase> {
  private final Provider<OrderRepository> orderRepositoryProvider;

  public GetOrderDetailUseCase_Factory(Provider<OrderRepository> orderRepositoryProvider) {
    this.orderRepositoryProvider = orderRepositoryProvider;
  }

  @Override
  public GetOrderDetailUseCase get() {
    return newInstance(orderRepositoryProvider.get());
  }

  public static GetOrderDetailUseCase_Factory create(
      Provider<OrderRepository> orderRepositoryProvider) {
    return new GetOrderDetailUseCase_Factory(orderRepositoryProvider);
  }

  public static GetOrderDetailUseCase newInstance(OrderRepository orderRepository) {
    return new GetOrderDetailUseCase(orderRepository);
  }
}
