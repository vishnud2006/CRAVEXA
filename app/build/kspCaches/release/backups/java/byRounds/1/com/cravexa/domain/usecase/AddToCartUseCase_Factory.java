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
public final class AddToCartUseCase_Factory implements Factory<AddToCartUseCase> {
  private final Provider<CartRepository> cartRepositoryProvider;

  public AddToCartUseCase_Factory(Provider<CartRepository> cartRepositoryProvider) {
    this.cartRepositoryProvider = cartRepositoryProvider;
  }

  @Override
  public AddToCartUseCase get() {
    return newInstance(cartRepositoryProvider.get());
  }

  public static AddToCartUseCase_Factory create(Provider<CartRepository> cartRepositoryProvider) {
    return new AddToCartUseCase_Factory(cartRepositoryProvider);
  }

  public static AddToCartUseCase newInstance(CartRepository cartRepository) {
    return new AddToCartUseCase(cartRepository);
  }
}
