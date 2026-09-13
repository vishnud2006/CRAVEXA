package com.cravexa.presentation.customer.orders;

import com.cravexa.domain.usecase.GetOrdersUseCase;
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
public final class OrdersViewModel_Factory implements Factory<OrdersViewModel> {
  private final Provider<GetOrdersUseCase> getOrdersUseCaseProvider;

  public OrdersViewModel_Factory(Provider<GetOrdersUseCase> getOrdersUseCaseProvider) {
    this.getOrdersUseCaseProvider = getOrdersUseCaseProvider;
  }

  @Override
  public OrdersViewModel get() {
    return newInstance(getOrdersUseCaseProvider.get());
  }

  public static OrdersViewModel_Factory create(
      Provider<GetOrdersUseCase> getOrdersUseCaseProvider) {
    return new OrdersViewModel_Factory(getOrdersUseCaseProvider);
  }

  public static OrdersViewModel newInstance(GetOrdersUseCase getOrdersUseCase) {
    return new OrdersViewModel(getOrdersUseCase);
  }
}
