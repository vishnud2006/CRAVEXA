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
public final class GetProductDetailUseCase_Factory implements Factory<GetProductDetailUseCase> {
  private final Provider<ProductRepository> productRepositoryProvider;

  public GetProductDetailUseCase_Factory(Provider<ProductRepository> productRepositoryProvider) {
    this.productRepositoryProvider = productRepositoryProvider;
  }

  @Override
  public GetProductDetailUseCase get() {
    return newInstance(productRepositoryProvider.get());
  }

  public static GetProductDetailUseCase_Factory create(
      Provider<ProductRepository> productRepositoryProvider) {
    return new GetProductDetailUseCase_Factory(productRepositoryProvider);
  }

  public static GetProductDetailUseCase newInstance(ProductRepository productRepository) {
    return new GetProductDetailUseCase(productRepository);
  }
}
