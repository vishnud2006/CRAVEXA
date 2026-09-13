package com.cravexa.data.remote;

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
public final class TokenInterceptor_Factory implements Factory<TokenInterceptor> {
  private final Provider<PreferenceManager> preferenceManagerProvider;

  public TokenInterceptor_Factory(Provider<PreferenceManager> preferenceManagerProvider) {
    this.preferenceManagerProvider = preferenceManagerProvider;
  }

  @Override
  public TokenInterceptor get() {
    return newInstance(preferenceManagerProvider.get());
  }

  public static TokenInterceptor_Factory create(
      Provider<PreferenceManager> preferenceManagerProvider) {
    return new TokenInterceptor_Factory(preferenceManagerProvider);
  }

  public static TokenInterceptor newInstance(PreferenceManager preferenceManager) {
    return new TokenInterceptor(preferenceManager);
  }
}
