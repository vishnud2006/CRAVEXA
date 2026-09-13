package com.cravexa.presentation.customer.orders;

import com.cravexa.domain.usecase.GetOrderDetailUseCase;
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
public final class OrderDetailViewModel_Factory implements Factory<OrderDetailViewModel> {
  private final Provider<GetOrderDetailUseCase> getOrderDetailUseCaseProvider;

  public OrderDetailViewModel_Factory(
      Provider<GetOrderDetailUseCase> getOrderDetailUseCaseProvider) {
    this.getOrderDetailUseCaseProvider = getOrderDetailUseCaseProvider;
  }

  @Override
  public OrderDetailViewModel get() {
    return newInstance(getOrderDetailUseCaseProvider.get());
  }

  public static OrderDetailViewModel_Factory create(
      Provider<GetOrderDetailUseCase> getOrderDetailUseCaseProvider) {
    return new OrderDetailViewModel_Factory(getOrderDetailUseCaseProvider);
  }

  public static OrderDetailViewModel newInstance(GetOrderDetailUseCase getOrderDetailUseCase) {
    return new OrderDetailViewModel(getOrderDetailUseCase);
  }
}
