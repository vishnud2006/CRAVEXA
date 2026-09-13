package com.cravexa.presentation.checkout;

import com.cravexa.domain.repository.AuthRepository;
import com.cravexa.domain.usecase.CartCalculator;
import com.cravexa.domain.usecase.CreateOrderUseCase;
import com.cravexa.domain.usecase.GetAddressesUseCase;
import com.cravexa.domain.usecase.GetCartUseCase;
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
public final class CheckoutViewModel_Factory implements Factory<CheckoutViewModel> {
  private final Provider<GetCartUseCase> getCartUseCaseProvider;

  private final Provider<GetAddressesUseCase> getAddressesUseCaseProvider;

  private final Provider<CartCalculator> cartCalculatorProvider;

  private final Provider<CreateOrderUseCase> createOrderUseCaseProvider;

  private final Provider<AuthRepository> authRepositoryProvider;

  public CheckoutViewModel_Factory(Provider<GetCartUseCase> getCartUseCaseProvider,
      Provider<GetAddressesUseCase> getAddressesUseCaseProvider,
      Provider<CartCalculator> cartCalculatorProvider,
      Provider<CreateOrderUseCase> createOrderUseCaseProvider,
      Provider<AuthRepository> authRepositoryProvider) {
    this.getCartUseCaseProvider = getCartUseCaseProvider;
    this.getAddressesUseCaseProvider = getAddressesUseCaseProvider;
    this.cartCalculatorProvider = cartCalculatorProvider;
    this.createOrderUseCaseProvider = createOrderUseCaseProvider;
    this.authRepositoryProvider = authRepositoryProvider;
  }

  @Override
  public CheckoutViewModel get() {
    return newInstance(getCartUseCaseProvider.get(), getAddressesUseCaseProvider.get(), cartCalculatorProvider.get(), createOrderUseCaseProvider.get(), authRepositoryProvider.get());
  }

  public static CheckoutViewModel_Factory create(Provider<GetCartUseCase> getCartUseCaseProvider,
      Provider<GetAddressesUseCase> getAddressesUseCaseProvider,
      Provider<CartCalculator> cartCalculatorProvider,
      Provider<CreateOrderUseCase> createOrderUseCaseProvider,
      Provider<AuthRepository> authRepositoryProvider) {
    return new CheckoutViewModel_Factory(getCartUseCaseProvider, getAddressesUseCaseProvider, cartCalculatorProvider, createOrderUseCaseProvider, authRepositoryProvider);
  }

  public static CheckoutViewModel newInstance(GetCartUseCase getCartUseCase,
      GetAddressesUseCase getAddressesUseCase, CartCalculator cartCalculator,
      CreateOrderUseCase createOrderUseCase, AuthRepository authRepository) {
    return new CheckoutViewModel(getCartUseCase, getAddressesUseCase, cartCalculator, createOrderUseCase, authRepository);
  }
}
