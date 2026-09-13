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
public final class CustomerRepositoryImpl_Factory implements Factory<CustomerRepositoryImpl> {
  private final Provider<PreferenceManager> preferenceManagerProvider;

  public CustomerRepositoryImpl_Factory(Provider<PreferenceManager> preferenceManagerProvider) {
    this.preferenceManagerProvider = preferenceManagerProvider;
  }

  @Override
  public CustomerRepositoryImpl get() {
    return newInstance(preferenceManagerProvider.get());
  }

  public static CustomerRepositoryImpl_Factory create(
      Provider<PreferenceManager> preferenceManagerProvider) {
    return new CustomerRepositoryImpl_Factory(preferenceManagerProvider);
  }

  public static CustomerRepositoryImpl newInstance(PreferenceManager preferenceManager) {
    return new CustomerRepositoryImpl(preferenceManager);
  }
}
