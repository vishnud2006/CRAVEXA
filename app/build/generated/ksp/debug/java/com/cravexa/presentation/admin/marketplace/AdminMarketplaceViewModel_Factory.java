package com.cravexa.presentation.admin.marketplace;

import com.cravexa.domain.usecase.AddAdminCategoryUseCase;
import com.cravexa.domain.usecase.AddAdminCouponUseCase;
import com.cravexa.domain.usecase.GetAdminBannersUseCase;
import com.cravexa.domain.usecase.GetAdminCategoriesUseCase;
import com.cravexa.domain.usecase.GetAdminCouponsUseCase;
import com.cravexa.domain.usecase.ToggleAdminBannerUseCase;
import com.cravexa.domain.usecase.ToggleAdminCategoryUseCase;
import com.cravexa.domain.usecase.ToggleAdminCouponUseCase;
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
public final class AdminMarketplaceViewModel_Factory implements Factory<AdminMarketplaceViewModel> {
  private final Provider<GetAdminCategoriesUseCase> getAdminCategoriesUseCaseProvider;

  private final Provider<AddAdminCategoryUseCase> addAdminCategoryUseCaseProvider;

  private final Provider<ToggleAdminCategoryUseCase> toggleAdminCategoryUseCaseProvider;

  private final Provider<GetAdminCouponsUseCase> getAdminCouponsUseCaseProvider;

  private final Provider<AddAdminCouponUseCase> addAdminCouponUseCaseProvider;

  private final Provider<ToggleAdminCouponUseCase> toggleAdminCouponUseCaseProvider;

  private final Provider<GetAdminBannersUseCase> getAdminBannersUseCaseProvider;

  private final Provider<ToggleAdminBannerUseCase> toggleAdminBannerUseCaseProvider;

  public AdminMarketplaceViewModel_Factory(
      Provider<GetAdminCategoriesUseCase> getAdminCategoriesUseCaseProvider,
      Provider<AddAdminCategoryUseCase> addAdminCategoryUseCaseProvider,
      Provider<ToggleAdminCategoryUseCase> toggleAdminCategoryUseCaseProvider,
      Provider<GetAdminCouponsUseCase> getAdminCouponsUseCaseProvider,
      Provider<AddAdminCouponUseCase> addAdminCouponUseCaseProvider,
      Provider<ToggleAdminCouponUseCase> toggleAdminCouponUseCaseProvider,
      Provider<GetAdminBannersUseCase> getAdminBannersUseCaseProvider,
      Provider<ToggleAdminBannerUseCase> toggleAdminBannerUseCaseProvider) {
    this.getAdminCategoriesUseCaseProvider = getAdminCategoriesUseCaseProvider;
    this.addAdminCategoryUseCaseProvider = addAdminCategoryUseCaseProvider;
    this.toggleAdminCategoryUseCaseProvider = toggleAdminCategoryUseCaseProvider;
    this.getAdminCouponsUseCaseProvider = getAdminCouponsUseCaseProvider;
    this.addAdminCouponUseCaseProvider = addAdminCouponUseCaseProvider;
    this.toggleAdminCouponUseCaseProvider = toggleAdminCouponUseCaseProvider;
    this.getAdminBannersUseCaseProvider = getAdminBannersUseCaseProvider;
    this.toggleAdminBannerUseCaseProvider = toggleAdminBannerUseCaseProvider;
  }

  @Override
  public AdminMarketplaceViewModel get() {
    return newInstance(getAdminCategoriesUseCaseProvider.get(), addAdminCategoryUseCaseProvider.get(), toggleAdminCategoryUseCaseProvider.get(), getAdminCouponsUseCaseProvider.get(), addAdminCouponUseCaseProvider.get(), toggleAdminCouponUseCaseProvider.get(), getAdminBannersUseCaseProvider.get(), toggleAdminBannerUseCaseProvider.get());
  }

  public static AdminMarketplaceViewModel_Factory create(
      Provider<GetAdminCategoriesUseCase> getAdminCategoriesUseCaseProvider,
      Provider<AddAdminCategoryUseCase> addAdminCategoryUseCaseProvider,
      Provider<ToggleAdminCategoryUseCase> toggleAdminCategoryUseCaseProvider,
      Provider<GetAdminCouponsUseCase> getAdminCouponsUseCaseProvider,
      Provider<AddAdminCouponUseCase> addAdminCouponUseCaseProvider,
      Provider<ToggleAdminCouponUseCase> toggleAdminCouponUseCaseProvider,
      Provider<GetAdminBannersUseCase> getAdminBannersUseCaseProvider,
      Provider<ToggleAdminBannerUseCase> toggleAdminBannerUseCaseProvider) {
    return new AdminMarketplaceViewModel_Factory(getAdminCategoriesUseCaseProvider, addAdminCategoryUseCaseProvider, toggleAdminCategoryUseCaseProvider, getAdminCouponsUseCaseProvider, addAdminCouponUseCaseProvider, toggleAdminCouponUseCaseProvider, getAdminBannersUseCaseProvider, toggleAdminBannerUseCaseProvider);
  }

  public static AdminMarketplaceViewModel newInstance(
      GetAdminCategoriesUseCase getAdminCategoriesUseCase,
      AddAdminCategoryUseCase addAdminCategoryUseCase,
      ToggleAdminCategoryUseCase toggleAdminCategoryUseCase,
      GetAdminCouponsUseCase getAdminCouponsUseCase, AddAdminCouponUseCase addAdminCouponUseCase,
      ToggleAdminCouponUseCase toggleAdminCouponUseCase,
      GetAdminBannersUseCase getAdminBannersUseCase,
      ToggleAdminBannerUseCase toggleAdminBannerUseCase) {
    return new AdminMarketplaceViewModel(getAdminCategoriesUseCase, addAdminCategoryUseCase, toggleAdminCategoryUseCase, getAdminCouponsUseCase, addAdminCouponUseCase, toggleAdminCouponUseCase, getAdminBannersUseCase, toggleAdminBannerUseCase);
  }
}
