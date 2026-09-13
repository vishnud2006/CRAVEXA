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
public final class RejectFssaiUseCase_Factory implements Factory<RejectFssaiUseCase> {
  private final Provider<AdminSellerRepository> repositoryProvider;

  public RejectFssaiUseCase_Factory(Provider<AdminSellerRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public RejectFssaiUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static RejectFssaiUseCase_Factory create(
      Provider<AdminSellerRepository> repositoryProvider) {
    return new RejectFssaiUseCase_Factory(repositoryProvider);
  }

  public static RejectFssaiUseCase newInstance(AdminSellerRepository repository) {
    return new RejectFssaiUseCase(repository);
  }
}
