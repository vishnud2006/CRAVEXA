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
public final class AdminChangePasswordUseCase_Factory implements Factory<AdminChangePasswordUseCase> {
  private final Provider<AdminSettingsRepository> repositoryProvider;

  public AdminChangePasswordUseCase_Factory(Provider<AdminSettingsRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public AdminChangePasswordUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static AdminChangePasswordUseCase_Factory create(
      Provider<AdminSettingsRepository> repositoryProvider) {
    return new AdminChangePasswordUseCase_Factory(repositoryProvider);
  }

  public static AdminChangePasswordUseCase newInstance(AdminSettingsRepository repository) {
    return new AdminChangePasswordUseCase(repository);
  }
}
