package com.cravexa.domain.usecase;

import com.cravexa.domain.repository.CustomerRepository;
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
public final class UpdateCustomerProfileUseCase_Factory implements Factory<UpdateCustomerProfileUseCase> {
  private final Provider<CustomerRepository> customerRepositoryProvider;

  public UpdateCustomerProfileUseCase_Factory(
      Provider<CustomerRepository> customerRepositoryProvider) {
    this.customerRepositoryProvider = customerRepositoryProvider;
  }

  @Override
  public UpdateCustomerProfileUseCase get() {
    return newInstance(customerRepositoryProvider.get());
  }

  public static UpdateCustomerProfileUseCase_Factory create(
      Provider<CustomerRepository> customerRepositoryProvider) {
    return new UpdateCustomerProfileUseCase_Factory(customerRepositoryProvider);
  }

  public static UpdateCustomerProfileUseCase newInstance(CustomerRepository customerRepository) {
    return new UpdateCustomerProfileUseCase(customerRepository);
  }
}
