package com.cravexa.presentation.admin.login;

import com.cravexa.domain.usecase.CheckAdminMustChangePasswordUseCase;
import com.cravexa.domain.usecase.LoginUseCase;
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
public final class AdminLoginViewModel_Factory implements Factory<AdminLoginViewModel> {
  private final Provider<LoginUseCase> loginUseCaseProvider;

  private final Provider<CheckAdminMustChangePasswordUseCase> checkAdminMustChangePasswordUseCaseProvider;

  public AdminLoginViewModel_Factory(Provider<LoginUseCase> loginUseCaseProvider,
      Provider<CheckAdminMustChangePasswordUseCase> checkAdminMustChangePasswordUseCaseProvider) {
    this.loginUseCaseProvider = loginUseCaseProvider;
    this.checkAdminMustChangePasswordUseCaseProvider = checkAdminMustChangePasswordUseCaseProvider;
  }

  @Override
  public AdminLoginViewModel get() {
    return newInstance(loginUseCaseProvider.get(), checkAdminMustChangePasswordUseCaseProvider.get());
  }

  public static AdminLoginViewModel_Factory create(Provider<LoginUseCase> loginUseCaseProvider,
      Provider<CheckAdminMustChangePasswordUseCase> checkAdminMustChangePasswordUseCaseProvider) {
    return new AdminLoginViewModel_Factory(loginUseCaseProvider, checkAdminMustChangePasswordUseCaseProvider);
  }

  public static AdminLoginViewModel newInstance(LoginUseCase loginUseCase,
      CheckAdminMustChangePasswordUseCase checkAdminMustChangePasswordUseCase) {
    return new AdminLoginViewModel(loginUseCase, checkAdminMustChangePasswordUseCase);
  }
}
