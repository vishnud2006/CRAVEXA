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
public final class AddAdminCategoryUseCase_Factory implements Factory<AddAdminCategoryUseCase> {
  private final Provider<AdminMarketplaceRepository> repositoryProvider;

  public AddAdminCategoryUseCase_Factory(Provider<AdminMarketplaceRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public AddAdminCategoryUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static AddAdminCategoryUseCase_Factory create(
      Provider<AdminMarketplaceRepository> repositoryProvider) {
    return new AddAdminCategoryUseCase_Factory(repositoryProvider);
  }

  public static AddAdminCategoryUseCase newInstance(AdminMarketplaceRepository repository) {
    return new AddAdminCategoryUseCase(repository);
  }
}
