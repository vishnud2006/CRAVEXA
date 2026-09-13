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
public final class RejectProductUseCase_Factory implements Factory<RejectProductUseCase> {
  private final Provider<AdminProductModerationRepository> repositoryProvider;

  public RejectProductUseCase_Factory(
      Provider<AdminProductModerationRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public RejectProductUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static RejectProductUseCase_Factory create(
      Provider<AdminProductModerationRepository> repositoryProvider) {
    return new RejectProductUseCase_Factory(repositoryProvider);
  }

  public static RejectProductUseCase newInstance(AdminProductModerationRepository repository) {
    return new RejectProductUseCase(repository);
  }
}
