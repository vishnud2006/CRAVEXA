package com.cravexa.presentation.explore;

import com.cravexa.domain.usecase.AddToCartUseCase;
import com.cravexa.domain.usecase.GetCategoriesUseCase;
import com.cravexa.domain.usecase.GetHomeMarketplaceUseCase;
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
public final class ExploreViewModel_Factory implements Factory<ExploreViewModel> {
  private final Provider<GetCategoriesUseCase> getCategoriesUseCaseProvider;

  private final Provider<GetHomeMarketplaceUseCase> getHomeMarketplaceUseCaseProvider;

  private final Provider<GetWishlistUseCase> getWishlistUseCaseProvider;

  private final Provider<ToggleWishlistUseCase> toggleWishlistUseCaseProvider;

  private final Provider<AddToCartUseCase> addToCartUseCaseProvider;

  public ExploreViewModel_Factory(Provider<GetCategoriesUseCase> getCategoriesUseCaseProvider,
      Provider<GetHomeMarketplaceUseCase> getHomeMarketplaceUseCaseProvider,
      Provider<GetWishlistUseCase> getWishlistUseCaseProvider,
      Provider<ToggleWishlistUseCase> toggleWishlistUseCaseProvider,
      Provider<AddToCartUseCase> addToCartUseCaseProvider) {
    this.getCategoriesUseCaseProvider = getCategoriesUseCaseProvider;
    this.getHomeMarketplaceUseCaseProvider = getHomeMarketplaceUseCaseProvider;
    this.getWishlistUseCaseProvider = getWishlistUseCaseProvider;
    this.toggleWishlistUseCaseProvider = toggleWishlistUseCaseProvider;
    this.addToCartUseCaseProvider = addToCartUseCaseProvider;
  }

  @Override
  public ExploreViewModel get() {
    return newInstance(getCategoriesUseCaseProvider.get(), getHomeMarketplaceUseCaseProvider.get(), getWishlistUseCaseProvider.get(), toggleWishlistUseCaseProvider.get(), addToCartUseCaseProvider.get());
  }

  public static ExploreViewModel_Factory create(
      Provider<GetCategoriesUseCase> getCategoriesUseCaseProvider,
      Provider<GetHomeMarketplaceUseCase> getHomeMarketplaceUseCaseProvider,
      Provider<GetWishlistUseCase> getWishlistUseCaseProvider,
      Provider<ToggleWishlistUseCase> toggleWishlistUseCaseProvider,
      Provider<AddToCartUseCase> addToCartUseCaseProvider) {
    return new ExploreViewModel_Factory(getCategoriesUseCaseProvider, getHomeMarketplaceUseCaseProvider, getWishlistUseCaseProvider, toggleWishlistUseCaseProvider, addToCartUseCaseProvider);
  }

  public static ExploreViewModel newInstance(GetCategoriesUseCase getCategoriesUseCase,
      GetHomeMarketplaceUseCase getHomeMarketplaceUseCase, GetWishlistUseCase getWishlistUseCase,
      ToggleWishlistUseCase toggleWishlistUseCase, AddToCartUseCase addToCartUseCase) {
    return new ExploreViewModel(getCategoriesUseCase, getHomeMarketplaceUseCase, getWishlistUseCase, toggleWishlistUseCase, addToCartUseCase);
  }
}
