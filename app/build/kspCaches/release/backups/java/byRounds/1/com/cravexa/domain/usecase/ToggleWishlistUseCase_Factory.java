package com.cravexa.domain.usecase;

import com.cravexa.domain.repository.WishlistRepository;
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
public final class ToggleWishlistUseCase_Factory implements Factory<ToggleWishlistUseCase> {
  private final Provider<WishlistRepository> wishlistRepositoryProvider;

  public ToggleWishlistUseCase_Factory(Provider<WishlistRepository> wishlistRepositoryProvider) {
    this.wishlistRepositoryProvider = wishlistRepositoryProvider;
  }

  @Override
  public ToggleWishlistUseCase get() {
    return newInstance(wishlistRepositoryProvider.get());
  }

  public static ToggleWishlistUseCase_Factory create(
      Provider<WishlistRepository> wishlistRepositoryProvider) {
    return new ToggleWishlistUseCase_Factory(wishlistRepositoryProvider);
  }

  public static ToggleWishlistUseCase newInstance(WishlistRepository wishlistRepository) {
    return new ToggleWishlistUseCase(wishlistRepository);
  }
}
