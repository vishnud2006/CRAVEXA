package com.cravexa.domain.usecase;

import com.cravexa.domain.repository.AdminSellerRepository;
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
public final class VerifyFssaiUseCase_Factory implements Factory<VerifyFssaiUseCase> {
  private final Provider<AdminSellerRepository> repositoryProvider;

  public VerifyFssaiUseCase_Factory(Provider<AdminSellerRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public VerifyFssaiUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static VerifyFssaiUseCase_Factory create(
      Provider<AdminSellerRepository> repositoryProvider) {
    return new VerifyFssaiUseCase_Factory(repositoryProvider);
  }

  public static VerifyFssaiUseCase newInstance(AdminSellerRepository repository) {
    return new VerifyFssaiUseCase(repository);
  }
}
