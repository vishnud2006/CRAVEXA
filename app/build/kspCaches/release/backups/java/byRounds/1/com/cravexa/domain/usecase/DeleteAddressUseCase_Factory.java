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
public final class DeleteAddressUseCase_Factory implements Factory<DeleteAddressUseCase> {
  private final Provider<AddressRepository> addressRepositoryProvider;

  public DeleteAddressUseCase_Factory(Provider<AddressRepository> addressRepositoryProvider) {
    this.addressRepositoryProvider = addressRepositoryProvider;
  }

  @Override
  public DeleteAddressUseCase get() {
    return newInstance(addressRepositoryProvider.get());
  }

  public static DeleteAddressUseCase_Factory create(
      Provider<AddressRepository> addressRepositoryProvider) {
    return new DeleteAddressUseCase_Factory(addressRepositoryProvider);
  }

  public static DeleteAddressUseCase newInstance(AddressRepository addressRepository) {
    return new DeleteAddressUseCase(addressRepository);
  }
}
