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
public final class GetUserProfileUseCase_Factory implements Factory<GetUserProfileUseCase> {
  private final Provider<UserRepository> userRepositoryProvider;

  public GetUserProfileUseCase_Factory(Provider<UserRepository> userRepositoryProvider) {
    this.userRepositoryProvider = userRepositoryProvider;
  }

  @Override
  public GetUserProfileUseCase get() {
    return newInstance(userRepositoryProvider.get());
  }

  public static GetUserProfileUseCase_Factory create(
      Provider<UserRepository> userRepositoryProvider) {
    return new GetUserProfileUseCase_Factory(userRepositoryProvider);
  }

  public static GetUserProfileUseCase newInstance(UserRepository userRepository) {
    return new GetUserProfileUseCase(userRepository);
  }
}
