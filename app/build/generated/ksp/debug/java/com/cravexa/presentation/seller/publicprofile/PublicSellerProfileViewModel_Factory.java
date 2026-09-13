package com.cravexa.presentation.seller.publicprofile;

import androidx.lifecycle.SavedStateHandle;
import com.cravexa.domain.usecase.AddToCartUseCase;
import com.cravexa.domain.usecase.GetSellerProductsUseCase;
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
public final class PublicSellerProfileViewModel_Factory implements Factory<PublicSellerProfileViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<GetSellerProductsUseCase> getSellerProductsUseCaseProvider;

  private final Provider<GetWishlistUseCase> getWishlistUseCaseProvider;

  private final Provider<ToggleWishlistUseCase> toggleWishlistUseCaseProvider;

  private final Provider<AddToCartUseCase> addToCartUseCaseProvider;

  public PublicSellerProfileViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<GetSellerProductsUseCase> getSellerProductsUseCaseProvider,
      Provider<GetWishlistUseCase> getWishlistUseCaseProvider,
      Provider<ToggleWishlistUseCase> toggleWishlistUseCaseProvider,
      Provider<AddToCartUseCase> addToCartUseCaseProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.getSellerProductsUseCaseProvider = getSellerProductsUseCaseProvider;
    this.getWishlistUseCaseProvider = getWishlistUseCaseProvider;
    this.toggleWishlistUseCaseProvider = toggleWishlistUseCaseProvider;
    this.addToCartUseCaseProvider = addToCartUseCaseProvider;
  }

  @Override
  public PublicSellerProfileViewModel get() {
    return newInstance(savedStateHandleProvider.get(), getSellerProductsUseCaseProvider.get(), getWishlistUseCaseProvider.get(), toggleWishlistUseCaseProvider.get(), addToCartUseCaseProvider.get());
  }

  public static PublicSellerProfileViewModel_Factory create(
      Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<GetSellerProductsUseCase> getSellerProductsUseCaseProvider,
      Provider<GetWishlistUseCase> getWishlistUseCaseProvider,
      Provider<ToggleWishlistUseCase> toggleWishlistUseCaseProvider,
      Provider<AddToCartUseCase> addToCartUseCaseProvider) {
    return new PublicSellerProfileViewModel_Factory(savedStateHandleProvider, getSellerProductsUseCaseProvider, getWishlistUseCaseProvider, toggleWishlistUseCaseProvider, addToCartUseCaseProvider);
  }

  public static PublicSellerProfileViewModel newInstance(SavedStateHandle savedStateHandle,
      GetSellerProductsUseCase getSellerProductsUseCase, GetWishlistUseCase getWishlistUseCase,
      ToggleWishlistUseCase toggleWishlistUseCase, AddToCartUseCase addToCartUseCase) {
    return new PublicSellerProfileViewModel(savedStateHandle, getSellerProductsUseCase, getWishlistUseCase, toggleWishlistUseCase, addToCartUseCase);
  }
}
