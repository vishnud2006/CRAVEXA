package com.cravexa.domain.usecase;

import com.cravexa.domain.repository.AdminOrderManagementRepository;
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
public final class GetAdminOrdersUseCase_Factory implements Factory<GetAdminOrdersUseCase> {
  private final Provider<AdminOrderManagementRepository> repositoryProvider;

  public GetAdminOrdersUseCase_Factory(
      Provider<AdminOrderManagementRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public GetAdminOrdersUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static GetAdminOrdersUseCase_Factory create(
      Provider<AdminOrderManagementRepository> repositoryProvider) {
    return new GetAdminOrdersUseCase_Factory(repositoryProvider);
  }

  public static GetAdminOrdersUseCase newInstance(AdminOrderManagementRepository repository) {
    return new GetAdminOrdersUseCase(repository);
  }
}
