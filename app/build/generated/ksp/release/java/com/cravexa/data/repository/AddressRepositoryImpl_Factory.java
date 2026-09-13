package com.cravexa.data.repository;

import com.cravexa.data.local.PreferenceManager;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class AddressRepositoryImpl_Factory implements Factory<AddressRepositoryImpl> {
  private final Provider<PreferenceManager> preferenceManagerProvider;

  public AddressRepositoryImpl_Factory(Provider<PreferenceManager> preferenceManagerProvider) {
    this.preferenceManagerProvider = preferenceManagerProvider;
  }

  @Override
  public AddressRepositoryImpl get() {
    return newInstance(preferenceManagerProvider.get());
  }

  public static AddressRepositoryImpl_Factory create(
      Provider<PreferenceManager> preferenceManagerProvider) {
    return new AddressRepositoryImpl_Factory(preferenceManagerProvider);
  }

  public static AddressRepositoryImpl newInstance(PreferenceManager preferenceManager) {
    return new AddressRepositoryImpl(preferenceManager);
  }
}
