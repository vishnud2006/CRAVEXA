package com.cravexa.presentation.onboarding;

import com.cravexa.data.local.PreferenceManager;
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
public final class OnboardingViewModel_Factory implements Factory<OnboardingViewModel> {
  private final Provider<PreferenceManager> preferenceManagerProvider;

  public OnboardingViewModel_Factory(Provider<PreferenceManager> preferenceManagerProvider) {
    this.preferenceManagerProvider = preferenceManagerProvider;
  }

  @Override
  public OnboardingViewModel get() {
    return newInstance(preferenceManagerProvider.get());
  }

  public static OnboardingViewModel_Factory create(
      Provider<PreferenceManager> preferenceManagerProvider) {
    return new OnboardingViewModel_Factory(preferenceManagerProvider);
  }

  public static OnboardingViewModel newInstance(PreferenceManager preferenceManager) {
    return new OnboardingViewModel(preferenceManager);
  }
}
