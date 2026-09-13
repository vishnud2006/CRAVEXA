package com.cravexa.domain.usecase;

import com.cravexa.domain.repository.AdminProductModerationRepository;
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
public final class ApproveProductUseCase_Factory implements Factory<ApproveProductUseCase> {
  private final Provider<AdminProductModerationRepository> repositoryProvider;

  public ApproveProductUseCase_Factory(
      Provider<AdminProductModerationRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public ApproveProductUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static ApproveProductUseCase_Factory create(
      Provider<AdminProductModerationRepository> repositoryProvider) {
    return new ApproveProductUseCase_Factory(repositoryProvider);
  }

  public static ApproveProductUseCase newInstance(AdminProductModerationRepository repository) {
    return new ApproveProductUseCase(repository);
  }
}
