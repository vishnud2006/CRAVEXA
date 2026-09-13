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
public final class SetDefaultAddressUseCase_Factory implements Factory<SetDefaultAddressUseCase> {
  private final Provider<AddressRepository> addressRepositoryProvider;

  public SetDefaultAddressUseCase_Factory(Provider<AddressRepository> addressRepositoryProvider) {
    this.addressRepositoryProvider = addressRepositoryProvider;
  }

  @Override
  public SetDefaultAddressUseCase get() {
    return newInstance(addressRepositoryProvider.get());
  }

  public static SetDefaultAddressUseCase_Factory create(
      Provider<AddressRepository> addressRepositoryProvider) {
    return new SetDefaultAddressUseCase_Factory(addressRepositoryProvider);
  }

  public static SetDefaultAddressUseCase newInstance(AddressRepository addressRepository) {
    return new SetDefaultAddressUseCase(addressRepository);
  }
}
