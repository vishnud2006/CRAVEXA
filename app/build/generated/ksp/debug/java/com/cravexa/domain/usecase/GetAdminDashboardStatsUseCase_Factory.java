package com.cravexa.domain.usecase;

import com.cravexa.domain.repository.AdminDashboardRepository;
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
public final class GetAdminDashboardStatsUseCase_Factory implements Factory<GetAdminDashboardStatsUseCase> {
  private final Provider<AdminDashboardRepository> repositoryProvider;

  public GetAdminDashboardStatsUseCase_Factory(
      Provider<AdminDashboardRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public GetAdminDashboardStatsUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static GetAdminDashboardStatsUseCase_Factory create(
      Provider<AdminDashboardRepository> repositoryProvider) {
    return new GetAdminDashboardStatsUseCase_Factory(repositoryProvider);
  }

  public static GetAdminDashboardStatsUseCase newInstance(AdminDashboardRepository repository) {
    return new GetAdminDashboardStatsUseCase(repository);
  }
}
