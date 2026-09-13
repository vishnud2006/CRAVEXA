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
public final class UpdateSellerProductUseCase_Factory implements Factory<UpdateSellerProductUseCase> {
  private final Provider<SellerProductRepository> productRepositoryProvider;

  public UpdateSellerProductUseCase_Factory(
      Provider<SellerProductRepository> productRepositoryProvider) {
    this.productRepositoryProvider = productRepositoryProvider;
  }

  @Override
  public UpdateSellerProductUseCase get() {
    return newInstance(productRepositoryProvider.get());
  }

  public static UpdateSellerProductUseCase_Factory create(
      Provider<SellerProductRepository> productRepositoryProvider) {
    return new UpdateSellerProductUseCase_Factory(productRepositoryProvider);
  }

  public static UpdateSellerProductUseCase newInstance(SellerProductRepository productRepository) {
    return new UpdateSellerProductUseCase(productRepository);
  }
}
