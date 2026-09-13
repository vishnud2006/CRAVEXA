package com.cravexa.presentation.admin.password;

import com.cravexa.domain.usecase.AdminChangePasswordUseCase;
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
public final class AdminChangePasswordViewModel_Factory implements Factory<AdminChangePasswordViewModel> {
  private final Provider<AdminChangePasswordUseCase> adminChangePasswordUseCaseProvider;

  public AdminChangePasswordViewModel_Factory(
      Provider<AdminChangePasswordUseCase> adminChangePasswordUseCaseProvider) {
    this.adminChangePasswordUseCaseProvider = adminChangePasswordUseCaseProvider;
  }

  @Override
  public AdminChangePasswordViewModel get() {
    return newInstance(adminChangePasswordUseCaseProvider.get());
  }

  public static AdminChangePasswordViewModel_Factory create(
      Provider<AdminChangePasswordUseCase> adminChangePasswordUseCaseProvider) {
    return new AdminChangePasswordViewModel_Factory(adminChangePasswordUseCaseProvider);
  }

  public static AdminChangePasswordViewModel newInstance(
      AdminChangePasswordUseCase adminChangePasswordUseCase) {
    return new AdminChangePasswordViewModel(adminChangePasswordUseCase);
  }
}
