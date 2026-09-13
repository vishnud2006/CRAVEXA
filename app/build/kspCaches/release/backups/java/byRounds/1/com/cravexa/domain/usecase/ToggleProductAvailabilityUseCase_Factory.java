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
public final class ToggleProductAvailabilityUseCase_Factory implements Factory<ToggleProductAvailabilityUseCase> {
  private final Provider<SellerProductRepository> productRepositoryProvider;

  public ToggleProductAvailabilityUseCase_Factory(
      Provider<SellerProductRepository> productRepositoryProvider) {
    this.productRepositoryProvider = productRepositoryProvider;
  }

  @Override
  public ToggleProductAvailabilityUseCase get() {
    return newInstance(productRepositoryProvider.get());
  }

  public static ToggleProductAvailabilityUseCase_Factory create(
      Provider<SellerProductRepository> productRepositoryProvider) {
    return new ToggleProductAvailabilityUseCase_Factory(productRepositoryProvider);
  }

  public static ToggleProductAvailabilityUseCase newInstance(
      SellerProductRepository productRepository) {
    return new ToggleProductAvailabilityUseCase(productRepository);
  }
}
