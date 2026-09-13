package com.cravexa.domain.usecase;

import com.cravexa.domain.repository.AuthRepository;
import com.cravexa.domain.repository.CartRepository;
import com.cravexa.domain.repository.OrderRepository;
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
public final class CreateOrderUseCase_Factory implements Factory<CreateOrderUseCase> {
  private final Provider<OrderRepository> orderRepositoryProvider;

  private final Provider<AuthRepository> authRepositoryProvider;

  private final Provider<CartRepository> cartRepositoryProvider;

  private final Provider<CartCalculator> cartCalculatorProvider;

  public CreateOrderUseCase_Factory(Provider<OrderRepository> orderRepositoryProvider,
      Provider<AuthRepository> authRepositoryProvider,
      Provider<CartRepository> cartRepositoryProvider,
      Provider<CartCalculator> cartCalculatorProvider) {
    this.orderRepositoryProvider = orderRepositoryProvider;
    this.authRepositoryProvider = authRepositoryProvider;
    this.cartRepositoryProvider = cartRepositoryProvider;
    this.cartCalculatorProvider = cartCalculatorProvider;
  }

  @Override
  public CreateOrderUseCase get() {
    return newInstance(orderRepositoryProvider.get(), authRepositoryProvider.get(), cartRepositoryProvider.get(), cartCalculatorProvider.get());
  }

  public static CreateOrderUseCase_Factory create(Provider<OrderRepository> orderRepositoryProvider,
      Provider<AuthRepository> authRepositoryProvider,
      Provider<CartRepository> cartRepositoryProvider,
      Provider<CartCalculator> cartCalculatorProvider) {
    return new CreateOrderUseCase_Factory(orderRepositoryProvider, authRepositoryProvider, cartRepositoryProvider, cartCalculatorProvider);
  }

  public static CreateOrderUseCase newInstance(OrderRepository orderRepository,
      AuthRepository authRepository, CartRepository cartRepository, CartCalculator cartCalculator) {
    return new CreateOrderUseCase(orderRepository, authRepository, cartRepository, cartCalculator);
  }
}
