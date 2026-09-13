package com.cravexa.presentation.seller.orders;

import com.cravexa.domain.usecase.GetSellerOrdersUseCase;
import com.cravexa.domain.usecase.UpdateSellerOrderStatusUseCase;
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
public final class SellerOrdersViewModel_Factory implements Factory<SellerOrdersViewModel> {
  private final Provider<GetSellerOrdersUseCase> getSellerOrdersUseCaseProvider;

  private final Provider<UpdateSellerOrderStatusUseCase> updateSellerOrderStatusUseCaseProvider;

  public SellerOrdersViewModel_Factory(
      Provider<GetSellerOrdersUseCase> getSellerOrdersUseCaseProvider,
      Provider<UpdateSellerOrderStatusUseCase> updateSellerOrderStatusUseCaseProvider) {
    this.getSellerOrdersUseCaseProvider = getSellerOrdersUseCaseProvider;
    this.updateSellerOrderStatusUseCaseProvider = updateSellerOrderStatusUseCaseProvider;
  }

  @Override
  public SellerOrdersViewModel get() {
    return newInstance(getSellerOrdersUseCaseProvider.get(), updateSellerOrderStatusUseCaseProvider.get());
  }

  public static SellerOrdersViewModel_Factory create(
      Provider<GetSellerOrdersUseCase> getSellerOrdersUseCaseProvider,
      Provider<UpdateSellerOrderStatusUseCase> updateSellerOrderStatusUseCaseProvider) {
    return new SellerOrdersViewModel_Factory(getSellerOrdersUseCaseProvider, updateSellerOrderStatusUseCaseProvider);
  }

  public static SellerOrdersViewModel newInstance(GetSellerOrdersUseCase getSellerOrdersUseCase,
      UpdateSellerOrderStatusUseCase updateSellerOrderStatusUseCase) {
    return new SellerOrdersViewModel(getSellerOrdersUseCase, updateSellerOrderStatusUseCase);
  }
}
