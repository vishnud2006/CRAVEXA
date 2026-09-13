package com.cravexa.domain.usecase;

import com.cravexa.domain.repository.UserRepository;
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
public final class UpdateUserProfileUseCase_Factory implements Factory<UpdateUserProfileUseCase> {
  private final Provider<UserRepository> userRepositoryProvider;

  public UpdateUserProfileUseCase_Factory(Provider<UserRepository> userRepositoryProvider) {
    this.userRepositoryProvider = userRepositoryProvider;
  }

  @Override
  public UpdateUserProfileUseCase get() {
    return newInstance(userRepositoryProvider.get());
  }

  public static UpdateUserProfileUseCase_Factory create(
      Provider<UserRepository> userRepositoryProvider) {
    return new UpdateUserProfileUseCase_Factory(userRepositoryProvider);
  }

  public static UpdateUserProfileUseCase newInstance(UserRepository userRepository) {
    return new UpdateUserProfileUseCase(userRepository);
  }
}
