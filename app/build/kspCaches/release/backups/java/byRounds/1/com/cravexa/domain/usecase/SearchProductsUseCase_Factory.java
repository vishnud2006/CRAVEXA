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
public final class SearchProductsUseCase_Factory implements Factory<SearchProductsUseCase> {
  private final Provider<ProductRepository> productRepositoryProvider;

  public SearchProductsUseCase_Factory(Provider<ProductRepository> productRepositoryProvider) {
    this.productRepositoryProvider = productRepositoryProvider;
  }

  @Override
  public SearchProductsUseCase get() {
    return newInstance(productRepositoryProvider.get());
  }

  public static SearchProductsUseCase_Factory create(
      Provider<ProductRepository> productRepositoryProvider) {
    return new SearchProductsUseCase_Factory(productRepositoryProvider);
  }

  public static SearchProductsUseCase newInstance(ProductRepository productRepository) {
    return new SearchProductsUseCase(productRepository);
  }
}
