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
public final class WishlistRepositoryImpl_Factory implements Factory<WishlistRepositoryImpl> {
  private final Provider<PreferenceManager> preferenceManagerProvider;

  public WishlistRepositoryImpl_Factory(Provider<PreferenceManager> preferenceManagerProvider) {
    this.preferenceManagerProvider = preferenceManagerProvider;
  }

  @Override
  public WishlistRepositoryImpl get() {
    return newInstance(preferenceManagerProvider.get());
  }

  public static WishlistRepositoryImpl_Factory create(
      Provider<PreferenceManager> preferenceManagerProvider) {
    return new WishlistRepositoryImpl_Factory(preferenceManagerProvider);
  }

  public static WishlistRepositoryImpl newInstance(PreferenceManager preferenceManager) {
    return new WishlistRepositoryImpl(preferenceManager);
  }
}
