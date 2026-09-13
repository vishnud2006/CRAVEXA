package com.cravexa.presentation.product;

import androidx.lifecycle.SavedStateHandle;
import com.cravexa.domain.usecase.AddToCartUseCase;
import com.cravexa.domain.usecase.GetProductDetailUseCase;
import com.cravexa.domain.usecase.GetWishlistUseCase;
import com.cravexa.domain.usecase.ToggleWishlistUseCase;
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
public final class ProductDetailViewModel_Factory implements Factory<ProductDetailViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<GetProductDetailUseCase> getProductDetailUseCaseProvider;

  private final Provider<GetWishlistUseCase> getWishlistUseCaseProvider;

  private final Provider<ToggleWishlistUseCase> toggleWishlistUseCaseProvider;

  private final Provider<AddToCartUseCase> addToCartUseCaseProvider;

  public ProductDetailViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<GetProductDetailUseCase> getProductDetailUseCaseProvider,
      Provider<GetWishlistUseCase> getWishlistUseCaseProvider,
      Provider<ToggleWishlistUseCase> toggleWishlistUseCaseProvider,
      Provider<AddToCartUseCase> addToCartUseCaseProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.getProductDetailUseCaseProvider = getProductDetailUseCaseProvider;
    this.getWishlistUseCaseProvider = getWishlistUseCaseProvider;
    this.toggleWishlistUseCaseProvider = toggleWishlistUseCaseProvider;
    this.addToCartUseCaseProvider = addToCartUseCaseProvider;
  }

  @Override
  public ProductDetailViewModel get() {
    return newInstance(savedStateHandleProvider.get(), getProductDetailUseCaseProvider.get(), getWishlistUseCaseProvider.get(), toggleWishlistUseCaseProvider.get(), addToCartUseCaseProvider.get());
  }

  public static ProductDetailViewModel_Factory create(
      Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<GetProductDetailUseCase> getProductDetailUseCaseProvider,
      Provider<GetWishlistUseCase> getWishlistUseCaseProvider,
      Provider<ToggleWishlistUseCase> toggleWishlistUseCaseProvider,
      Provider<AddToCartUseCase> addToCartUseCaseProvider) {
    return new ProductDetailViewModel_Factory(savedStateHandleProvider, getProductDetailUseCaseProvider, getWishlistUseCaseProvider, toggleWishlistUseCaseProvider, addToCartUseCaseProvider);
  }

  public static ProductDetailViewModel newInstance(SavedStateHandle savedStateHandle,
      GetProductDetailUseCase getProductDetailUseCase, GetWishlistUseCase getWishlistUseCase,
      ToggleWishlistUseCase toggleWishlistUseCase, AddToCartUseCase addToCartUseCase) {
    return new ProductDetailViewModel(savedStateHandle, getProductDetailUseCase, getWishlistUseCase, toggleWishlistUseCase, addToCartUseCase);
  }
}
