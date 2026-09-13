package com.cravexa.domain.usecase;

import com.cravexa.domain.repository.CategoryRepository;
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
public final class GetHomeMarketplaceUseCase_Factory implements Factory<GetHomeMarketplaceUseCase> {
  private final Provider<ProductRepository> productRepositoryProvider;

  private final Provider<CategoryRepository> categoryRepositoryProvider;

  public GetHomeMarketplaceUseCase_Factory(Provider<ProductRepository> productRepositoryProvider,
      Provider<CategoryRepository> categoryRepositoryProvider) {
    this.productRepositoryProvider = productRepositoryProvider;
    this.categoryRepositoryProvider = categoryRepositoryProvider;
  }

  @Override
  public GetHomeMarketplaceUseCase get() {
    return newInstance(productRepositoryProvider.get(), categoryRepositoryProvider.get());
  }

  public static GetHomeMarketplaceUseCase_Factory create(
      Provider<ProductRepository> productRepositoryProvider,
      Provider<CategoryRepository> categoryRepositoryProvider) {
    return new GetHomeMarketplaceUseCase_Factory(productRepositoryProvider, categoryRepositoryProvider);
  }

  public static GetHomeMarketplaceUseCase newInstance(ProductRepository productRepository,
      CategoryRepository categoryRepository) {
    return new GetHomeMarketplaceUseCase(productRepository, categoryRepository);
  }
}
