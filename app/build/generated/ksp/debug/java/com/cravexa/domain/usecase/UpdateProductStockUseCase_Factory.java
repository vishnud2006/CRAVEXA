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
public final class UpdateProductStockUseCase_Factory implements Factory<UpdateProductStockUseCase> {
  private final Provider<SellerProductRepository> productRepositoryProvider;

  public UpdateProductStockUseCase_Factory(
      Provider<SellerProductRepository> productRepositoryProvider) {
    this.productRepositoryProvider = productRepositoryProvider;
  }

  @Override
  public UpdateProductStockUseCase get() {
    return newInstance(productRepositoryProvider.get());
  }

  public static UpdateProductStockUseCase_Factory create(
      Provider<SellerProductRepository> productRepositoryProvider) {
    return new UpdateProductStockUseCase_Factory(productRepositoryProvider);
  }

  public static UpdateProductStockUseCase newInstance(SellerProductRepository productRepository) {
    return new UpdateProductStockUseCase(productRepository);
  }
}
