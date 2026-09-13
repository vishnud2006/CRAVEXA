package com.cravexa.domain.usecase;

import com.cravexa.domain.repository.AdminComplaintRepository;
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
public final class UpdateComplaintStatusUseCase_Factory implements Factory<UpdateComplaintStatusUseCase> {
  private final Provider<AdminComplaintRepository> repositoryProvider;

  public UpdateComplaintStatusUseCase_Factory(
      Provider<AdminComplaintRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public UpdateComplaintStatusUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static UpdateComplaintStatusUseCase_Factory create(
      Provider<AdminComplaintRepository> repositoryProvider) {
    return new UpdateComplaintStatusUseCase_Factory(repositoryProvider);
  }

  public static UpdateComplaintStatusUseCase newInstance(AdminComplaintRepository repository) {
    return new UpdateComplaintStatusUseCase(repository);
  }
}
