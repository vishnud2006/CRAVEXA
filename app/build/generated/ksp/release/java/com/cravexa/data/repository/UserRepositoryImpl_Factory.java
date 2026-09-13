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
public final class UserRepositoryImpl_Factory implements Factory<UserRepositoryImpl> {
  private final Provider<PreferenceManager> preferenceManagerProvider;

  public UserRepositoryImpl_Factory(Provider<PreferenceManager> preferenceManagerProvider) {
    this.preferenceManagerProvider = preferenceManagerProvider;
  }

  @Override
  public UserRepositoryImpl get() {
    return newInstance(preferenceManagerProvider.get());
  }

  public static UserRepositoryImpl_Factory create(
      Provider<PreferenceManager> preferenceManagerProvider) {
    return new UserRepositoryImpl_Factory(preferenceManagerProvider);
  }

  public static UserRepositoryImpl newInstance(PreferenceManager preferenceManager) {
    return new UserRepositoryImpl(preferenceManager);
  }
}
