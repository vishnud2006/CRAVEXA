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
public final class GetCategoryProductsUseCase_Factory implements Factory<GetCategoryProductsUseCase> {
  private final Provider<ProductRepository> productRepositoryProvider;

  public GetCategoryProductsUseCase_Factory(Provider<ProductRepository> productRepositoryProvider) {
    this.productRepositoryProvider = productRepositoryProvider;
  }

  @Override
  public GetCategoryProductsUseCase get() {
    return newInstance(productRepositoryProvider.get());
  }

  public static GetCategoryProductsUseCase_Factory create(
      Provider<ProductRepository> productRepositoryProvider) {
    return new GetCategoryProductsUseCase_Factory(productRepositoryProvider);
  }

  public static GetCategoryProductsUseCase newInstance(ProductRepository productRepository) {
    return new GetCategoryProductsUseCase(productRepository);
  }
}
