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
public final class GetCustomerProfileUseCase_Factory implements Factory<GetCustomerProfileUseCase> {
  private final Provider<CustomerRepository> customerRepositoryProvider;

  public GetCustomerProfileUseCase_Factory(
      Provider<CustomerRepository> customerRepositoryProvider) {
    this.customerRepositoryProvider = customerRepositoryProvider;
  }

  @Override
  public GetCustomerProfileUseCase get() {
    return newInstance(customerRepositoryProvider.get());
  }

  public static GetCustomerProfileUseCase_Factory create(
      Provider<CustomerRepository> customerRepositoryProvider) {
    return new GetCustomerProfileUseCase_Factory(customerRepositoryProvider);
  }

  public static GetCustomerProfileUseCase newInstance(CustomerRepository customerRepository) {
    return new GetCustomerProfileUseCase(customerRepository);
  }
}
