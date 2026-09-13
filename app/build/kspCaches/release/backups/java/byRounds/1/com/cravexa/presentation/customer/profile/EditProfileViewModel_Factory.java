package com.cravexa.presentation.customer.profile;

import com.cravexa.domain.usecase.GetCustomerProfileUseCase;
import com.cravexa.domain.usecase.UpdateCustomerProfileUseCase;
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
public final class EditProfileViewModel_Factory implements Factory<EditProfileViewModel> {
  private final Provider<GetCustomerProfileUseCase> getCustomerProfileUseCaseProvider;

  private final Provider<UpdateCustomerProfileUseCase> updateCustomerProfileUseCaseProvider;

  public EditProfileViewModel_Factory(
      Provider<GetCustomerProfileUseCase> getCustomerProfileUseCaseProvider,
      Provider<UpdateCustomerProfileUseCase> updateCustomerProfileUseCaseProvider) {
    this.getCustomerProfileUseCaseProvider = getCustomerProfileUseCaseProvider;
    this.updateCustomerProfileUseCaseProvider = updateCustomerProfileUseCaseProvider;
  }

  @Override
  public EditProfileViewModel get() {
    return newInstance(getCustomerProfileUseCaseProvider.get(), updateCustomerProfileUseCaseProvider.get());
  }

  public static EditProfileViewModel_Factory create(
      Provider<GetCustomerProfileUseCase> getCustomerProfileUseCaseProvider,
      Provider<UpdateCustomerProfileUseCase> updateCustomerProfileUseCaseProvider) {
    return new EditProfileViewModel_Factory(getCustomerProfileUseCaseProvider, updateCustomerProfileUseCaseProvider);
  }

  public static EditProfileViewModel newInstance(
      GetCustomerProfileUseCase getCustomerProfileUseCase,
      UpdateCustomerProfileUseCase updateCustomerProfileUseCase) {
    return new EditProfileViewModel(getCustomerProfileUseCase, updateCustomerProfileUseCase);
  }
}
