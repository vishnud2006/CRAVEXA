package com.cravexa.domain.usecase;

import com.cravexa.domain.repository.SellerProductRepository;
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
public final class GetSellerKitchenProductsUseCase_Factory implements Factory<GetSellerKitchenProductsUseCase> {
  private final Provider<SellerProductRepository> productRepositoryProvider;

  public GetSellerKitchenProductsUseCase_Factory(
      Provider<SellerProductRepository> productRepositoryProvider) {
    this.productRepositoryProvider = productRepositoryProvider;
  }

  @Override
  public GetSellerKitchenProductsUseCase get() {
    return newInstance(productRepositoryProvider.get());
  }

  public static GetSellerKitchenProductsUseCase_Factory create(
      Provider<SellerProductRepository> productRepositoryProvider) {
    return new GetSellerKitchenProductsUseCase_Factory(productRepositoryProvider);
  }

  public static GetSellerKitchenProductsUseCase newInstance(
      SellerProductRepository productRepository) {
    return new GetSellerKitchenProductsUseCase(productRepository);
  }
}
