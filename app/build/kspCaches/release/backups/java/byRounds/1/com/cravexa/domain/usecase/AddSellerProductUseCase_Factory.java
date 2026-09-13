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
public final class AddSellerProductUseCase_Factory implements Factory<AddSellerProductUseCase> {
  private final Provider<SellerProductRepository> productRepositoryProvider;

  public AddSellerProductUseCase_Factory(
      Provider<SellerProductRepository> productRepositoryProvider) {
    this.productRepositoryProvider = productRepositoryProvider;
  }

  @Override
  public AddSellerProductUseCase get() {
    return newInstance(productRepositoryProvider.get());
  }

  public static AddSellerProductUseCase_Factory create(
      Provider<SellerProductRepository> productRepositoryProvider) {
    return new AddSellerProductUseCase_Factory(productRepositoryProvider);
  }

  public static AddSellerProductUseCase newInstance(SellerProductRepository productRepository) {
    return new AddSellerProductUseCase(productRepository);
  }
}
