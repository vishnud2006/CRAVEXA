package com.cravexa.presentation.seller.profile;

import com.cravexa.domain.usecase.GetSellerProfileUseCase;
import com.cravexa.domain.usecase.LogoutUseCase;
import com.cravexa.domain.usecase.SubmitFssaiUseCase;
import com.cravexa.domain.usecase.UpdateSellerProfileUseCase;
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
public final class SellerProfileViewModel_Factory implements Factory<SellerProfileViewModel> {
  private final Provider<GetSellerProfileUseCase> getSellerProfileUseCaseProvider;

  private final Provider<UpdateSellerProfileUseCase> updateSellerProfileUseCaseProvider;

  private final Provider<SubmitFssaiUseCase> submitFssaiUseCaseProvider;

  private final Provider<LogoutUseCase> logoutUseCaseProvider;

  public SellerProfileViewModel_Factory(
      Provider<GetSellerProfileUseCase> getSellerProfileUseCaseProvider,
      Provider<UpdateSellerProfileUseCase> updateSellerProfileUseCaseProvider,
      Provider<SubmitFssaiUseCase> submitFssaiUseCaseProvider,
      Provider<LogoutUseCase> logoutUseCaseProvider) {
    this.getSellerProfileUseCaseProvider = getSellerProfileUseCaseProvider;
    this.updateSellerProfileUseCaseProvider = updateSellerProfileUseCaseProvider;
    this.submitFssaiUseCaseProvider = submitFssaiUseCaseProvider;
    this.logoutUseCaseProvider = logoutUseCaseProvider;
  }

  @Override
  public SellerProfileViewModel get() {
    return newInstance(getSellerProfileUseCaseProvider.get(), updateSellerProfileUseCaseProvider.get(), submitFssaiUseCaseProvider.get(), logoutUseCaseProvider.get());
  }

  public static SellerProfileViewModel_Factory create(
      Provider<GetSellerProfileUseCase> getSellerProfileUseCaseProvider,
      Provider<UpdateSellerProfileUseCase> updateSellerProfileUseCaseProvider,
      Provider<SubmitFssaiUseCase> submitFssaiUseCaseProvider,
      Provider<LogoutUseCase> logoutUseCaseProvider) {
    return new SellerProfileViewModel_Factory(getSellerProfileUseCaseProvider, updateSellerProfileUseCaseProvider, submitFssaiUseCaseProvider, logoutUseCaseProvider);
  }

  public static SellerProfileViewModel newInstance(GetSellerProfileUseCase getSellerProfileUseCase,
      UpdateSellerProfileUseCase updateSellerProfileUseCase, SubmitFssaiUseCase submitFssaiUseCase,
      LogoutUseCase logoutUseCase) {
    return new SellerProfileViewModel(getSellerProfileUseCase, updateSellerProfileUseCase, submitFssaiUseCase, logoutUseCase);
  }
}
