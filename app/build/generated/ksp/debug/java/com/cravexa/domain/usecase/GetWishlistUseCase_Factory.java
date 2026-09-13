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
public final class GetWishlistUseCase_Factory implements Factory<GetWishlistUseCase> {
  private final Provider<WishlistRepository> wishlistRepositoryProvider;

  public GetWishlistUseCase_Factory(Provider<WishlistRepository> wishlistRepositoryProvider) {
    this.wishlistRepositoryProvider = wishlistRepositoryProvider;
  }

  @Override
  public GetWishlistUseCase get() {
    return newInstance(wishlistRepositoryProvider.get());
  }

  public static GetWishlistUseCase_Factory create(
      Provider<WishlistRepository> wishlistRepositoryProvider) {
    return new GetWishlistUseCase_Factory(wishlistRepositoryProvider);
  }

  public static GetWishlistUseCase newInstance(WishlistRepository wishlistRepository) {
    return new GetWishlistUseCase(wishlistRepository);
  }
}
