package com.cravexa.presentation.admin.payments;

import com.cravexa.domain.usecase.GetAdminPaymentsUseCase;
import com.cravexa.domain.usecase.GetAdminRefundsUseCase;
import com.cravexa.domain.usecase.UpdateRefundStatusUseCase;
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
public final class AdminPaymentsViewModel_Factory implements Factory<AdminPaymentsViewModel> {
  private final Provider<GetAdminPaymentsUseCase> getAdminPaymentsUseCaseProvider;

  private final Provider<GetAdminRefundsUseCase> getAdminRefundsUseCaseProvider;

  private final Provider<UpdateRefundStatusUseCase> updateRefundStatusUseCaseProvider;

  public AdminPaymentsViewModel_Factory(
      Provider<GetAdminPaymentsUseCase> getAdminPaymentsUseCaseProvider,
      Provider<GetAdminRefundsUseCase> getAdminRefundsUseCaseProvider,
      Provider<UpdateRefundStatusUseCase> updateRefundStatusUseCaseProvider) {
    this.getAdminPaymentsUseCaseProvider = getAdminPaymentsUseCaseProvider;
    this.getAdminRefundsUseCaseProvider = getAdminRefundsUseCaseProvider;
    this.updateRefundStatusUseCaseProvider = updateRefundStatusUseCaseProvider;
  }

  @Override
  public AdminPaymentsViewModel get() {
    return newInstance(getAdminPaymentsUseCaseProvider.get(), getAdminRefundsUseCaseProvider.get(), updateRefundStatusUseCaseProvider.get());
  }

  public static AdminPaymentsViewModel_Factory create(
      Provider<GetAdminPaymentsUseCase> getAdminPaymentsUseCaseProvider,
      Provider<GetAdminRefundsUseCase> getAdminRefundsUseCaseProvider,
      Provider<UpdateRefundStatusUseCase> updateRefundStatusUseCaseProvider) {
    return new AdminPaymentsViewModel_Factory(getAdminPaymentsUseCaseProvider, getAdminRefundsUseCaseProvider, updateRefundStatusUseCaseProvider);
  }

  public static AdminPaymentsViewModel newInstance(GetAdminPaymentsUseCase getAdminPaymentsUseCase,
      GetAdminRefundsUseCase getAdminRefundsUseCase,
      UpdateRefundStatusUseCase updateRefundStatusUseCase) {
    return new AdminPaymentsViewModel(getAdminPaymentsUseCase, getAdminRefundsUseCase, updateRefundStatusUseCase);
  }
}
