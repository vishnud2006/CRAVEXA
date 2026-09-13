package com.cravexa.presentation.admin.fssai;

import com.cravexa.domain.usecase.GetAdminSellersUseCase;
import com.cravexa.domain.usecase.RejectFssaiUseCase;
import com.cravexa.domain.usecase.VerifyFssaiUseCase;
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
public final class AdminFssaiViewModel_Factory implements Factory<AdminFssaiViewModel> {
  private final Provider<GetAdminSellersUseCase> getAdminSellersUseCaseProvider;

  private final Provider<VerifyFssaiUseCase> verifyFssaiUseCaseProvider;

  private final Provider<RejectFssaiUseCase> rejectFssaiUseCaseProvider;

  public AdminFssaiViewModel_Factory(
      Provider<GetAdminSellersUseCase> getAdminSellersUseCaseProvider,
      Provider<VerifyFssaiUseCase> verifyFssaiUseCaseProvider,
      Provider<RejectFssaiUseCase> rejectFssaiUseCaseProvider) {
    this.getAdminSellersUseCaseProvider = getAdminSellersUseCaseProvider;
    this.verifyFssaiUseCaseProvider = verifyFssaiUseCaseProvider;
    this.rejectFssaiUseCaseProvider = rejectFssaiUseCaseProvider;
  }

  @Override
  public AdminFssaiViewModel get() {
    return newInstance(getAdminSellersUseCaseProvider.get(), verifyFssaiUseCaseProvider.get(), rejectFssaiUseCaseProvider.get());
  }

  public static AdminFssaiViewModel_Factory create(
      Provider<GetAdminSellersUseCase> getAdminSellersUseCaseProvider,
      Provider<VerifyFssaiUseCase> verifyFssaiUseCaseProvider,
      Provider<RejectFssaiUseCase> rejectFssaiUseCaseProvider) {
    return new AdminFssaiViewModel_Factory(getAdminSellersUseCaseProvider, verifyFssaiUseCaseProvider, rejectFssaiUseCaseProvider);
  }

  public static AdminFssaiViewModel newInstance(GetAdminSellersUseCase getAdminSellersUseCase,
      VerifyFssaiUseCase verifyFssaiUseCase, RejectFssaiUseCase rejectFssaiUseCase) {
    return new AdminFssaiViewModel(getAdminSellersUseCase, verifyFssaiUseCase, rejectFssaiUseCase);
  }
}
