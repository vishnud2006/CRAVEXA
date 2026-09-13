package com.cravexa.presentation.admin.users;

import com.cravexa.domain.usecase.GetAdminUsersUseCase;
import com.cravexa.domain.usecase.UpdateUserStatusUseCase;
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
public final class AdminUsersViewModel_Factory implements Factory<AdminUsersViewModel> {
  private final Provider<GetAdminUsersUseCase> getAdminUsersUseCaseProvider;

  private final Provider<UpdateUserStatusUseCase> updateUserStatusUseCaseProvider;

  public AdminUsersViewModel_Factory(Provider<GetAdminUsersUseCase> getAdminUsersUseCaseProvider,
      Provider<UpdateUserStatusUseCase> updateUserStatusUseCaseProvider) {
    this.getAdminUsersUseCaseProvider = getAdminUsersUseCaseProvider;
    this.updateUserStatusUseCaseProvider = updateUserStatusUseCaseProvider;
  }

  @Override
  public AdminUsersViewModel get() {
    return newInstance(getAdminUsersUseCaseProvider.get(), updateUserStatusUseCaseProvider.get());
  }

  public static AdminUsersViewModel_Factory create(
      Provider<GetAdminUsersUseCase> getAdminUsersUseCaseProvider,
      Provider<UpdateUserStatusUseCase> updateUserStatusUseCaseProvider) {
    return new AdminUsersViewModel_Factory(getAdminUsersUseCaseProvider, updateUserStatusUseCaseProvider);
  }

  public static AdminUsersViewModel newInstance(GetAdminUsersUseCase getAdminUsersUseCase,
      UpdateUserStatusUseCase updateUserStatusUseCase) {
    return new AdminUsersViewModel(getAdminUsersUseCase, updateUserStatusUseCase);
  }
}
