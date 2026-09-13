package com.cravexa.presentation.admin.settings;

import com.cravexa.domain.usecase.GetAdminSettingsUseCase;
import com.cravexa.domain.usecase.GetCurrentUserUseCase;
import com.cravexa.domain.usecase.LogoutUseCase;
import com.cravexa.domain.usecase.UpdateAdminSettingsUseCase;
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
public final class AdminSettingsViewModel_Factory implements Factory<AdminSettingsViewModel> {
  private final Provider<GetAdminSettingsUseCase> getAdminSettingsUseCaseProvider;

  private final Provider<UpdateAdminSettingsUseCase> updateAdminSettingsUseCaseProvider;

  private final Provider<GetCurrentUserUseCase> getCurrentUserUseCaseProvider;

  private final Provider<LogoutUseCase> logoutUseCaseProvider;

  public AdminSettingsViewModel_Factory(
      Provider<GetAdminSettingsUseCase> getAdminSettingsUseCaseProvider,
      Provider<UpdateAdminSettingsUseCase> updateAdminSettingsUseCaseProvider,
      Provider<GetCurrentUserUseCase> getCurrentUserUseCaseProvider,
      Provider<LogoutUseCase> logoutUseCaseProvider) {
    this.getAdminSettingsUseCaseProvider = getAdminSettingsUseCaseProvider;
    this.updateAdminSettingsUseCaseProvider = updateAdminSettingsUseCaseProvider;
    this.getCurrentUserUseCaseProvider = getCurrentUserUseCaseProvider;
    this.logoutUseCaseProvider = logoutUseCaseProvider;
  }

  @Override
  public AdminSettingsViewModel get() {
    return newInstance(getAdminSettingsUseCaseProvider.get(), updateAdminSettingsUseCaseProvider.get(), getCurrentUserUseCaseProvider.get(), logoutUseCaseProvider.get());
  }

  public static AdminSettingsViewModel_Factory create(
      Provider<GetAdminSettingsUseCase> getAdminSettingsUseCaseProvider,
      Provider<UpdateAdminSettingsUseCase> updateAdminSettingsUseCaseProvider,
      Provider<GetCurrentUserUseCase> getCurrentUserUseCaseProvider,
      Provider<LogoutUseCase> logoutUseCaseProvider) {
    return new AdminSettingsViewModel_Factory(getAdminSettingsUseCaseProvider, updateAdminSettingsUseCaseProvider, getCurrentUserUseCaseProvider, logoutUseCaseProvider);
  }

  public static AdminSettingsViewModel newInstance(GetAdminSettingsUseCase getAdminSettingsUseCase,
      UpdateAdminSettingsUseCase updateAdminSettingsUseCase,
      GetCurrentUserUseCase getCurrentUserUseCase, LogoutUseCase logoutUseCase) {
    return new AdminSettingsViewModel(getAdminSettingsUseCase, updateAdminSettingsUseCase, getCurrentUserUseCase, logoutUseCase);
  }
}
