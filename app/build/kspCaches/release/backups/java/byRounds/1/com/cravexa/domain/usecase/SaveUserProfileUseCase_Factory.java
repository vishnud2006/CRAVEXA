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
public final class SaveUserProfileUseCase_Factory implements Factory<SaveUserProfileUseCase> {
  private final Provider<UserRepository> userRepositoryProvider;

  public SaveUserProfileUseCase_Factory(Provider<UserRepository> userRepositoryProvider) {
    this.userRepositoryProvider = userRepositoryProvider;
  }

  @Override
  public SaveUserProfileUseCase get() {
    return newInstance(userRepositoryProvider.get());
  }

  public static SaveUserProfileUseCase_Factory create(
      Provider<UserRepository> userRepositoryProvider) {
    return new SaveUserProfileUseCase_Factory(userRepositoryProvider);
  }

  public static SaveUserProfileUseCase newInstance(UserRepository userRepository) {
    return new SaveUserProfileUseCase(userRepository);
  }
}
