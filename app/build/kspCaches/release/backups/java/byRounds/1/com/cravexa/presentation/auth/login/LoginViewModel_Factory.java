package com.cravexa.presentation.auth.login;

import com.cravexa.domain.usecase.GoogleSignInUseCase;
import com.cravexa.domain.usecase.LoginUseCase;
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
public final class LoginViewModel_Factory implements Factory<LoginViewModel> {
  private final Provider<LoginUseCase> loginUseCaseProvider;

  private final Provider<GoogleSignInUseCase> googleSignInUseCaseProvider;

  public LoginViewModel_Factory(Provider<LoginUseCase> loginUseCaseProvider,
      Provider<GoogleSignInUseCase> googleSignInUseCaseProvider) {
    this.loginUseCaseProvider = loginUseCaseProvider;
    this.googleSignInUseCaseProvider = googleSignInUseCaseProvider;
  }

  @Override
  public LoginViewModel get() {
    return newInstance(loginUseCaseProvider.get(), googleSignInUseCaseProvider.get());
  }

  public static LoginViewModel_Factory create(Provider<LoginUseCase> loginUseCaseProvider,
      Provider<GoogleSignInUseCase> googleSignInUseCaseProvider) {
    return new LoginViewModel_Factory(loginUseCaseProvider, googleSignInUseCaseProvider);
  }

  public static LoginViewModel newInstance(LoginUseCase loginUseCase,
      GoogleSignInUseCase googleSignInUseCase) {
    return new LoginViewModel(loginUseCase, googleSignInUseCase);
  }
}
