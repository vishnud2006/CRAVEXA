package com.cravexa.domain.usecase;

import com.cravexa.domain.repository.CartRepository;
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
public final class SaveForLaterUseCase_Factory implements Factory<SaveForLaterUseCase> {
  private final Provider<CartRepository> cartRepositoryProvider;

  private final Provider<WishlistRepository> wishlistRepositoryProvider;

  public SaveForLaterUseCase_Factory(Provider<CartRepository> cartRepositoryProvider,
      Provider<WishlistRepository> wishlistRepositoryProvider) {
    this.cartRepositoryProvider = cartRepositoryProvider;
    this.wishlistRepositoryProvider = wishlistRepositoryProvider;
  }

  @Override
  public SaveForLaterUseCase get() {
    return newInstance(cartRepositoryProvider.get(), wishlistRepositoryProvider.get());
  }

  public static SaveForLaterUseCase_Factory create(Provider<CartRepository> cartRepositoryProvider,
      Provider<WishlistRepository> wishlistRepositoryProvider) {
    return new SaveForLaterUseCase_Factory(cartRepositoryProvider, wishlistRepositoryProvider);
  }

  public static SaveForLaterUseCase newInstance(CartRepository cartRepository,
      WishlistRepository wishlistRepository) {
    return new SaveForLaterUseCase(cartRepository, wishlistRepository);
  }
}
