package com.cravexa.presentation.cart;

import com.cravexa.domain.usecase.CartCalculator;
import com.cravexa.domain.usecase.ClearCartUseCase;
import com.cravexa.domain.usecase.GetCartUseCase;
import com.cravexa.domain.usecase.RemoveFromCartUseCase;
import com.cravexa.domain.usecase.SaveForLaterUseCase;
import com.cravexa.domain.usecase.UpdateCartQuantityUseCase;
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
public final class CartViewModel_Factory implements Factory<CartViewModel> {
  private final Provider<GetCartUseCase> getCartUseCaseProvider;

  private final Provider<UpdateCartQuantityUseCase> updateCartQuantityUseCaseProvider;

  private final Provider<RemoveFromCartUseCase> removeFromCartUseCaseProvider;

  private final Provider<ClearCartUseCase> clearCartUseCaseProvider;

  private final Provider<SaveForLaterUseCase> saveForLaterUseCaseProvider;

  private final Provider<CartCalculator> cartCalculatorProvider;

  public CartViewModel_Factory(Provider<GetCartUseCase> getCartUseCaseProvider,
      Provider<UpdateCartQuantityUseCase> updateCartQuantityUseCaseProvider,
      Provider<RemoveFromCartUseCase> removeFromCartUseCaseProvider,
      Provider<ClearCartUseCase> clearCartUseCaseProvider,
      Provider<SaveForLaterUseCase> saveForLaterUseCaseProvider,
      Provider<CartCalculator> cartCalculatorProvider) {
    this.getCartUseCaseProvider = getCartUseCaseProvider;
    this.updateCartQuantityUseCaseProvider = updateCartQuantityUseCaseProvider;
    this.removeFromCartUseCaseProvider = removeFromCartUseCaseProvider;
    this.clearCartUseCaseProvider = clearCartUseCaseProvider;
    this.saveForLaterUseCaseProvider = saveForLaterUseCaseProvider;
    this.cartCalculatorProvider = cartCalculatorProvider;
  }

  @Override
  public CartViewModel get() {
    return newInstance(getCartUseCaseProvider.get(), updateCartQuantityUseCaseProvider.get(), removeFromCartUseCaseProvider.get(), clearCartUseCaseProvider.get(), saveForLaterUseCaseProvider.get(), cartCalculatorProvider.get());
  }

  public static CartViewModel_Factory create(Provider<GetCartUseCase> getCartUseCaseProvider,
      Provider<UpdateCartQuantityUseCase> updateCartQuantityUseCaseProvider,
      Provider<RemoveFromCartUseCase> removeFromCartUseCaseProvider,
      Provider<ClearCartUseCase> clearCartUseCaseProvider,
      Provider<SaveForLaterUseCase> saveForLaterUseCaseProvider,
      Provider<CartCalculator> cartCalculatorProvider) {
    return new CartViewModel_Factory(getCartUseCaseProvider, updateCartQuantityUseCaseProvider, removeFromCartUseCaseProvider, clearCartUseCaseProvider, saveForLaterUseCaseProvider, cartCalculatorProvider);
  }

  public static CartViewModel newInstance(GetCartUseCase getCartUseCase,
      UpdateCartQuantityUseCase updateCartQuantityUseCase,
      RemoveFromCartUseCase removeFromCartUseCase, ClearCartUseCase clearCartUseCase,
      SaveForLaterUseCase saveForLaterUseCase, CartCalculator cartCalculator) {
    return new CartViewModel(getCartUseCase, updateCartQuantityUseCase, removeFromCartUseCase, clearCartUseCase, saveForLaterUseCase, cartCalculator);
  }
}
