package com.cravexa.presentation.home;

import com.cravexa.domain.usecase.AddToCartUseCase;
import com.cravexa.domain.usecase.GetCartUseCase;
import com.cravexa.domain.usecase.GetHomeMarketplaceUseCase;
import com.cravexa.domain.usecase.GetUserProfileUseCase;
import com.cravexa.domain.usecase.GetWishlistUseCase;
import com.cravexa.domain.usecase.LogoutUseCase;
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
public final class HomeViewModel_Factory implements Factory<HomeViewModel> {
  private final Provider<GetUserProfileUseCase> getUserProfileUseCaseProvider;

  private final Provider<GetHomeMarketplaceUseCase> getHomeMarketplaceUseCaseProvider;

  private final Provider<GetWishlistUseCase> getWishlistUseCaseProvider;

  private final Provider<ToggleWishlistUseCase> toggleWishlistUseCaseProvider;

  private final Provider<GetCartUseCase> getCartUseCaseProvider;

  private final Provider<AddToCartUseCase> addToCartUseCaseProvider;

  private final Provider<LogoutUseCase> logoutUseCaseProvider;

  public HomeViewModel_Factory(Provider<GetUserProfileUseCase> getUserProfileUseCaseProvider,
      Provider<GetHomeMarketplaceUseCase> getHomeMarketplaceUseCaseProvider,
      Provider<GetWishlistUseCase> getWishlistUseCaseProvider,
      Provider<ToggleWishlistUseCase> toggleWishlistUseCaseProvider,
      Provider<GetCartUseCase> getCartUseCaseProvider,
      Provider<AddToCartUseCase> addToCartUseCaseProvider,
      Provider<LogoutUseCase> logoutUseCaseProvider) {
    this.getUserProfileUseCaseProvider = getUserProfileUseCaseProvider;
    this.getHomeMarketplaceUseCaseProvider = getHomeMarketplaceUseCaseProvider;
    this.getWishlistUseCaseProvider = getWishlistUseCaseProvider;
    this.toggleWishlistUseCaseProvider = toggleWishlistUseCaseProvider;
    this.getCartUseCaseProvider = getCartUseCaseProvider;
    this.addToCartUseCaseProvider = addToCartUseCaseProvider;
    this.logoutUseCaseProvider = logoutUseCaseProvider;
  }

  @Override
  public HomeViewModel get() {
    return newInstance(getUserProfileUseCaseProvider.get(), getHomeMarketplaceUseCaseProvider.get(), getWishlistUseCaseProvider.get(), toggleWishlistUseCaseProvider.get(), getCartUseCaseProvider.get(), addToCartUseCaseProvider.get(), logoutUseCaseProvider.get());
  }

  public static HomeViewModel_Factory create(
      Provider<GetUserProfileUseCase> getUserProfileUseCaseProvider,
      Provider<GetHomeMarketplaceUseCase> getHomeMarketplaceUseCaseProvider,
      Provider<GetWishlistUseCase> getWishlistUseCaseProvider,
      Provider<ToggleWishlistUseCase> toggleWishlistUseCaseProvider,
      Provider<GetCartUseCase> getCartUseCaseProvider,
      Provider<AddToCartUseCase> addToCartUseCaseProvider,
      Provider<LogoutUseCase> logoutUseCaseProvider) {
    return new HomeViewModel_Factory(getUserProfileUseCaseProvider, getHomeMarketplaceUseCaseProvider, getWishlistUseCaseProvider, toggleWishlistUseCaseProvider, getCartUseCaseProvider, addToCartUseCaseProvider, logoutUseCaseProvider);
  }

  public static HomeViewModel newInstance(GetUserProfileUseCase getUserProfileUseCase,
      GetHomeMarketplaceUseCase getHomeMarketplaceUseCase, GetWishlistUseCase getWishlistUseCase,
      ToggleWishlistUseCase toggleWishlistUseCase, GetCartUseCase getCartUseCase,
      AddToCartUseCase addToCartUseCase, LogoutUseCase logoutUseCase) {
    return new HomeViewModel(getUserProfileUseCase, getHomeMarketplaceUseCase, getWishlistUseCase, toggleWishlistUseCase, getCartUseCase, addToCartUseCase, logoutUseCase);
  }
}
