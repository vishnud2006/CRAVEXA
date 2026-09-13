package com.cravexa.domain.usecase;

import com.cravexa.domain.repository.AdminUserRepository;
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
public final class GetAdminUsersUseCase_Factory implements Factory<GetAdminUsersUseCase> {
  private final Provider<AdminUserRepository> repositoryProvider;

  public GetAdminUsersUseCase_Factory(Provider<AdminUserRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public GetAdminUsersUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static GetAdminUsersUseCase_Factory create(
      Provider<AdminUserRepository> repositoryProvider) {
    return new GetAdminUsersUseCase_Factory(repositoryProvider);
  }

  public static GetAdminUsersUseCase newInstance(AdminUserRepository repository) {
    return new GetAdminUsersUseCase(repository);
  }
}
