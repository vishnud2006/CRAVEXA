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
public final class CheckAdminMustChangePasswordUseCase_Factory implements Factory<CheckAdminMustChangePasswordUseCase> {
  private final Provider<AdminSettingsRepository> repositoryProvider;

  public CheckAdminMustChangePasswordUseCase_Factory(
      Provider<AdminSettingsRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public CheckAdminMustChangePasswordUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static CheckAdminMustChangePasswordUseCase_Factory create(
      Provider<AdminSettingsRepository> repositoryProvider) {
    return new CheckAdminMustChangePasswordUseCase_Factory(repositoryProvider);
  }

  public static CheckAdminMustChangePasswordUseCase newInstance(
      AdminSettingsRepository repository) {
    return new CheckAdminMustChangePasswordUseCase(repository);
  }
}
