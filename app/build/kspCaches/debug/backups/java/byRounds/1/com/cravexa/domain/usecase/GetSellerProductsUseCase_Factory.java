package com.cravexa.domain.usecase;

import com.cravexa.domain.repository.ProductRepository;
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
public final class GetSellerProductsUseCase_Factory implements Factory<GetSellerProductsUseCase> {
  private final Provider<ProductRepository> productRepositoryProvider;

  public GetSellerProductsUseCase_Factory(Provider<ProductRepository> productRepositoryProvider) {
    this.productRepositoryProvider = productRepositoryProvider;
  }

  @Override
  public GetSellerProductsUseCase get() {
    return newInstance(productRepositoryProvider.get());
  }

  public static GetSellerProductsUseCase_Factory create(
      Provider<ProductRepository> productRepositoryProvider) {
    return new GetSellerProductsUseCase_Factory(productRepositoryProvider);
  }

  public static GetSellerProductsUseCase newInstance(ProductRepository productRepository) {
    return new GetSellerProductsUseCase(productRepository);
  }
}
