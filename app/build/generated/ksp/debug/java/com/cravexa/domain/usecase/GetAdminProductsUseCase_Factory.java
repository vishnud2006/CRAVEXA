package com.cravexa.domain.usecase;

import com.cravexa.domain.repository.AdminProductModerationRepository;
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
public final class GetAdminProductsUseCase_Factory implements Factory<GetAdminProductsUseCase> {
  private final Provider<AdminProductModerationRepository> repositoryProvider;

  public GetAdminProductsUseCase_Factory(
      Provider<AdminProductModerationRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public GetAdminProductsUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static GetAdminProductsUseCase_Factory create(
      Provider<AdminProductModerationRepository> repositoryProvider) {
    return new GetAdminProductsUseCase_Factory(repositoryProvider);
  }

  public static GetAdminProductsUseCase newInstance(AdminProductModerationRepository repository) {
    return new GetAdminProductsUseCase(repository);
  }
}
