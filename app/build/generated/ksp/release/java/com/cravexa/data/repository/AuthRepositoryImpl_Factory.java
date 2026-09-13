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
public final class AuthRepositoryImpl_Factory implements Factory<AuthRepositoryImpl> {
  private final Provider<PreferenceManager> preferenceManagerProvider;

  public AuthRepositoryImpl_Factory(Provider<PreferenceManager> preferenceManagerProvider) {
    this.preferenceManagerProvider = preferenceManagerProvider;
  }

  @Override
  public AuthRepositoryImpl get() {
    return newInstance(preferenceManagerProvider.get());
  }

  public static AuthRepositoryImpl_Factory create(
      Provider<PreferenceManager> preferenceManagerProvider) {
    return new AuthRepositoryImpl_Factory(preferenceManagerProvider);
  }

  public static AuthRepositoryImpl newInstance(PreferenceManager preferenceManager) {
    return new AuthRepositoryImpl(preferenceManager);
  }
}
