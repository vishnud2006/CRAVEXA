package com.cravexa.presentation.admin.products;

import com.cravexa.domain.usecase.ApproveProductUseCase;
import com.cravexa.domain.usecase.GetAdminProductsUseCase;
import com.cravexa.domain.usecase.RejectProductUseCase;
import com.cravexa.domain.usecase.ToggleProductStatusUseCase;
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
public final class AdminProductsViewModel_Factory implements Factory<AdminProductsViewModel> {
  private final Provider<GetAdminProductsUseCase> getAdminProductsUseCaseProvider;

  private final Provider<ApproveProductUseCase> approveProductUseCaseProvider;

  private final Provider<RejectProductUseCase> rejectProductUseCaseProvider;

  private final Provider<ToggleProductStatusUseCase> toggleProductStatusUseCaseProvider;

  public AdminProductsViewModel_Factory(
      Provider<GetAdminProductsUseCase> getAdminProductsUseCaseProvider,
      Provider<ApproveProductUseCase> approveProductUseCaseProvider,
      Provider<RejectProductUseCase> rejectProductUseCaseProvider,
      Provider<ToggleProductStatusUseCase> toggleProductStatusUseCaseProvider) {
    this.getAdminProductsUseCaseProvider = getAdminProductsUseCaseProvider;
    this.approveProductUseCaseProvider = approveProductUseCaseProvider;
    this.rejectProductUseCaseProvider = rejectProductUseCaseProvider;
    this.toggleProductStatusUseCaseProvider = toggleProductStatusUseCaseProvider;
  }

  @Override
  public AdminProductsViewModel get() {
    return newInstance(getAdminProductsUseCaseProvider.get(), approveProductUseCaseProvider.get(), rejectProductUseCaseProvider.get(), toggleProductStatusUseCaseProvider.get());
  }

  public static AdminProductsViewModel_Factory create(
      Provider<GetAdminProductsUseCase> getAdminProductsUseCaseProvider,
      Provider<ApproveProductUseCase> approveProductUseCaseProvider,
      Provider<RejectProductUseCase> rejectProductUseCaseProvider,
      Provider<ToggleProductStatusUseCase> toggleProductStatusUseCaseProvider) {
    return new AdminProductsViewModel_Factory(getAdminProductsUseCaseProvider, approveProductUseCaseProvider, rejectProductUseCaseProvider, toggleProductStatusUseCaseProvider);
  }

  public static AdminProductsViewModel newInstance(GetAdminProductsUseCase getAdminProductsUseCase,
      ApproveProductUseCase approveProductUseCase, RejectProductUseCase rejectProductUseCase,
      ToggleProductStatusUseCase toggleProductStatusUseCase) {
    return new AdminProductsViewModel(getAdminProductsUseCase, approveProductUseCase, rejectProductUseCase, toggleProductStatusUseCase);
  }
}
