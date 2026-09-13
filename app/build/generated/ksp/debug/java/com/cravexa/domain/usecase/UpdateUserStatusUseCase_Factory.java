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
public final class UpdateUserStatusUseCase_Factory implements Factory<UpdateUserStatusUseCase> {
  private final Provider<AdminUserRepository> repositoryProvider;

  public UpdateUserStatusUseCase_Factory(Provider<AdminUserRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public UpdateUserStatusUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static UpdateUserStatusUseCase_Factory create(
      Provider<AdminUserRepository> repositoryProvider) {
    return new UpdateUserStatusUseCase_Factory(repositoryProvider);
  }

  public static UpdateUserStatusUseCase newInstance(AdminUserRepository repository) {
    return new UpdateUserStatusUseCase(repository);
  }
}
