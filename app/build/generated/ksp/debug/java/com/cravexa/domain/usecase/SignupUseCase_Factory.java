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
public final class SignupUseCase_Factory implements Factory<SignupUseCase> {
  private final Provider<AuthRepository> authRepositoryProvider;

  public SignupUseCase_Factory(Provider<AuthRepository> authRepositoryProvider) {
    this.authRepositoryProvider = authRepositoryProvider;
  }

  @Override
  public SignupUseCase get() {
    return newInstance(authRepositoryProvider.get());
  }

  public static SignupUseCase_Factory create(Provider<AuthRepository> authRepositoryProvider) {
    return new SignupUseCase_Factory(authRepositoryProvider);
  }

  public static SignupUseCase newInstance(AuthRepository authRepository) {
    return new SignupUseCase(authRepository);
  }
}
