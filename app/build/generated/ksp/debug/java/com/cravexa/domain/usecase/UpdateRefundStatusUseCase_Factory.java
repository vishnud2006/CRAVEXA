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
public final class UpdateRefundStatusUseCase_Factory implements Factory<UpdateRefundStatusUseCase> {
  private final Provider<AdminPaymentRefundRepository> repositoryProvider;

  public UpdateRefundStatusUseCase_Factory(
      Provider<AdminPaymentRefundRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public UpdateRefundStatusUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static UpdateRefundStatusUseCase_Factory create(
      Provider<AdminPaymentRefundRepository> repositoryProvider) {
    return new UpdateRefundStatusUseCase_Factory(repositoryProvider);
  }

  public static UpdateRefundStatusUseCase newInstance(AdminPaymentRefundRepository repository) {
    return new UpdateRefundStatusUseCase(repository);
  }
}
