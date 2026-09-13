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
public final class GetAddressesUseCase_Factory implements Factory<GetAddressesUseCase> {
  private final Provider<AddressRepository> addressRepositoryProvider;

  public GetAddressesUseCase_Factory(Provider<AddressRepository> addressRepositoryProvider) {
    this.addressRepositoryProvider = addressRepositoryProvider;
  }

  @Override
  public GetAddressesUseCase get() {
    return newInstance(addressRepositoryProvider.get());
  }

  public static GetAddressesUseCase_Factory create(
      Provider<AddressRepository> addressRepositoryProvider) {
    return new GetAddressesUseCase_Factory(addressRepositoryProvider);
  }

  public static GetAddressesUseCase newInstance(AddressRepository addressRepository) {
    return new GetAddressesUseCase(addressRepository);
  }
}
