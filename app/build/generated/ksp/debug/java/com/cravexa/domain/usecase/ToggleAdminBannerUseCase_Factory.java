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
public final class ToggleAdminBannerUseCase_Factory implements Factory<ToggleAdminBannerUseCase> {
  private final Provider<AdminMarketplaceRepository> repositoryProvider;

  public ToggleAdminBannerUseCase_Factory(Provider<AdminMarketplaceRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public ToggleAdminBannerUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static ToggleAdminBannerUseCase_Factory create(
      Provider<AdminMarketplaceRepository> repositoryProvider) {
    return new ToggleAdminBannerUseCase_Factory(repositoryProvider);
  }

  public static ToggleAdminBannerUseCase newInstance(AdminMarketplaceRepository repository) {
    return new ToggleAdminBannerUseCase(repository);
  }
}
