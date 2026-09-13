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
public final class GetAdminRefundsUseCase_Factory implements Factory<GetAdminRefundsUseCase> {
  private final Provider<AdminPaymentRefundRepository> repositoryProvider;

  public GetAdminRefundsUseCase_Factory(Provider<AdminPaymentRefundRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public GetAdminRefundsUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static GetAdminRefundsUseCase_Factory create(
      Provider<AdminPaymentRefundRepository> repositoryProvider) {
    return new GetAdminRefundsUseCase_Factory(repositoryProvider);
  }

  public static GetAdminRefundsUseCase newInstance(AdminPaymentRefundRepository repository) {
    return new GetAdminRefundsUseCase(repository);
  }
}
