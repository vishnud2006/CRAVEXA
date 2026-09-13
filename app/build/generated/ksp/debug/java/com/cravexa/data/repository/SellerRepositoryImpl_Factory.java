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
public final class SellerRepositoryImpl_Factory implements Factory<SellerRepositoryImpl> {
  private final Provider<PreferenceManager> preferenceManagerProvider;

  public SellerRepositoryImpl_Factory(Provider<PreferenceManager> preferenceManagerProvider) {
    this.preferenceManagerProvider = preferenceManagerProvider;
  }

  @Override
  public SellerRepositoryImpl get() {
    return newInstance(preferenceManagerProvider.get());
  }

  public static SellerRepositoryImpl_Factory create(
      Provider<PreferenceManager> preferenceManagerProvider) {
    return new SellerRepositoryImpl_Factory(preferenceManagerProvider);
  }

  public static SellerRepositoryImpl newInstance(PreferenceManager preferenceManager) {
    return new SellerRepositoryImpl(preferenceManager);
  }
}
