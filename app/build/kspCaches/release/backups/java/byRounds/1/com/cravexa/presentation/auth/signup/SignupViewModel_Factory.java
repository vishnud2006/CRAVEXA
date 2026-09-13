package com.cravexa.presentation.auth.signup;

import com.cravexa.domain.usecase.GoogleSignInUseCase;
import com.cravexa.domain.usecase.SignupUseCase;
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
public final class SignupViewModel_Factory implements Factory<SignupViewModel> {
  private final Provider<SignupUseCase> signupUseCaseProvider;

  private final Provider<GoogleSignInUseCase> googleSignInUseCaseProvider;

  public SignupViewModel_Factory(Provider<SignupUseCase> signupUseCaseProvider,
      Provider<GoogleSignInUseCase> googleSignInUseCaseProvider) {
    this.signupUseCaseProvider = signupUseCaseProvider;
    this.googleSignInUseCaseProvider = googleSignInUseCaseProvider;
  }

  @Override
  public SignupViewModel get() {
    return newInstance(signupUseCaseProvider.get(), googleSignInUseCaseProvider.get());
  }

  public static SignupViewModel_Factory create(Provider<SignupUseCase> signupUseCaseProvider,
      Provider<GoogleSignInUseCase> googleSignInUseCaseProvider) {
    return new SignupViewModel_Factory(signupUseCaseProvider, googleSignInUseCaseProvider);
  }

  public static SignupViewModel newInstance(SignupUseCase signupUseCase,
      GoogleSignInUseCase googleSignInUseCase) {
    return new SignupViewModel(signupUseCase, googleSignInUseCase);
  }
}
