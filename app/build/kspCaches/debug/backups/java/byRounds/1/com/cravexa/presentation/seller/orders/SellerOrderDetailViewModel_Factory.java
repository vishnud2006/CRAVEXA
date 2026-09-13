package com.cravexa.presentation.seller.orders;

import androidx.lifecycle.SavedStateHandle;
import com.cravexa.domain.usecase.GetSellerOrderDetailUseCase;
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
public final class SellerOrderDetailViewModel_Factory implements Factory<SellerOrderDetailViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<GetSellerOrderDetailUseCase> getSellerOrderDetailUseCaseProvider;

  private final Provider<UpdateSellerOrderStatusUseCase> updateSellerOrderStatusUseCaseProvider;

  public SellerOrderDetailViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<GetSellerOrderDetailUseCase> getSellerOrderDetailUseCaseProvider,
      Provider<UpdateSellerOrderStatusUseCase> updateSellerOrderStatusUseCaseProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.getSellerOrderDetailUseCaseProvider = getSellerOrderDetailUseCaseProvider;
    this.updateSellerOrderStatusUseCaseProvider = updateSellerOrderStatusUseCaseProvider;
  }

  @Override
  public SellerOrderDetailViewModel get() {
    return newInstance(savedStateHandleProvider.get(), getSellerOrderDetailUseCaseProvider.get(), updateSellerOrderStatusUseCaseProvider.get());
  }

  public static SellerOrderDetailViewModel_Factory create(
      Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<GetSellerOrderDetailUseCase> getSellerOrderDetailUseCaseProvider,
      Provider<UpdateSellerOrderStatusUseCase> updateSellerOrderStatusUseCaseProvider) {
    return new SellerOrderDetailViewModel_Factory(savedStateHandleProvider, getSellerOrderDetailUseCaseProvider, updateSellerOrderStatusUseCaseProvider);
  }

  public static SellerOrderDetailViewModel newInstance(SavedStateHandle savedStateHandle,
      GetSellerOrderDetailUseCase getSellerOrderDetailUseCase,
      UpdateSellerOrderStatusUseCase updateSellerOrderStatusUseCase) {
    return new SellerOrderDetailViewModel(savedStateHandle, getSellerOrderDetailUseCase, updateSellerOrderStatusUseCase);
  }
}
