package com.cravexa.presentation.customer.profile;

import com.cravexa.domain.usecase.GetCustomerProfileUseCase;
import com.cravexa.domain.usecase.LogoutUseCase;
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
public final class CustomerProfileViewModel_Factory implements Factory<CustomerProfileViewModel> {
  private final Provider<GetCustomerProfileUseCase> getCustomerProfileUseCaseProvider;

  private final Provider<LogoutUseCase> logoutUseCaseProvider;

  public CustomerProfileViewModel_Factory(
      Provider<GetCustomerProfileUseCase> getCustomerProfileUseCaseProvider,
      Provider<LogoutUseCase> logoutUseCaseProvider) {
    this.getCustomerProfileUseCaseProvider = getCustomerProfileUseCaseProvider;
    this.logoutUseCaseProvider = logoutUseCaseProvider;
  }

  @Override
  public CustomerProfileViewModel get() {
    return newInstance(getCustomerProfileUseCaseProvider.get(), logoutUseCaseProvider.get());
  }

  public static CustomerProfileViewModel_Factory create(
      Provider<GetCustomerProfileUseCase> getCustomerProfileUseCaseProvider,
      Provider<LogoutUseCase> logoutUseCaseProvider) {
    return new CustomerProfileViewModel_Factory(getCustomerProfileUseCaseProvider, logoutUseCaseProvider);
  }

  public static CustomerProfileViewModel newInstance(
      GetCustomerProfileUseCase getCustomerProfileUseCase, LogoutUseCase logoutUseCase) {
    return new CustomerProfileViewModel(getCustomerProfileUseCase, logoutUseCase);
  }
}
