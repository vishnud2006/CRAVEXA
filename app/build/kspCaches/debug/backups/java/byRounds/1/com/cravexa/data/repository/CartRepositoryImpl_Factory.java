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
public final class CartRepositoryImpl_Factory implements Factory<CartRepositoryImpl> {
  private final Provider<PreferenceManager> preferenceManagerProvider;

  public CartRepositoryImpl_Factory(Provider<PreferenceManager> preferenceManagerProvider) {
    this.preferenceManagerProvider = preferenceManagerProvider;
  }

  @Override
  public CartRepositoryImpl get() {
    return newInstance(preferenceManagerProvider.get());
  }

  public static CartRepositoryImpl_Factory create(
      Provider<PreferenceManager> preferenceManagerProvider) {
    return new CartRepositoryImpl_Factory(preferenceManagerProvider);
  }

  public static CartRepositoryImpl newInstance(PreferenceManager preferenceManager) {
    return new CartRepositoryImpl(preferenceManager);
  }
}
