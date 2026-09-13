package com.cravexa.domain.usecase;

import com.cravexa.domain.repository.AuthRepository;
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
public final class VerifyPhoneOtpUseCase_Factory implements Factory<VerifyPhoneOtpUseCase> {
  private final Provider<AuthRepository> authRepositoryProvider;

  public VerifyPhoneOtpUseCase_Factory(Provider<AuthRepository> authRepositoryProvider) {
    this.authRepositoryProvider = authRepositoryProvider;
  }

  @Override
  public VerifyPhoneOtpUseCase get() {
    return newInstance(authRepositoryProvider.get());
  }

  public static VerifyPhoneOtpUseCase_Factory create(
      Provider<AuthRepository> authRepositoryProvider) {
    return new VerifyPhoneOtpUseCase_Factory(authRepositoryProvider);
  }

  public static VerifyPhoneOtpUseCase newInstance(AuthRepository authRepository) {
    return new VerifyPhoneOtpUseCase(authRepository);
  }
}
