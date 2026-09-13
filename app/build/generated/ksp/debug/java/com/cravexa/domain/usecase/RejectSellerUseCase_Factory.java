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
public final class RejectSellerUseCase_Factory implements Factory<RejectSellerUseCase> {
  private final Provider<AdminSellerRepository> repositoryProvider;

  public RejectSellerUseCase_Factory(Provider<AdminSellerRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public RejectSellerUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static RejectSellerUseCase_Factory create(
      Provider<AdminSellerRepository> repositoryProvider) {
    return new RejectSellerUseCase_Factory(repositoryProvider);
  }

  public static RejectSellerUseCase newInstance(AdminSellerRepository repository) {
    return new RejectSellerUseCase(repository);
  }
}
