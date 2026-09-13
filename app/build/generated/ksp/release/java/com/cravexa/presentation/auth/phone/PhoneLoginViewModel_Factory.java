package com.cravexa.presentation.auth.phone;

import com.cravexa.domain.usecase.SendPhoneOtpUseCase;
import com.cravexa.domain.usecase.VerifyPhoneOtpUseCase;
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
public final class PhoneLoginViewModel_Factory implements Factory<PhoneLoginViewModel> {
  private final Provider<SendPhoneOtpUseCase> sendPhoneOtpUseCaseProvider;

  private final Provider<VerifyPhoneOtpUseCase> verifyPhoneOtpUseCaseProvider;

  public PhoneLoginViewModel_Factory(Provider<SendPhoneOtpUseCase> sendPhoneOtpUseCaseProvider,
      Provider<VerifyPhoneOtpUseCase> verifyPhoneOtpUseCaseProvider) {
    this.sendPhoneOtpUseCaseProvider = sendPhoneOtpUseCaseProvider;
    this.verifyPhoneOtpUseCaseProvider = verifyPhoneOtpUseCaseProvider;
  }

  @Override
  public PhoneLoginViewModel get() {
    return newInstance(sendPhoneOtpUseCaseProvider.get(), verifyPhoneOtpUseCaseProvider.get());
  }

  public static PhoneLoginViewModel_Factory create(
      Provider<SendPhoneOtpUseCase> sendPhoneOtpUseCaseProvider,
      Provider<VerifyPhoneOtpUseCase> verifyPhoneOtpUseCaseProvider) {
    return new PhoneLoginViewModel_Factory(sendPhoneOtpUseCaseProvider, verifyPhoneOtpUseCaseProvider);
  }

  public static PhoneLoginViewModel newInstance(SendPhoneOtpUseCase sendPhoneOtpUseCase,
      VerifyPhoneOtpUseCase verifyPhoneOtpUseCase) {
    return new PhoneLoginViewModel(sendPhoneOtpUseCase, verifyPhoneOtpUseCase);
  }
}
