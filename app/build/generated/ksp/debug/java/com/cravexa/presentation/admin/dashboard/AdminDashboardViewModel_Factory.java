package com.cravexa.presentation.admin.dashboard;

import com.cravexa.domain.usecase.GetAdminDashboardStatsUseCase;
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
public final class AdminDashboardViewModel_Factory implements Factory<AdminDashboardViewModel> {
  private final Provider<GetAdminDashboardStatsUseCase> getAdminDashboardStatsUseCaseProvider;

  public AdminDashboardViewModel_Factory(
      Provider<GetAdminDashboardStatsUseCase> getAdminDashboardStatsUseCaseProvider) {
    this.getAdminDashboardStatsUseCaseProvider = getAdminDashboardStatsUseCaseProvider;
  }

  @Override
  public AdminDashboardViewModel get() {
    return newInstance(getAdminDashboardStatsUseCaseProvider.get());
  }

  public static AdminDashboardViewModel_Factory create(
      Provider<GetAdminDashboardStatsUseCase> getAdminDashboardStatsUseCaseProvider) {
    return new AdminDashboardViewModel_Factory(getAdminDashboardStatsUseCaseProvider);
  }

  public static AdminDashboardViewModel newInstance(
      GetAdminDashboardStatsUseCase getAdminDashboardStatsUseCase) {
    return new AdminDashboardViewModel(getAdminDashboardStatsUseCase);
  }
}
