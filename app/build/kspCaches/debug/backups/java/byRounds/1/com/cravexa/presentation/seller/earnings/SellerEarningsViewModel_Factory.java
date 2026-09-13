package com.cravexa.presentation.seller.earnings;

import com.cravexa.domain.usecase.GetSellerEarningsUseCase;
import com.cravexa.domain.usecase.GetSellerPayoutsUseCase;
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
public final class SellerEarningsViewModel_Factory implements Factory<SellerEarningsViewModel> {
  private final Provider<GetSellerEarningsUseCase> getSellerEarningsUseCaseProvider;

  private final Provider<GetSellerPayoutsUseCase> getSellerPayoutsUseCaseProvider;

  public SellerEarningsViewModel_Factory(
      Provider<GetSellerEarningsUseCase> getSellerEarningsUseCaseProvider,
      Provider<GetSellerPayoutsUseCase> getSellerPayoutsUseCaseProvider) {
    this.getSellerEarningsUseCaseProvider = getSellerEarningsUseCaseProvider;
    this.getSellerPayoutsUseCaseProvider = getSellerPayoutsUseCaseProvider;
  }

  @Override
  public SellerEarningsViewModel get() {
    return newInstance(getSellerEarningsUseCaseProvider.get(), getSellerPayoutsUseCaseProvider.get());
  }

  public static SellerEarningsViewModel_Factory create(
      Provider<GetSellerEarningsUseCase> getSellerEarningsUseCaseProvider,
      Provider<GetSellerPayoutsUseCase> getSellerPayoutsUseCaseProvider) {
    return new SellerEarningsViewModel_Factory(getSellerEarningsUseCaseProvider, getSellerPayoutsUseCaseProvider);
  }

  public static SellerEarningsViewModel newInstance(
      GetSellerEarningsUseCase getSellerEarningsUseCase,
      GetSellerPayoutsUseCase getSellerPayoutsUseCase) {
    return new SellerEarningsViewModel(getSellerEarningsUseCase, getSellerPayoutsUseCase);
  }
}
