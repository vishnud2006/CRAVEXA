package com.cravexa.presentation.seller.products;

import com.cravexa.domain.usecase.DeleteSellerProductUseCase;
import com.cravexa.domain.usecase.GetSellerKitchenProductsUseCase;
import com.cravexa.domain.usecase.ToggleProductAvailabilityUseCase;
import com.cravexa.domain.usecase.UpdateProductStockUseCase;
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
public final class SellerProductsViewModel_Factory implements Factory<SellerProductsViewModel> {
  private final Provider<GetSellerKitchenProductsUseCase> getSellerKitchenProductsUseCaseProvider;

  private final Provider<UpdateProductStockUseCase> updateProductStockUseCaseProvider;

  private final Provider<ToggleProductAvailabilityUseCase> toggleProductAvailabilityUseCaseProvider;

  private final Provider<DeleteSellerProductUseCase> deleteSellerProductUseCaseProvider;

  public SellerProductsViewModel_Factory(
      Provider<GetSellerKitchenProductsUseCase> getSellerKitchenProductsUseCaseProvider,
      Provider<UpdateProductStockUseCase> updateProductStockUseCaseProvider,
      Provider<ToggleProductAvailabilityUseCase> toggleProductAvailabilityUseCaseProvider,
      Provider<DeleteSellerProductUseCase> deleteSellerProductUseCaseProvider) {
    this.getSellerKitchenProductsUseCaseProvider = getSellerKitchenProductsUseCaseProvider;
    this.updateProductStockUseCaseProvider = updateProductStockUseCaseProvider;
    this.toggleProductAvailabilityUseCaseProvider = toggleProductAvailabilityUseCaseProvider;
    this.deleteSellerProductUseCaseProvider = deleteSellerProductUseCaseProvider;
  }

  @Override
  public SellerProductsViewModel get() {
    return newInstance(getSellerKitchenProductsUseCaseProvider.get(), updateProductStockUseCaseProvider.get(), toggleProductAvailabilityUseCaseProvider.get(), deleteSellerProductUseCaseProvider.get());
  }

  public static SellerProductsViewModel_Factory create(
      Provider<GetSellerKitchenProductsUseCase> getSellerKitchenProductsUseCaseProvider,
      Provider<UpdateProductStockUseCase> updateProductStockUseCaseProvider,
      Provider<ToggleProductAvailabilityUseCase> toggleProductAvailabilityUseCaseProvider,
      Provider<DeleteSellerProductUseCase> deleteSellerProductUseCaseProvider) {
    return new SellerProductsViewModel_Factory(getSellerKitchenProductsUseCaseProvider, updateProductStockUseCaseProvider, toggleProductAvailabilityUseCaseProvider, deleteSellerProductUseCaseProvider);
  }

  public static SellerProductsViewModel newInstance(
      GetSellerKitchenProductsUseCase getSellerKitchenProductsUseCase,
      UpdateProductStockUseCase updateProductStockUseCase,
      ToggleProductAvailabilityUseCase toggleProductAvailabilityUseCase,
      DeleteSellerProductUseCase deleteSellerProductUseCase) {
    return new SellerProductsViewModel(getSellerKitchenProductsUseCase, updateProductStockUseCase, toggleProductAvailabilityUseCase, deleteSellerProductUseCase);
  }
}
