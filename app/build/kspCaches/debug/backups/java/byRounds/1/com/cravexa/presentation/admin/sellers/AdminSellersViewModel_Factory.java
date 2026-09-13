package com.cravexa.presentation.admin.sellers;

import com.cravexa.domain.usecase.ApproveSellerUseCase;
import com.cravexa.domain.usecase.GetAdminSellersUseCase;
import com.cravexa.domain.usecase.RejectSellerUseCase;
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
public final class AdminSellersViewModel_Factory implements Factory<AdminSellersViewModel> {
  private final Provider<GetAdminSellersUseCase> getAdminSellersUseCaseProvider;

  private final Provider<ApproveSellerUseCase> approveSellerUseCaseProvider;

  private final Provider<RejectSellerUseCase> rejectSellerUseCaseProvider;

  public AdminSellersViewModel_Factory(
      Provider<GetAdminSellersUseCase> getAdminSellersUseCaseProvider,
      Provider<ApproveSellerUseCase> approveSellerUseCaseProvider,
      Provider<RejectSellerUseCase> rejectSellerUseCaseProvider) {
    this.getAdminSellersUseCaseProvider = getAdminSellersUseCaseProvider;
    this.approveSellerUseCaseProvider = approveSellerUseCaseProvider;
    this.rejectSellerUseCaseProvider = rejectSellerUseCaseProvider;
  }

  @Override
  public AdminSellersViewModel get() {
    return newInstance(getAdminSellersUseCaseProvider.get(), approveSellerUseCaseProvider.get(), rejectSellerUseCaseProvider.get());
  }

  public static AdminSellersViewModel_Factory create(
      Provider<GetAdminSellersUseCase> getAdminSellersUseCaseProvider,
      Provider<ApproveSellerUseCase> approveSellerUseCaseProvider,
      Provider<RejectSellerUseCase> rejectSellerUseCaseProvider) {
    return new AdminSellersViewModel_Factory(getAdminSellersUseCaseProvider, approveSellerUseCaseProvider, rejectSellerUseCaseProvider);
  }

  public static AdminSellersViewModel newInstance(GetAdminSellersUseCase getAdminSellersUseCase,
      ApproveSellerUseCase approveSellerUseCase, RejectSellerUseCase rejectSellerUseCase) {
    return new AdminSellersViewModel(getAdminSellersUseCase, approveSellerUseCase, rejectSellerUseCase);
  }
}
