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
public final class ToggleAdminCouponUseCase_Factory implements Factory<ToggleAdminCouponUseCase> {
  private final Provider<AdminMarketplaceRepository> repositoryProvider;

  public ToggleAdminCouponUseCase_Factory(Provider<AdminMarketplaceRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public ToggleAdminCouponUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static ToggleAdminCouponUseCase_Factory create(
      Provider<AdminMarketplaceRepository> repositoryProvider) {
    return new ToggleAdminCouponUseCase_Factory(repositoryProvider);
  }

  public static ToggleAdminCouponUseCase newInstance(AdminMarketplaceRepository repository) {
    return new ToggleAdminCouponUseCase(repository);
  }
}
