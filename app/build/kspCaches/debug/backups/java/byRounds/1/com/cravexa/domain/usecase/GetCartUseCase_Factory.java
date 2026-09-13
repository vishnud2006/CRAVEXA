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
public final class GetCartUseCase_Factory implements Factory<GetCartUseCase> {
  private final Provider<CartRepository> cartRepositoryProvider;

  public GetCartUseCase_Factory(Provider<CartRepository> cartRepositoryProvider) {
    this.cartRepositoryProvider = cartRepositoryProvider;
  }

  @Override
  public GetCartUseCase get() {
    return newInstance(cartRepositoryProvider.get());
  }

  public static GetCartUseCase_Factory create(Provider<CartRepository> cartRepositoryProvider) {
    return new GetCartUseCase_Factory(cartRepositoryProvider);
  }

  public static GetCartUseCase newInstance(CartRepository cartRepository) {
    return new GetCartUseCase(cartRepository);
  }
}
