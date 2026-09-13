package com.cravexa.presentation.admin.complaints;

import com.cravexa.domain.usecase.GetAdminComplaintsUseCase;
import com.cravexa.domain.usecase.UpdateComplaintStatusUseCase;
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
public final class AdminComplaintsViewModel_Factory implements Factory<AdminComplaintsViewModel> {
  private final Provider<GetAdminComplaintsUseCase> getAdminComplaintsUseCaseProvider;

  private final Provider<UpdateComplaintStatusUseCase> updateComplaintStatusUseCaseProvider;

  public AdminComplaintsViewModel_Factory(
      Provider<GetAdminComplaintsUseCase> getAdminComplaintsUseCaseProvider,
      Provider<UpdateComplaintStatusUseCase> updateComplaintStatusUseCaseProvider) {
    this.getAdminComplaintsUseCaseProvider = getAdminComplaintsUseCaseProvider;
    this.updateComplaintStatusUseCaseProvider = updateComplaintStatusUseCaseProvider;
  }

  @Override
  public AdminComplaintsViewModel get() {
    return newInstance(getAdminComplaintsUseCaseProvider.get(), updateComplaintStatusUseCaseProvider.get());
  }

  public static AdminComplaintsViewModel_Factory create(
      Provider<GetAdminComplaintsUseCase> getAdminComplaintsUseCaseProvider,
      Provider<UpdateComplaintStatusUseCase> updateComplaintStatusUseCaseProvider) {
    return new AdminComplaintsViewModel_Factory(getAdminComplaintsUseCaseProvider, updateComplaintStatusUseCaseProvider);
  }

  public static AdminComplaintsViewModel newInstance(
      GetAdminComplaintsUseCase getAdminComplaintsUseCase,
      UpdateComplaintStatusUseCase updateComplaintStatusUseCase) {
    return new AdminComplaintsViewModel(getAdminComplaintsUseCase, updateComplaintStatusUseCase);
  }
}
