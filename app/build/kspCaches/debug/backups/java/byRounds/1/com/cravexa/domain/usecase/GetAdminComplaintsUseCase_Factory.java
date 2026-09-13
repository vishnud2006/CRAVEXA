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
public final class GetAdminComplaintsUseCase_Factory implements Factory<GetAdminComplaintsUseCase> {
  private final Provider<AdminComplaintRepository> repositoryProvider;

  public GetAdminComplaintsUseCase_Factory(Provider<AdminComplaintRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public GetAdminComplaintsUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static GetAdminComplaintsUseCase_Factory create(
      Provider<AdminComplaintRepository> repositoryProvider) {
    return new GetAdminComplaintsUseCase_Factory(repositoryProvider);
  }

  public static GetAdminComplaintsUseCase newInstance(AdminComplaintRepository repository) {
    return new GetAdminComplaintsUseCase(repository);
  }
}
