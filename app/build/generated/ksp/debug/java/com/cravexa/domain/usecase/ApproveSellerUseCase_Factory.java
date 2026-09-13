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
public final class ApproveSellerUseCase_Factory implements Factory<ApproveSellerUseCase> {
  private final Provider<AdminSellerRepository> repositoryProvider;

  public ApproveSellerUseCase_Factory(Provider<AdminSellerRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public ApproveSellerUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static ApproveSellerUseCase_Factory create(
      Provider<AdminSellerRepository> repositoryProvider) {
    return new ApproveSellerUseCase_Factory(repositoryProvider);
  }

  public static ApproveSellerUseCase newInstance(AdminSellerRepository repository) {
    return new ApproveSellerUseCase(repository);
  }
}
