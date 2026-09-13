package com.cravexa.presentation.auth.profile;

import com.cravexa.domain.usecase.GetUserProfileUseCase;
import com.cravexa.domain.usecase.SaveUserProfileUseCase;
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
public final class ProfileSetupViewModel_Factory implements Factory<ProfileSetupViewModel> {
  private final Provider<GetUserProfileUseCase> getUserProfileUseCaseProvider;

  private final Provider<SaveUserProfileUseCase> saveUserProfileUseCaseProvider;

  public ProfileSetupViewModel_Factory(
      Provider<GetUserProfileUseCase> getUserProfileUseCaseProvider,
      Provider<SaveUserProfileUseCase> saveUserProfileUseCaseProvider) {
    this.getUserProfileUseCaseProvider = getUserProfileUseCaseProvider;
    this.saveUserProfileUseCaseProvider = saveUserProfileUseCaseProvider;
  }

  @Override
  public ProfileSetupViewModel get() {
    return newInstance(getUserProfileUseCaseProvider.get(), saveUserProfileUseCaseProvider.get());
  }

  public static ProfileSetupViewModel_Factory create(
      Provider<GetUserProfileUseCase> getUserProfileUseCaseProvider,
      Provider<SaveUserProfileUseCase> saveUserProfileUseCaseProvider) {
    return new ProfileSetupViewModel_Factory(getUserProfileUseCaseProvider, saveUserProfileUseCaseProvider);
  }

  public static ProfileSetupViewModel newInstance(GetUserProfileUseCase getUserProfileUseCase,
      SaveUserProfileUseCase saveUserProfileUseCase) {
    return new ProfileSetupViewModel(getUserProfileUseCase, saveUserProfileUseCase);
  }
}
