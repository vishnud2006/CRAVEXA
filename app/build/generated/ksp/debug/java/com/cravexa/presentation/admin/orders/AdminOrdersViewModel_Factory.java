package com.cravexa.presentation.admin.orders;

import com.cravexa.domain.usecase.GetAdminOrdersUseCase;
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
public final class AdminOrdersViewModel_Factory implements Factory<AdminOrdersViewModel> {
  private final Provider<GetAdminOrdersUseCase> getAdminOrdersUseCaseProvider;

  public AdminOrdersViewModel_Factory(
      Provider<GetAdminOrdersUseCase> getAdminOrdersUseCaseProvider) {
    this.getAdminOrdersUseCaseProvider = getAdminOrdersUseCaseProvider;
  }

  @Override
  public AdminOrdersViewModel get() {
    return newInstance(getAdminOrdersUseCaseProvider.get());
  }

  public static AdminOrdersViewModel_Factory create(
      Provider<GetAdminOrdersUseCase> getAdminOrdersUseCaseProvider) {
    return new AdminOrdersViewModel_Factory(getAdminOrdersUseCaseProvider);
  }

  public static AdminOrdersViewModel newInstance(GetAdminOrdersUseCase getAdminOrdersUseCase) {
    return new AdminOrdersViewModel(getAdminOrdersUseCase);
  }
}
