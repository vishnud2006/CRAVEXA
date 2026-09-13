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
public final class SendPhoneOtpUseCase_Factory implements Factory<SendPhoneOtpUseCase> {
  private final Provider<AuthRepository> authRepositoryProvider;

  public SendPhoneOtpUseCase_Factory(Provider<AuthRepository> authRepositoryProvider) {
    this.authRepositoryProvider = authRepositoryProvider;
  }

  @Override
  public SendPhoneOtpUseCase get() {
    return newInstance(authRepositoryProvider.get());
  }

  public static SendPhoneOtpUseCase_Factory create(
      Provider<AuthRepository> authRepositoryProvider) {
    return new SendPhoneOtpUseCase_Factory(authRepositoryProvider);
  }

  public static SendPhoneOtpUseCase newInstance(AuthRepository authRepository) {
    return new SendPhoneOtpUseCase(authRepository);
  }
}
