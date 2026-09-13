package com.cravexa.domain.usecase;

import com.cravexa.domain.repository.AdminPaymentRefundRepository;
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
public final class GetAdminPaymentsUseCase_Factory implements Factory<GetAdminPaymentsUseCase> {
  private final Provider<AdminPaymentRefundRepository> repositoryProvider;

  public GetAdminPaymentsUseCase_Factory(
      Provider<AdminPaymentRefundRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public GetAdminPaymentsUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static GetAdminPaymentsUseCase_Factory create(
      Provider<AdminPaymentRefundRepository> repositoryProvider) {
    return new GetAdminPaymentsUseCase_Factory(repositoryProvider);
  }

  public static GetAdminPaymentsUseCase newInstance(AdminPaymentRefundRepository repository) {
    return new GetAdminPaymentsUseCase(repository);
  }
}
