package com.cravexa.presentation.category;

import androidx.lifecycle.SavedStateHandle;
import com.cravexa.domain.usecase.AddToCartUseCase;
import com.cravexa.domain.usecase.GetCategoriesUseCase;
import com.cravexa.domain.usecase.GetCategoryProductsUseCase;
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
public final class CategoryProductsViewModel_Factory implements Factory<CategoryProductsViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<GetCategoryProductsUseCase> getCategoryProductsUseCaseProvider;

  private final Provider<GetCategoriesUseCase> getCategoriesUseCaseProvider;

  private final Provider<GetWishlistUseCase> getWishlistUseCaseProvider;

  private final Provider<ToggleWishlistUseCase> toggleWishlistUseCaseProvider;

  private final Provider<AddToCartUseCase> addToCartUseCaseProvider;

  public CategoryProductsViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<GetCategoryProductsUseCase> getCategoryProductsUseCaseProvider,
      Provider<GetCategoriesUseCase> getCategoriesUseCaseProvider,
      Provider<GetWishlistUseCase> getWishlistUseCaseProvider,
      Provider<ToggleWishlistUseCase> toggleWishlistUseCaseProvider,
      Provider<AddToCartUseCase> addToCartUseCaseProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.getCategoryProductsUseCaseProvider = getCategoryProductsUseCaseProvider;
    this.getCategoriesUseCaseProvider = getCategoriesUseCaseProvider;
    this.getWishlistUseCaseProvider = getWishlistUseCaseProvider;
    this.toggleWishlistUseCaseProvider = toggleWishlistUseCaseProvider;
    this.addToCartUseCaseProvider = addToCartUseCaseProvider;
  }

  @Override
  public CategoryProductsViewModel get() {
    return newInstance(savedStateHandleProvider.get(), getCategoryProductsUseCaseProvider.get(), getCategoriesUseCaseProvider.get(), getWishlistUseCaseProvider.get(), toggleWishlistUseCaseProvider.get(), addToCartUseCaseProvider.get());
  }

  public static CategoryProductsViewModel_Factory create(
      Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<GetCategoryProductsUseCase> getCategoryProductsUseCaseProvider,
      Provider<GetCategoriesUseCase> getCategoriesUseCaseProvider,
      Provider<GetWishlistUseCase> getWishlistUseCaseProvider,
      Provider<ToggleWishlistUseCase> toggleWishlistUseCaseProvider,
      Provider<AddToCartUseCase> addToCartUseCaseProvider) {
    return new CategoryProductsViewModel_Factory(savedStateHandleProvider, getCategoryProductsUseCaseProvider, getCategoriesUseCaseProvider, getWishlistUseCaseProvider, toggleWishlistUseCaseProvider, addToCartUseCaseProvider);
  }

  public static CategoryProductsViewModel newInstance(SavedStateHandle savedStateHandle,
      GetCategoryProductsUseCase getCategoryProductsUseCase,
      GetCategoriesUseCase getCategoriesUseCase, GetWishlistUseCase getWishlistUseCase,
      ToggleWishlistUseCase toggleWishlistUseCase, AddToCartUseCase addToCartUseCase) {
    return new CategoryProductsViewModel(savedStateHandle, getCategoryProductsUseCase, getCategoriesUseCase, getWishlistUseCase, toggleWishlistUseCase, addToCartUseCase);
  }
}
