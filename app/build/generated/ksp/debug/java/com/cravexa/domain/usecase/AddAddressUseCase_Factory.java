package com.cravexa.domain.usecase;

import com.cravexa.domain.repository.AddressRepository;
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
public final class AddAddressUseCase_Factory implements Factory<AddAddressUseCase> {
  private final Provider<AddressRepository> addressRepositoryProvider;

  public AddAddressUseCase_Factory(Provider<AddressRepository> addressRepositoryProvider) {
    this.addressRepositoryProvider = addressRepositoryProvider;
  }

  @Override
  public AddAddressUseCase get() {
    return newInstance(addressRepositoryProvider.get());
  }

  public static AddAddressUseCase_Factory create(
      Provider<AddressRepository> addressRepositoryProvider) {
    return new AddAddressUseCase_Factory(addressRepositoryProvider);
  }

  public static AddAddressUseCase newInstance(AddressRepository addressRepository) {
    return new AddAddressUseCase(addressRepository);
  }
}
