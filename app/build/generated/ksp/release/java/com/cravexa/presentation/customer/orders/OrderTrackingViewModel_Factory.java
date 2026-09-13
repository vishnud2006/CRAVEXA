package com.cravexa.presentation.customer.orders;

import com.cravexa.domain.usecase.GetOrderTrackingUseCase;
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
public final class OrderTrackingViewModel_Factory implements Factory<OrderTrackingViewModel> {
  private final Provider<GetOrderTrackingUseCase> getOrderTrackingUseCaseProvider;

  public OrderTrackingViewModel_Factory(
      Provider<GetOrderTrackingUseCase> getOrderTrackingUseCaseProvider) {
    this.getOrderTrackingUseCaseProvider = getOrderTrackingUseCaseProvider;
  }

  @Override
  public OrderTrackingViewModel get() {
    return newInstance(getOrderTrackingUseCaseProvider.get());
  }

  public static OrderTrackingViewModel_Factory create(
      Provider<GetOrderTrackingUseCase> getOrderTrackingUseCaseProvider) {
    return new OrderTrackingViewModel_Factory(getOrderTrackingUseCaseProvider);
  }

  public static OrderTrackingViewModel newInstance(
      GetOrderTrackingUseCase getOrderTrackingUseCase) {
    return new OrderTrackingViewModel(getOrderTrackingUseCase);
  }
}
