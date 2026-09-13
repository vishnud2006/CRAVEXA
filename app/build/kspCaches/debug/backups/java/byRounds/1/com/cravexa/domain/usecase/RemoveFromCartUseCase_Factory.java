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
public final class RemoveFromCartUseCase_Factory implements Factory<RemoveFromCartUseCase> {
  private final Provider<CartRepository> cartRepositoryProvider;

  public RemoveFromCartUseCase_Factory(Provider<CartRepository> cartRepositoryProvider) {
    this.cartRepositoryProvider = cartRepositoryProvider;
  }

  @Override
  public RemoveFromCartUseCase get() {
    return newInstance(cartRepositoryProvider.get());
  }

  public static RemoveFromCartUseCase_Factory create(
      Provider<CartRepository> cartRepositoryProvider) {
    return new RemoveFromCartUseCase_Factory(cartRepositoryProvider);
  }

  public static RemoveFromCartUseCase newInstance(CartRepository cartRepository) {
    return new RemoveFromCartUseCase(cartRepository);
  }
}
