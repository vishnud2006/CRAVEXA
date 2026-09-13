package com.cravexa.presentation.customer.address;

import com.cravexa.domain.usecase.AddAddressUseCase;
import com.cravexa.domain.usecase.DeleteAddressUseCase;
import com.cravexa.domain.usecase.GetAddressesUseCase;
import com.cravexa.domain.usecase.SetDefaultAddressUseCase;
import com.cravexa.domain.usecase.UpdateAddressUseCase;
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
public final class AddressViewModel_Factory implements Factory<AddressViewModel> {
  private final Provider<GetAddressesUseCase> getAddressesUseCaseProvider;

  private final Provider<AddAddressUseCase> addAddressUseCaseProvider;

  private final Provider<UpdateAddressUseCase> updateAddressUseCaseProvider;

  private final Provider<DeleteAddressUseCase> deleteAddressUseCaseProvider;

  private final Provider<SetDefaultAddressUseCase> setDefaultAddressUseCaseProvider;

  public AddressViewModel_Factory(Provider<GetAddressesUseCase> getAddressesUseCaseProvider,
      Provider<AddAddressUseCase> addAddressUseCaseProvider,
      Provider<UpdateAddressUseCase> updateAddressUseCaseProvider,
      Provider<DeleteAddressUseCase> deleteAddressUseCaseProvider,
      Provider<SetDefaultAddressUseCase> setDefaultAddressUseCaseProvider) {
    this.getAddressesUseCaseProvider = getAddressesUseCaseProvider;
    this.addAddressUseCaseProvider = addAddressUseCaseProvider;
    this.updateAddressUseCaseProvider = updateAddressUseCaseProvider;
    this.deleteAddressUseCaseProvider = deleteAddressUseCaseProvider;
    this.setDefaultAddressUseCaseProvider = setDefaultAddressUseCaseProvider;
  }

  @Override
  public AddressViewModel get() {
    return newInstance(getAddressesUseCaseProvider.get(), addAddressUseCaseProvider.get(), updateAddressUseCaseProvider.get(), deleteAddressUseCaseProvider.get(), setDefaultAddressUseCaseProvider.get());
  }

  public static AddressViewModel_Factory create(
      Provider<GetAddressesUseCase> getAddressesUseCaseProvider,
      Provider<AddAddressUseCase> addAddressUseCaseProvider,
      Provider<UpdateAddressUseCase> updateAddressUseCaseProvider,
      Provider<DeleteAddressUseCase> deleteAddressUseCaseProvider,
      Provider<SetDefaultAddressUseCase> setDefaultAddressUseCaseProvider) {
    return new AddressViewModel_Factory(getAddressesUseCaseProvider, addAddressUseCaseProvider, updateAddressUseCaseProvider, deleteAddressUseCaseProvider, setDefaultAddressUseCaseProvider);
  }

  public static AddressViewModel newInstance(GetAddressesUseCase getAddressesUseCase,
      AddAddressUseCase addAddressUseCase, UpdateAddressUseCase updateAddressUseCase,
      DeleteAddressUseCase deleteAddressUseCase,
      SetDefaultAddressUseCase setDefaultAddressUseCase) {
    return new AddressViewModel(getAddressesUseCase, addAddressUseCase, updateAddressUseCase, deleteAddressUseCase, setDefaultAddressUseCase);
  }
}
