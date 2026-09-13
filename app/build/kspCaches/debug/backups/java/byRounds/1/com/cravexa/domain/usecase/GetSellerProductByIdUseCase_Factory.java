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
public final class GetSellerProductByIdUseCase_Factory implements Factory<GetSellerProductByIdUseCase> {
  private final Provider<SellerProductRepository> productRepositoryProvider;

  public GetSellerProductByIdUseCase_Factory(
      Provider<SellerProductRepository> productRepositoryProvider) {
    this.productRepositoryProvider = productRepositoryProvider;
  }

  @Override
  public GetSellerProductByIdUseCase get() {
    return newInstance(productRepositoryProvider.get());
  }

  public static GetSellerProductByIdUseCase_Factory create(
      Provider<SellerProductRepository> productRepositoryProvider) {
    return new GetSellerProductByIdUseCase_Factory(productRepositoryProvider);
  }

  public static GetSellerProductByIdUseCase newInstance(SellerProductRepository productRepository) {
    return new GetSellerProductByIdUseCase(productRepository);
  }
}
