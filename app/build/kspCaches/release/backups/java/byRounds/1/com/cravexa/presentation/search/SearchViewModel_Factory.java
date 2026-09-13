package com.cravexa.presentation.search;

import androidx.lifecycle.SavedStateHandle;
import com.cravexa.domain.usecase.AddToCartUseCase;
import com.cravexa.domain.usecase.GetCategoriesUseCase;
import com.cravexa.domain.usecase.GetWishlistUseCase;
import com.cravexa.domain.usecase.RecentSearchUseCases;
import com.cravexa.domain.usecase.SearchProductsUseCase;
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
public final class SearchViewModel_Factory implements Factory<SearchViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<SearchProductsUseCase> searchProductsUseCaseProvider;

  private final Provider<GetCategoriesUseCase> getCategoriesUseCaseProvider;

  private final Provider<RecentSearchUseCases> recentSearchUseCasesProvider;

  private final Provider<GetWishlistUseCase> getWishlistUseCaseProvider;

  private final Provider<ToggleWishlistUseCase> toggleWishlistUseCaseProvider;

  private final Provider<AddToCartUseCase> addToCartUseCaseProvider;

  public SearchViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<SearchProductsUseCase> searchProductsUseCaseProvider,
      Provider<GetCategoriesUseCase> getCategoriesUseCaseProvider,
      Provider<RecentSearchUseCases> recentSearchUseCasesProvider,
      Provider<GetWishlistUseCase> getWishlistUseCaseProvider,
      Provider<ToggleWishlistUseCase> toggleWishlistUseCaseProvider,
      Provider<AddToCartUseCase> addToCartUseCaseProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.searchProductsUseCaseProvider = searchProductsUseCaseProvider;
    this.getCategoriesUseCaseProvider = getCategoriesUseCaseProvider;
    this.recentSearchUseCasesProvider = recentSearchUseCasesProvider;
    this.getWishlistUseCaseProvider = getWishlistUseCaseProvider;
    this.toggleWishlistUseCaseProvider = toggleWishlistUseCaseProvider;
    this.addToCartUseCaseProvider = addToCartUseCaseProvider;
  }

  @Override
  public SearchViewModel get() {
    return newInstance(savedStateHandleProvider.get(), searchProductsUseCaseProvider.get(), getCategoriesUseCaseProvider.get(), recentSearchUseCasesProvider.get(), getWishlistUseCaseProvider.get(), toggleWishlistUseCaseProvider.get(), addToCartUseCaseProvider.get());
  }

  public static SearchViewModel_Factory create(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<SearchProductsUseCase> searchProductsUseCaseProvider,
      Provider<GetCategoriesUseCase> getCategoriesUseCaseProvider,
      Provider<RecentSearchUseCases> recentSearchUseCasesProvider,
      Provider<GetWishlistUseCase> getWishlistUseCaseProvider,
      Provider<ToggleWishlistUseCase> toggleWishlistUseCaseProvider,
      Provider<AddToCartUseCase> addToCartUseCaseProvider) {
    return new SearchViewModel_Factory(savedStateHandleProvider, searchProductsUseCaseProvider, getCategoriesUseCaseProvider, recentSearchUseCasesProvider, getWishlistUseCaseProvider, toggleWishlistUseCaseProvider, addToCartUseCaseProvider);
  }

  public static SearchViewModel newInstance(SavedStateHandle savedStateHandle,
      SearchProductsUseCase searchProductsUseCase, GetCategoriesUseCase getCategoriesUseCase,
      RecentSearchUseCases recentSearchUseCases, GetWishlistUseCase getWishlistUseCase,
      ToggleWishlistUseCase toggleWishlistUseCase, AddToCartUseCase addToCartUseCase) {
    return new SearchViewModel(savedStateHandle, searchProductsUseCase, getCategoriesUseCase, recentSearchUseCases, getWishlistUseCase, toggleWishlistUseCase, addToCartUseCase);
  }
}
