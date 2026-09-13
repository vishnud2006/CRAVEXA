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
public final class ClearCartUseCase_Factory implements Factory<ClearCartUseCase> {
  private final Provider<CartRepository> cartRepositoryProvider;

  public ClearCartUseCase_Factory(Provider<CartRepository> cartRepositoryProvider) {
    this.cartRepositoryProvider = cartRepositoryProvider;
  }

  @Override
  public ClearCartUseCase get() {
    return newInstance(cartRepositoryProvider.get());
  }

  public static ClearCartUseCase_Factory create(Provider<CartRepository> cartRepositoryProvider) {
    return new ClearCartUseCase_Factory(cartRepositoryProvider);
  }

  public static ClearCartUseCase newInstance(CartRepository cartRepository) {
    return new ClearCartUseCase(cartRepository);
  }
}
