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
public final class DeleteSellerProductUseCase_Factory implements Factory<DeleteSellerProductUseCase> {
  private final Provider<SellerProductRepository> productRepositoryProvider;

  public DeleteSellerProductUseCase_Factory(
      Provider<SellerProductRepository> productRepositoryProvider) {
    this.productRepositoryProvider = productRepositoryProvider;
  }

  @Override
  public DeleteSellerProductUseCase get() {
    return newInstance(productRepositoryProvider.get());
  }

  public static DeleteSellerProductUseCase_Factory create(
      Provider<SellerProductRepository> productRepositoryProvider) {
    return new DeleteSellerProductUseCase_Factory(productRepositoryProvider);
  }

  public static DeleteSellerProductUseCase newInstance(SellerProductRepository productRepository) {
    return new DeleteSellerProductUseCase(productRepository);
  }
}
