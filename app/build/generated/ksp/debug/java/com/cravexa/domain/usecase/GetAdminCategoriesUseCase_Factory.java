package com.cravexa.domain.usecase;

import com.cravexa.domain.repository.AdminMarketplaceRepository;
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
public final class GetAdminCategoriesUseCase_Factory implements Factory<GetAdminCategoriesUseCase> {
  private final Provider<AdminMarketplaceRepository> repositoryProvider;

  public GetAdminCategoriesUseCase_Factory(
      Provider<AdminMarketplaceRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public GetAdminCategoriesUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static GetAdminCategoriesUseCase_Factory create(
      Provider<AdminMarketplaceRepository> repositoryProvider) {
    return new GetAdminCategoriesUseCase_Factory(repositoryProvider);
  }

  public static GetAdminCategoriesUseCase newInstance(AdminMarketplaceRepository repository) {
    return new GetAdminCategoriesUseCase(repository);
  }
}
