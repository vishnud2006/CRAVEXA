package com.cravexa.presentation.seller.dashboard;

import com.cravexa.domain.usecase.GetSellerDashboardUseCase;
import com.cravexa.domain.usecase.GetSellerOrdersUseCase;
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
public final class SellerDashboardViewModel_Factory implements Factory<SellerDashboardViewModel> {
  private final Provider<GetSellerDashboardUseCase> getDashboardUseCaseProvider;

  private final Provider<GetSellerOrdersUseCase> getOrdersUseCaseProvider;

  public SellerDashboardViewModel_Factory(
      Provider<GetSellerDashboardUseCase> getDashboardUseCaseProvider,
      Provider<GetSellerOrdersUseCase> getOrdersUseCaseProvider) {
    this.getDashboardUseCaseProvider = getDashboardUseCaseProvider;
    this.getOrdersUseCaseProvider = getOrdersUseCaseProvider;
  }

  @Override
  public SellerDashboardViewModel get() {
    return newInstance(getDashboardUseCaseProvider.get(), getOrdersUseCaseProvider.get());
  }

  public static SellerDashboardViewModel_Factory create(
      Provider<GetSellerDashboardUseCase> getDashboardUseCaseProvider,
      Provider<GetSellerOrdersUseCase> getOrdersUseCaseProvider) {
    return new SellerDashboardViewModel_Factory(getDashboardUseCaseProvider, getOrdersUseCaseProvider);
  }

  public static SellerDashboardViewModel newInstance(GetSellerDashboardUseCase getDashboardUseCase,
      GetSellerOrdersUseCase getOrdersUseCase) {
    return new SellerDashboardViewModel(getDashboardUseCase, getOrdersUseCase);
  }
}
