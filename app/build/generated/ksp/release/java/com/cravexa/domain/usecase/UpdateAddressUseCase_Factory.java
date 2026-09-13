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
public final class UpdateAddressUseCase_Factory implements Factory<UpdateAddressUseCase> {
  private final Provider<AddressRepository> addressRepositoryProvider;

  public UpdateAddressUseCase_Factory(Provider<AddressRepository> addressRepositoryProvider) {
    this.addressRepositoryProvider = addressRepositoryProvider;
  }

  @Override
  public UpdateAddressUseCase get() {
    return newInstance(addressRepositoryProvider.get());
  }

  public static UpdateAddressUseCase_Factory create(
      Provider<AddressRepository> addressRepositoryProvider) {
    return new UpdateAddressUseCase_Factory(addressRepositoryProvider);
  }

  public static UpdateAddressUseCase newInstance(AddressRepository addressRepository) {
    return new UpdateAddressUseCase(addressRepository);
  }
}
