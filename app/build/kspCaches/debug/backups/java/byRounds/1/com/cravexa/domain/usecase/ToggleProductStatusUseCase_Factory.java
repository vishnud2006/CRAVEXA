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
public final class ToggleProductStatusUseCase_Factory implements Factory<ToggleProductStatusUseCase> {
  private final Provider<AdminProductModerationRepository> repositoryProvider;

  public ToggleProductStatusUseCase_Factory(
      Provider<AdminProductModerationRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public ToggleProductStatusUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static ToggleProductStatusUseCase_Factory create(
      Provider<AdminProductModerationRepository> repositoryProvider) {
    return new ToggleProductStatusUseCase_Factory(repositoryProvider);
  }

  public static ToggleProductStatusUseCase newInstance(
      AdminProductModerationRepository repository) {
    return new ToggleProductStatusUseCase(repository);
  }
}
