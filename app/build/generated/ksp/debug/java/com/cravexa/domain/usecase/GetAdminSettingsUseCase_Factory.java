package com.cravexa.domain.usecase;

import com.cravexa.domain.repository.AdminSettingsRepository;
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
public final class GetAdminSettingsUseCase_Factory implements Factory<GetAdminSettingsUseCase> {
  private final Provider<AdminSettingsRepository> repositoryProvider;

  public GetAdminSettingsUseCase_Factory(Provider<AdminSettingsRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public GetAdminSettingsUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static GetAdminSettingsUseCase_Factory create(
      Provider<AdminSettingsRepository> repositoryProvider) {
    return new GetAdminSettingsUseCase_Factory(repositoryProvider);
  }

  public static GetAdminSettingsUseCase newInstance(AdminSettingsRepository repository) {
    return new GetAdminSettingsUseCase(repository);
  }
}
