package com.cravexa.presentation.splash;

import com.cravexa.data.local.PreferenceManager;
import com.cravexa.domain.usecase.GetAuthStateUseCase;
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
public final class SplashViewModel_Factory implements Factory<SplashViewModel> {
  private final Provider<PreferenceManager> preferenceManagerProvider;

  private final Provider<GetAuthStateUseCase> getAuthStateUseCaseProvider;

  public SplashViewModel_Factory(Provider<PreferenceManager> preferenceManagerProvider,
      Provider<GetAuthStateUseCase> getAuthStateUseCaseProvider) {
    this.preferenceManagerProvider = preferenceManagerProvider;
    this.getAuthStateUseCaseProvider = getAuthStateUseCaseProvider;
  }

  @Override
  public SplashViewModel get() {
    return newInstance(preferenceManagerProvider.get(), getAuthStateUseCaseProvider.get());
  }

  public static SplashViewModel_Factory create(
      Provider<PreferenceManager> preferenceManagerProvider,
      Provider<GetAuthStateUseCase> getAuthStateUseCaseProvider) {
    return new SplashViewModel_Factory(preferenceManagerProvider, getAuthStateUseCaseProvider);
  }

  public static SplashViewModel newInstance(PreferenceManager preferenceManager,
      GetAuthStateUseCase getAuthStateUseCase) {
    return new SplashViewModel(preferenceManager, getAuthStateUseCase);
  }
}
