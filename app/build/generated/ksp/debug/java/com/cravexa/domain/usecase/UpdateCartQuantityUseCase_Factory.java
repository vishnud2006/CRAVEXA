package com.cravexa.domain.usecase;

import com.cravexa.domain.repository.CartRepository;
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
public final class UpdateCartQuantityUseCase_Factory implements Factory<UpdateCartQuantityUseCase> {
  private final Provider<CartRepository> cartRepositoryProvider;

  public UpdateCartQuantityUseCase_Factory(Provider<CartRepository> cartRepositoryProvider) {
    this.cartRepositoryProvider = cartRepositoryProvider;
  }

  @Override
  public UpdateCartQuantityUseCase get() {
    return newInstance(cartRepositoryProvider.get());
  }

  public static UpdateCartQuantityUseCase_Factory create(
      Provider<CartRepository> cartRepositoryProvider) {
    return new UpdateCartQuantityUseCase_Factory(cartRepositoryProvider);
  }

  public static UpdateCartQuantityUseCase newInstance(CartRepository cartRepository) {
    return new UpdateCartQuantityUseCase(cartRepository);
  }
}
