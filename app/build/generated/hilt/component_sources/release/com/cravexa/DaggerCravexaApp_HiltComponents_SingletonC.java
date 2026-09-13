package com.cravexa;

import android.app.Activity;
import android.app.Service;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import com.cravexa.core.di.AppModule_ProvidePreferenceManagerFactory;
import com.cravexa.data.local.PreferenceManager;
import com.cravexa.data.repository.AddressRepositoryImpl;
import com.cravexa.data.repository.AuthRepositoryImpl;
import com.cravexa.data.repository.CartRepositoryImpl;
import com.cravexa.data.repository.CategoryRepositoryImpl;
import com.cravexa.data.repository.CustomerRepositoryImpl;
import com.cravexa.data.repository.ImageUploadRepositoryImpl;
import com.cravexa.data.repository.OrderRepositoryImpl;
import com.cravexa.data.repository.ProductRepositoryImpl;
import com.cravexa.data.repository.RecentSearchRepositoryImpl;
import com.cravexa.data.repository.SellerEarningsRepositoryImpl;
import com.cravexa.data.repository.SellerOrderRepositoryImpl;
import com.cravexa.data.repository.SellerProductRepositoryImpl;
import com.cravexa.data.repository.SellerRepositoryImpl;
import com.cravexa.data.repository.SellerReviewRepositoryImpl;
import com.cravexa.data.repository.UserRepositoryImpl;
import com.cravexa.data.repository.WishlistRepositoryImpl;
import com.cravexa.domain.usecase.AddAddressUseCase;
import com.cravexa.domain.usecase.AddSellerProductUseCase;
import com.cravexa.domain.usecase.AddToCartUseCase;
import com.cravexa.domain.usecase.DeleteAddressUseCase;
import com.cravexa.domain.usecase.DeleteSellerProductUseCase;
import com.cravexa.domain.usecase.ForgotPasswordUseCase;
import com.cravexa.domain.usecase.GetAddressesUseCase;
import com.cravexa.domain.usecase.GetAuthStateUseCase;
import com.cravexa.domain.usecase.GetCartUseCase;
import com.cravexa.domain.usecase.GetCategoriesUseCase;
import com.cravexa.domain.usecase.GetCategoryProductsUseCase;
import com.cravexa.domain.usecase.GetCustomerProfileUseCase;
import com.cravexa.domain.usecase.GetHomeMarketplaceUseCase;
import com.cravexa.domain.usecase.GetOrderDetailUseCase;
import com.cravexa.domain.usecase.GetOrderTrackingUseCase;
import com.cravexa.domain.usecase.GetOrdersUseCase;
import com.cravexa.domain.usecase.GetProductDetailUseCase;
import com.cravexa.domain.usecase.GetSellerDashboardUseCase;
import com.cravexa.domain.usecase.GetSellerEarningsUseCase;
import com.cravexa.domain.usecase.GetSellerKitchenProductsUseCase;
import com.cravexa.domain.usecase.GetSellerOrderDetailUseCase;
import com.cravexa.domain.usecase.GetSellerOrdersUseCase;
import com.cravexa.domain.usecase.GetSellerPayoutsUseCase;
import com.cravexa.domain.usecase.GetSellerProductByIdUseCase;
import com.cravexa.domain.usecase.GetSellerProductsUseCase;
import com.cravexa.domain.usecase.GetSellerProfileUseCase;
import com.cravexa.domain.usecase.GetSellerReviewsUseCase;
import com.cravexa.domain.usecase.GetUserProfileUseCase;
import com.cravexa.domain.usecase.GetWishlistUseCase;
import com.cravexa.domain.usecase.GoogleSignInUseCase;
import com.cravexa.domain.usecase.LoginUseCase;
import com.cravexa.domain.usecase.LogoutUseCase;
import com.cravexa.domain.usecase.RecentSearchUseCases;
import com.cravexa.domain.usecase.ReplyToReviewUseCase;
import com.cravexa.domain.usecase.SaveUserProfileUseCase;
import com.cravexa.domain.usecase.SearchProductsUseCase;
import com.cravexa.domain.usecase.SendPhoneOtpUseCase;
import com.cravexa.domain.usecase.SetDefaultAddressUseCase;
import com.cravexa.domain.usecase.SignupUseCase;
import com.cravexa.domain.usecase.SubmitFssaiUseCase;
import com.cravexa.domain.usecase.ToggleProductAvailabilityUseCase;
import com.cravexa.domain.usecase.ToggleWishlistUseCase;
import com.cravexa.domain.usecase.UpdateAddressUseCase;
import com.cravexa.domain.usecase.UpdateCustomerProfileUseCase;
import com.cravexa.domain.usecase.UpdateProductStockUseCase;
import com.cravexa.domain.usecase.UpdateSellerOrderStatusUseCase;
import com.cravexa.domain.usecase.UpdateSellerProductUseCase;
import com.cravexa.domain.usecase.UpdateSellerProfileUseCase;
import com.cravexa.domain.usecase.UploadProductImageUseCase;
import com.cravexa.domain.usecase.VerifyPhoneOtpUseCase;
import com.cravexa.presentation.auth.forgotpassword.ForgotPasswordViewModel;
import com.cravexa.presentation.auth.forgotpassword.ForgotPasswordViewModel_HiltModules;
import com.cravexa.presentation.auth.login.LoginViewModel;
import com.cravexa.presentation.auth.login.LoginViewModel_HiltModules;
import com.cravexa.presentation.auth.phone.PhoneLoginViewModel;
import com.cravexa.presentation.auth.phone.PhoneLoginViewModel_HiltModules;
import com.cravexa.presentation.auth.profile.ProfileSetupViewModel;
import com.cravexa.presentation.auth.profile.ProfileSetupViewModel_HiltModules;
import com.cravexa.presentation.auth.signup.SignupViewModel;
import com.cravexa.presentation.auth.signup.SignupViewModel_HiltModules;
import com.cravexa.presentation.category.CategoryProductsViewModel;
import com.cravexa.presentation.category.CategoryProductsViewModel_HiltModules;
import com.cravexa.presentation.customer.address.AddressViewModel;
import com.cravexa.presentation.customer.address.AddressViewModel_HiltModules;
import com.cravexa.presentation.customer.orders.OrderDetailViewModel;
import com.cravexa.presentation.customer.orders.OrderDetailViewModel_HiltModules;
import com.cravexa.presentation.customer.orders.OrderTrackingViewModel;
import com.cravexa.presentation.customer.orders.OrderTrackingViewModel_HiltModules;
import com.cravexa.presentation.customer.orders.OrdersViewModel;
import com.cravexa.presentation.customer.orders.OrdersViewModel_HiltModules;
import com.cravexa.presentation.customer.profile.CustomerProfileViewModel;
import com.cravexa.presentation.customer.profile.CustomerProfileViewModel_HiltModules;
import com.cravexa.presentation.customer.profile.EditProfileViewModel;
import com.cravexa.presentation.customer.profile.EditProfileViewModel_HiltModules;
import com.cravexa.presentation.explore.ExploreViewModel;
import com.cravexa.presentation.explore.ExploreViewModel_HiltModules;
import com.cravexa.presentation.home.HomeViewModel;
import com.cravexa.presentation.home.HomeViewModel_HiltModules;
import com.cravexa.presentation.onboarding.OnboardingViewModel;
import com.cravexa.presentation.onboarding.OnboardingViewModel_HiltModules;
import com.cravexa.presentation.product.ProductDetailViewModel;
import com.cravexa.presentation.product.ProductDetailViewModel_HiltModules;
import com.cravexa.presentation.search.SearchViewModel;
import com.cravexa.presentation.search.SearchViewModel_HiltModules;
import com.cravexa.presentation.seller.dashboard.SellerDashboardViewModel;
import com.cravexa.presentation.seller.dashboard.SellerDashboardViewModel_HiltModules;
import com.cravexa.presentation.seller.earnings.SellerEarningsViewModel;
import com.cravexa.presentation.seller.earnings.SellerEarningsViewModel_HiltModules;
import com.cravexa.presentation.seller.orders.SellerOrderDetailViewModel;
import com.cravexa.presentation.seller.orders.SellerOrderDetailViewModel_HiltModules;
import com.cravexa.presentation.seller.orders.SellerOrdersViewModel;
import com.cravexa.presentation.seller.orders.SellerOrdersViewModel_HiltModules;
import com.cravexa.presentation.seller.products.AddEditProductViewModel;
import com.cravexa.presentation.seller.products.AddEditProductViewModel_HiltModules;
import com.cravexa.presentation.seller.products.SellerProductsViewModel;
import com.cravexa.presentation.seller.products.SellerProductsViewModel_HiltModules;
import com.cravexa.presentation.seller.profile.SellerProfileViewModel;
import com.cravexa.presentation.seller.profile.SellerProfileViewModel_HiltModules;
import com.cravexa.presentation.seller.publicprofile.PublicSellerProfileViewModel;
import com.cravexa.presentation.seller.publicprofile.PublicSellerProfileViewModel_HiltModules;
import com.cravexa.presentation.seller.reviews.SellerReviewsViewModel;
import com.cravexa.presentation.seller.reviews.SellerReviewsViewModel_HiltModules;
import com.cravexa.presentation.splash.SplashViewModel;
import com.cravexa.presentation.splash.SplashViewModel_HiltModules;
import com.cravexa.presentation.wishlist.WishlistViewModel;
import com.cravexa.presentation.wishlist.WishlistViewModel_HiltModules;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.managers.SavedStateHandleHolder;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.IdentifierNameString;
import dagger.internal.KeepFieldType;
import dagger.internal.LazyClassKeyMap;
import dagger.internal.MapBuilder;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

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
public final class DaggerCravexaApp_HiltComponents_SingletonC {
  private DaggerCravexaApp_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    public CravexaApp_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements CravexaApp_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private SavedStateHandleHolder savedStateHandleHolder;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ActivityRetainedCBuilder savedStateHandleHolder(
        SavedStateHandleHolder savedStateHandleHolder) {
      this.savedStateHandleHolder = Preconditions.checkNotNull(savedStateHandleHolder);
      return this;
    }

    @Override
    public CravexaApp_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements CravexaApp_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public CravexaApp_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements CravexaApp_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public CravexaApp_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements CravexaApp_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public CravexaApp_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements CravexaApp_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public CravexaApp_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements CravexaApp_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public CravexaApp_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements CravexaApp_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public CravexaApp_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends CravexaApp_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    private ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends CravexaApp_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    private FragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends CravexaApp_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    private ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends CravexaApp_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    private ActivityCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    @Override
    public void injectMainActivity(MainActivity arg0) {
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Map<Class<?>, Boolean> getViewModelKeys() {
      return LazyClassKeyMap.<Boolean>of(MapBuilder.<String, Boolean>newMapBuilder(28).put(LazyClassKeyProvider.com_cravexa_presentation_seller_products_AddEditProductViewModel, AddEditProductViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_cravexa_presentation_customer_address_AddressViewModel, AddressViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_cravexa_presentation_category_CategoryProductsViewModel, CategoryProductsViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_cravexa_presentation_customer_profile_CustomerProfileViewModel, CustomerProfileViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_cravexa_presentation_customer_profile_EditProfileViewModel, EditProfileViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_cravexa_presentation_explore_ExploreViewModel, ExploreViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_cravexa_presentation_auth_forgotpassword_ForgotPasswordViewModel, ForgotPasswordViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_cravexa_presentation_home_HomeViewModel, HomeViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_cravexa_presentation_auth_login_LoginViewModel, LoginViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_cravexa_presentation_onboarding_OnboardingViewModel, OnboardingViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_cravexa_presentation_customer_orders_OrderDetailViewModel, OrderDetailViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_cravexa_presentation_customer_orders_OrderTrackingViewModel, OrderTrackingViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_cravexa_presentation_customer_orders_OrdersViewModel, OrdersViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_cravexa_presentation_auth_phone_PhoneLoginViewModel, PhoneLoginViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_cravexa_presentation_product_ProductDetailViewModel, ProductDetailViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_cravexa_presentation_auth_profile_ProfileSetupViewModel, ProfileSetupViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_cravexa_presentation_seller_publicprofile_PublicSellerProfileViewModel, PublicSellerProfileViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_cravexa_presentation_search_SearchViewModel, SearchViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_cravexa_presentation_seller_dashboard_SellerDashboardViewModel, SellerDashboardViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_cravexa_presentation_seller_earnings_SellerEarningsViewModel, SellerEarningsViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_cravexa_presentation_seller_orders_SellerOrderDetailViewModel, SellerOrderDetailViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_cravexa_presentation_seller_orders_SellerOrdersViewModel, SellerOrdersViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_cravexa_presentation_seller_products_SellerProductsViewModel, SellerProductsViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_cravexa_presentation_seller_profile_SellerProfileViewModel, SellerProfileViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_cravexa_presentation_seller_reviews_SellerReviewsViewModel, SellerReviewsViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_cravexa_presentation_auth_signup_SignupViewModel, SignupViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_cravexa_presentation_splash_SplashViewModel, SplashViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_cravexa_presentation_wishlist_WishlistViewModel, WishlistViewModel_HiltModules.KeyModule.provide()).build());
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_cravexa_presentation_auth_signup_SignupViewModel = "com.cravexa.presentation.auth.signup.SignupViewModel";

      static String com_cravexa_presentation_seller_dashboard_SellerDashboardViewModel = "com.cravexa.presentation.seller.dashboard.SellerDashboardViewModel";

      static String com_cravexa_presentation_seller_profile_SellerProfileViewModel = "com.cravexa.presentation.seller.profile.SellerProfileViewModel";

      static String com_cravexa_presentation_auth_forgotpassword_ForgotPasswordViewModel = "com.cravexa.presentation.auth.forgotpassword.ForgotPasswordViewModel";

      static String com_cravexa_presentation_wishlist_WishlistViewModel = "com.cravexa.presentation.wishlist.WishlistViewModel";

      static String com_cravexa_presentation_auth_login_LoginViewModel = "com.cravexa.presentation.auth.login.LoginViewModel";

      static String com_cravexa_presentation_customer_address_AddressViewModel = "com.cravexa.presentation.customer.address.AddressViewModel";

      static String com_cravexa_presentation_seller_orders_SellerOrdersViewModel = "com.cravexa.presentation.seller.orders.SellerOrdersViewModel";

      static String com_cravexa_presentation_home_HomeViewModel = "com.cravexa.presentation.home.HomeViewModel";

      static String com_cravexa_presentation_seller_products_SellerProductsViewModel = "com.cravexa.presentation.seller.products.SellerProductsViewModel";

      static String com_cravexa_presentation_seller_earnings_SellerEarningsViewModel = "com.cravexa.presentation.seller.earnings.SellerEarningsViewModel";

      static String com_cravexa_presentation_customer_orders_OrderTrackingViewModel = "com.cravexa.presentation.customer.orders.OrderTrackingViewModel";

      static String com_cravexa_presentation_customer_orders_OrderDetailViewModel = "com.cravexa.presentation.customer.orders.OrderDetailViewModel";

      static String com_cravexa_presentation_splash_SplashViewModel = "com.cravexa.presentation.splash.SplashViewModel";

      static String com_cravexa_presentation_customer_profile_CustomerProfileViewModel = "com.cravexa.presentation.customer.profile.CustomerProfileViewModel";

      static String com_cravexa_presentation_explore_ExploreViewModel = "com.cravexa.presentation.explore.ExploreViewModel";

      static String com_cravexa_presentation_auth_profile_ProfileSetupViewModel = "com.cravexa.presentation.auth.profile.ProfileSetupViewModel";

      static String com_cravexa_presentation_seller_publicprofile_PublicSellerProfileViewModel = "com.cravexa.presentation.seller.publicprofile.PublicSellerProfileViewModel";

      static String com_cravexa_presentation_category_CategoryProductsViewModel = "com.cravexa.presentation.category.CategoryProductsViewModel";

      static String com_cravexa_presentation_product_ProductDetailViewModel = "com.cravexa.presentation.product.ProductDetailViewModel";

      static String com_cravexa_presentation_customer_orders_OrdersViewModel = "com.cravexa.presentation.customer.orders.OrdersViewModel";

      static String com_cravexa_presentation_onboarding_OnboardingViewModel = "com.cravexa.presentation.onboarding.OnboardingViewModel";

      static String com_cravexa_presentation_seller_reviews_SellerReviewsViewModel = "com.cravexa.presentation.seller.reviews.SellerReviewsViewModel";

      static String com_cravexa_presentation_seller_orders_SellerOrderDetailViewModel = "com.cravexa.presentation.seller.orders.SellerOrderDetailViewModel";

      static String com_cravexa_presentation_search_SearchViewModel = "com.cravexa.presentation.search.SearchViewModel";

      static String com_cravexa_presentation_auth_phone_PhoneLoginViewModel = "com.cravexa.presentation.auth.phone.PhoneLoginViewModel";

      static String com_cravexa_presentation_customer_profile_EditProfileViewModel = "com.cravexa.presentation.customer.profile.EditProfileViewModel";

      static String com_cravexa_presentation_seller_products_AddEditProductViewModel = "com.cravexa.presentation.seller.products.AddEditProductViewModel";

      @KeepFieldType
      SignupViewModel com_cravexa_presentation_auth_signup_SignupViewModel2;

      @KeepFieldType
      SellerDashboardViewModel com_cravexa_presentation_seller_dashboard_SellerDashboardViewModel2;

      @KeepFieldType
      SellerProfileViewModel com_cravexa_presentation_seller_profile_SellerProfileViewModel2;

      @KeepFieldType
      ForgotPasswordViewModel com_cravexa_presentation_auth_forgotpassword_ForgotPasswordViewModel2;

      @KeepFieldType
      WishlistViewModel com_cravexa_presentation_wishlist_WishlistViewModel2;

      @KeepFieldType
      LoginViewModel com_cravexa_presentation_auth_login_LoginViewModel2;

      @KeepFieldType
      AddressViewModel com_cravexa_presentation_customer_address_AddressViewModel2;

      @KeepFieldType
      SellerOrdersViewModel com_cravexa_presentation_seller_orders_SellerOrdersViewModel2;

      @KeepFieldType
      HomeViewModel com_cravexa_presentation_home_HomeViewModel2;

      @KeepFieldType
      SellerProductsViewModel com_cravexa_presentation_seller_products_SellerProductsViewModel2;

      @KeepFieldType
      SellerEarningsViewModel com_cravexa_presentation_seller_earnings_SellerEarningsViewModel2;

      @KeepFieldType
      OrderTrackingViewModel com_cravexa_presentation_customer_orders_OrderTrackingViewModel2;

      @KeepFieldType
      OrderDetailViewModel com_cravexa_presentation_customer_orders_OrderDetailViewModel2;

      @KeepFieldType
      SplashViewModel com_cravexa_presentation_splash_SplashViewModel2;

      @KeepFieldType
      CustomerProfileViewModel com_cravexa_presentation_customer_profile_CustomerProfileViewModel2;

      @KeepFieldType
      ExploreViewModel com_cravexa_presentation_explore_ExploreViewModel2;

      @KeepFieldType
      ProfileSetupViewModel com_cravexa_presentation_auth_profile_ProfileSetupViewModel2;

      @KeepFieldType
      PublicSellerProfileViewModel com_cravexa_presentation_seller_publicprofile_PublicSellerProfileViewModel2;

      @KeepFieldType
      CategoryProductsViewModel com_cravexa_presentation_category_CategoryProductsViewModel2;

      @KeepFieldType
      ProductDetailViewModel com_cravexa_presentation_product_ProductDetailViewModel2;

      @KeepFieldType
      OrdersViewModel com_cravexa_presentation_customer_orders_OrdersViewModel2;

      @KeepFieldType
      OnboardingViewModel com_cravexa_presentation_onboarding_OnboardingViewModel2;

      @KeepFieldType
      SellerReviewsViewModel com_cravexa_presentation_seller_reviews_SellerReviewsViewModel2;

      @KeepFieldType
      SellerOrderDetailViewModel com_cravexa_presentation_seller_orders_SellerOrderDetailViewModel2;

      @KeepFieldType
      SearchViewModel com_cravexa_presentation_search_SearchViewModel2;

      @KeepFieldType
      PhoneLoginViewModel com_cravexa_presentation_auth_phone_PhoneLoginViewModel2;

      @KeepFieldType
      EditProfileViewModel com_cravexa_presentation_customer_profile_EditProfileViewModel2;

      @KeepFieldType
      AddEditProductViewModel com_cravexa_presentation_seller_products_AddEditProductViewModel2;
    }
  }

  private static final class ViewModelCImpl extends CravexaApp_HiltComponents.ViewModelC {
    private final SavedStateHandle savedStateHandle;

    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<AddEditProductViewModel> addEditProductViewModelProvider;

    private Provider<AddressViewModel> addressViewModelProvider;

    private Provider<CategoryProductsViewModel> categoryProductsViewModelProvider;

    private Provider<CustomerProfileViewModel> customerProfileViewModelProvider;

    private Provider<EditProfileViewModel> editProfileViewModelProvider;

    private Provider<ExploreViewModel> exploreViewModelProvider;

    private Provider<ForgotPasswordViewModel> forgotPasswordViewModelProvider;

    private Provider<HomeViewModel> homeViewModelProvider;

    private Provider<LoginViewModel> loginViewModelProvider;

    private Provider<OnboardingViewModel> onboardingViewModelProvider;

    private Provider<OrderDetailViewModel> orderDetailViewModelProvider;

    private Provider<OrderTrackingViewModel> orderTrackingViewModelProvider;

    private Provider<OrdersViewModel> ordersViewModelProvider;

    private Provider<PhoneLoginViewModel> phoneLoginViewModelProvider;

    private Provider<ProductDetailViewModel> productDetailViewModelProvider;

    private Provider<ProfileSetupViewModel> profileSetupViewModelProvider;

    private Provider<PublicSellerProfileViewModel> publicSellerProfileViewModelProvider;

    private Provider<SearchViewModel> searchViewModelProvider;

    private Provider<SellerDashboardViewModel> sellerDashboardViewModelProvider;

    private Provider<SellerEarningsViewModel> sellerEarningsViewModelProvider;

    private Provider<SellerOrderDetailViewModel> sellerOrderDetailViewModelProvider;

    private Provider<SellerOrdersViewModel> sellerOrdersViewModelProvider;

    private Provider<SellerProductsViewModel> sellerProductsViewModelProvider;

    private Provider<SellerProfileViewModel> sellerProfileViewModelProvider;

    private Provider<SellerReviewsViewModel> sellerReviewsViewModelProvider;

    private Provider<SignupViewModel> signupViewModelProvider;

    private Provider<SplashViewModel> splashViewModelProvider;

    private Provider<WishlistViewModel> wishlistViewModelProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.savedStateHandle = savedStateHandleParam;
      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    private GetSellerProductByIdUseCase getSellerProductByIdUseCase() {
      return new GetSellerProductByIdUseCase(singletonCImpl.sellerProductRepositoryImplProvider.get());
    }

    private AddSellerProductUseCase addSellerProductUseCase() {
      return new AddSellerProductUseCase(singletonCImpl.sellerProductRepositoryImplProvider.get());
    }

    private UpdateSellerProductUseCase updateSellerProductUseCase() {
      return new UpdateSellerProductUseCase(singletonCImpl.sellerProductRepositoryImplProvider.get());
    }

    private GetSellerProfileUseCase getSellerProfileUseCase() {
      return new GetSellerProfileUseCase(singletonCImpl.sellerRepositoryImplProvider.get());
    }

    private GetCategoriesUseCase getCategoriesUseCase() {
      return new GetCategoriesUseCase(singletonCImpl.categoryRepositoryImplProvider.get());
    }

    private UploadProductImageUseCase uploadProductImageUseCase() {
      return new UploadProductImageUseCase(singletonCImpl.imageUploadRepositoryImplProvider.get());
    }

    private GetAddressesUseCase getAddressesUseCase() {
      return new GetAddressesUseCase(singletonCImpl.addressRepositoryImplProvider.get());
    }

    private AddAddressUseCase addAddressUseCase() {
      return new AddAddressUseCase(singletonCImpl.addressRepositoryImplProvider.get());
    }

    private UpdateAddressUseCase updateAddressUseCase() {
      return new UpdateAddressUseCase(singletonCImpl.addressRepositoryImplProvider.get());
    }

    private DeleteAddressUseCase deleteAddressUseCase() {
      return new DeleteAddressUseCase(singletonCImpl.addressRepositoryImplProvider.get());
    }

    private SetDefaultAddressUseCase setDefaultAddressUseCase() {
      return new SetDefaultAddressUseCase(singletonCImpl.addressRepositoryImplProvider.get());
    }

    private GetCategoryProductsUseCase getCategoryProductsUseCase() {
      return new GetCategoryProductsUseCase(singletonCImpl.productRepositoryImplProvider.get());
    }

    private GetWishlistUseCase getWishlistUseCase() {
      return new GetWishlistUseCase(singletonCImpl.wishlistRepositoryImplProvider.get());
    }

    private ToggleWishlistUseCase toggleWishlistUseCase() {
      return new ToggleWishlistUseCase(singletonCImpl.wishlistRepositoryImplProvider.get());
    }

    private AddToCartUseCase addToCartUseCase() {
      return new AddToCartUseCase(singletonCImpl.cartRepositoryImplProvider.get());
    }

    private GetCustomerProfileUseCase getCustomerProfileUseCase() {
      return new GetCustomerProfileUseCase(singletonCImpl.customerRepositoryImplProvider.get());
    }

    private LogoutUseCase logoutUseCase() {
      return new LogoutUseCase(singletonCImpl.authRepositoryImplProvider.get());
    }

    private UpdateCustomerProfileUseCase updateCustomerProfileUseCase() {
      return new UpdateCustomerProfileUseCase(singletonCImpl.customerRepositoryImplProvider.get());
    }

    private GetHomeMarketplaceUseCase getHomeMarketplaceUseCase() {
      return new GetHomeMarketplaceUseCase(singletonCImpl.productRepositoryImplProvider.get(), singletonCImpl.categoryRepositoryImplProvider.get());
    }

    private ForgotPasswordUseCase forgotPasswordUseCase() {
      return new ForgotPasswordUseCase(singletonCImpl.authRepositoryImplProvider.get());
    }

    private GetUserProfileUseCase getUserProfileUseCase() {
      return new GetUserProfileUseCase(singletonCImpl.userRepositoryImplProvider.get());
    }

    private GetCartUseCase getCartUseCase() {
      return new GetCartUseCase(singletonCImpl.cartRepositoryImplProvider.get());
    }

    private LoginUseCase loginUseCase() {
      return new LoginUseCase(singletonCImpl.authRepositoryImplProvider.get());
    }

    private GoogleSignInUseCase googleSignInUseCase() {
      return new GoogleSignInUseCase(singletonCImpl.authRepositoryImplProvider.get());
    }

    private GetOrderDetailUseCase getOrderDetailUseCase() {
      return new GetOrderDetailUseCase(singletonCImpl.orderRepositoryImplProvider.get());
    }

    private GetOrderTrackingUseCase getOrderTrackingUseCase() {
      return new GetOrderTrackingUseCase(singletonCImpl.orderRepositoryImplProvider.get());
    }

    private GetOrdersUseCase getOrdersUseCase() {
      return new GetOrdersUseCase(singletonCImpl.orderRepositoryImplProvider.get());
    }

    private SendPhoneOtpUseCase sendPhoneOtpUseCase() {
      return new SendPhoneOtpUseCase(singletonCImpl.authRepositoryImplProvider.get());
    }

    private VerifyPhoneOtpUseCase verifyPhoneOtpUseCase() {
      return new VerifyPhoneOtpUseCase(singletonCImpl.authRepositoryImplProvider.get());
    }

    private GetProductDetailUseCase getProductDetailUseCase() {
      return new GetProductDetailUseCase(singletonCImpl.productRepositoryImplProvider.get());
    }

    private SaveUserProfileUseCase saveUserProfileUseCase() {
      return new SaveUserProfileUseCase(singletonCImpl.userRepositoryImplProvider.get());
    }

    private GetSellerProductsUseCase getSellerProductsUseCase() {
      return new GetSellerProductsUseCase(singletonCImpl.productRepositoryImplProvider.get());
    }

    private SearchProductsUseCase searchProductsUseCase() {
      return new SearchProductsUseCase(singletonCImpl.productRepositoryImplProvider.get());
    }

    private RecentSearchUseCases recentSearchUseCases() {
      return new RecentSearchUseCases(singletonCImpl.recentSearchRepositoryImplProvider.get());
    }

    private GetSellerDashboardUseCase getSellerDashboardUseCase() {
      return new GetSellerDashboardUseCase(singletonCImpl.sellerEarningsRepositoryImplProvider.get(), singletonCImpl.sellerRepositoryImplProvider.get());
    }

    private GetSellerOrdersUseCase getSellerOrdersUseCase() {
      return new GetSellerOrdersUseCase(singletonCImpl.sellerOrderRepositoryImplProvider.get());
    }

    private GetSellerEarningsUseCase getSellerEarningsUseCase() {
      return new GetSellerEarningsUseCase(singletonCImpl.sellerEarningsRepositoryImplProvider.get());
    }

    private GetSellerPayoutsUseCase getSellerPayoutsUseCase() {
      return new GetSellerPayoutsUseCase(singletonCImpl.sellerEarningsRepositoryImplProvider.get());
    }

    private GetSellerOrderDetailUseCase getSellerOrderDetailUseCase() {
      return new GetSellerOrderDetailUseCase(singletonCImpl.sellerOrderRepositoryImplProvider.get());
    }

    private UpdateSellerOrderStatusUseCase updateSellerOrderStatusUseCase() {
      return new UpdateSellerOrderStatusUseCase(singletonCImpl.sellerOrderRepositoryImplProvider.get());
    }

    private GetSellerKitchenProductsUseCase getSellerKitchenProductsUseCase() {
      return new GetSellerKitchenProductsUseCase(singletonCImpl.sellerProductRepositoryImplProvider.get());
    }

    private UpdateProductStockUseCase updateProductStockUseCase() {
      return new UpdateProductStockUseCase(singletonCImpl.sellerProductRepositoryImplProvider.get());
    }

    private ToggleProductAvailabilityUseCase toggleProductAvailabilityUseCase() {
      return new ToggleProductAvailabilityUseCase(singletonCImpl.sellerProductRepositoryImplProvider.get());
    }

    private DeleteSellerProductUseCase deleteSellerProductUseCase() {
      return new DeleteSellerProductUseCase(singletonCImpl.sellerProductRepositoryImplProvider.get());
    }

    private UpdateSellerProfileUseCase updateSellerProfileUseCase() {
      return new UpdateSellerProfileUseCase(singletonCImpl.sellerRepositoryImplProvider.get());
    }

    private SubmitFssaiUseCase submitFssaiUseCase() {
      return new SubmitFssaiUseCase(singletonCImpl.sellerRepositoryImplProvider.get());
    }

    private GetSellerReviewsUseCase getSellerReviewsUseCase() {
      return new GetSellerReviewsUseCase(singletonCImpl.sellerReviewRepositoryImplProvider.get());
    }

    private ReplyToReviewUseCase replyToReviewUseCase() {
      return new ReplyToReviewUseCase(singletonCImpl.sellerReviewRepositoryImplProvider.get());
    }

    private SignupUseCase signupUseCase() {
      return new SignupUseCase(singletonCImpl.authRepositoryImplProvider.get());
    }

    private GetAuthStateUseCase getAuthStateUseCase() {
      return new GetAuthStateUseCase(singletonCImpl.authRepositoryImplProvider.get());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.addEditProductViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.addressViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.categoryProductsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
      this.customerProfileViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 3);
      this.editProfileViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 4);
      this.exploreViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 5);
      this.forgotPasswordViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 6);
      this.homeViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 7);
      this.loginViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 8);
      this.onboardingViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 9);
      this.orderDetailViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 10);
      this.orderTrackingViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 11);
      this.ordersViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 12);
      this.phoneLoginViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 13);
      this.productDetailViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 14);
      this.profileSetupViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 15);
      this.publicSellerProfileViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 16);
      this.searchViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 17);
      this.sellerDashboardViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 18);
      this.sellerEarningsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 19);
      this.sellerOrderDetailViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 20);
      this.sellerOrdersViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 21);
      this.sellerProductsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 22);
      this.sellerProfileViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 23);
      this.sellerReviewsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 24);
      this.signupViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 25);
      this.splashViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 26);
      this.wishlistViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 27);
    }

    @Override
    public Map<Class<?>, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return LazyClassKeyMap.<javax.inject.Provider<ViewModel>>of(MapBuilder.<String, javax.inject.Provider<ViewModel>>newMapBuilder(28).put(LazyClassKeyProvider.com_cravexa_presentation_seller_products_AddEditProductViewModel, ((Provider) addEditProductViewModelProvider)).put(LazyClassKeyProvider.com_cravexa_presentation_customer_address_AddressViewModel, ((Provider) addressViewModelProvider)).put(LazyClassKeyProvider.com_cravexa_presentation_category_CategoryProductsViewModel, ((Provider) categoryProductsViewModelProvider)).put(LazyClassKeyProvider.com_cravexa_presentation_customer_profile_CustomerProfileViewModel, ((Provider) customerProfileViewModelProvider)).put(LazyClassKeyProvider.com_cravexa_presentation_customer_profile_EditProfileViewModel, ((Provider) editProfileViewModelProvider)).put(LazyClassKeyProvider.com_cravexa_presentation_explore_ExploreViewModel, ((Provider) exploreViewModelProvider)).put(LazyClassKeyProvider.com_cravexa_presentation_auth_forgotpassword_ForgotPasswordViewModel, ((Provider) forgotPasswordViewModelProvider)).put(LazyClassKeyProvider.com_cravexa_presentation_home_HomeViewModel, ((Provider) homeViewModelProvider)).put(LazyClassKeyProvider.com_cravexa_presentation_auth_login_LoginViewModel, ((Provider) loginViewModelProvider)).put(LazyClassKeyProvider.com_cravexa_presentation_onboarding_OnboardingViewModel, ((Provider) onboardingViewModelProvider)).put(LazyClassKeyProvider.com_cravexa_presentation_customer_orders_OrderDetailViewModel, ((Provider) orderDetailViewModelProvider)).put(LazyClassKeyProvider.com_cravexa_presentation_customer_orders_OrderTrackingViewModel, ((Provider) orderTrackingViewModelProvider)).put(LazyClassKeyProvider.com_cravexa_presentation_customer_orders_OrdersViewModel, ((Provider) ordersViewModelProvider)).put(LazyClassKeyProvider.com_cravexa_presentation_auth_phone_PhoneLoginViewModel, ((Provider) phoneLoginViewModelProvider)).put(LazyClassKeyProvider.com_cravexa_presentation_product_ProductDetailViewModel, ((Provider) productDetailViewModelProvider)).put(LazyClassKeyProvider.com_cravexa_presentation_auth_profile_ProfileSetupViewModel, ((Provider) profileSetupViewModelProvider)).put(LazyClassKeyProvider.com_cravexa_presentation_seller_publicprofile_PublicSellerProfileViewModel, ((Provider) publicSellerProfileViewModelProvider)).put(LazyClassKeyProvider.com_cravexa_presentation_search_SearchViewModel, ((Provider) searchViewModelProvider)).put(LazyClassKeyProvider.com_cravexa_presentation_seller_dashboard_SellerDashboardViewModel, ((Provider) sellerDashboardViewModelProvider)).put(LazyClassKeyProvider.com_cravexa_presentation_seller_earnings_SellerEarningsViewModel, ((Provider) sellerEarningsViewModelProvider)).put(LazyClassKeyProvider.com_cravexa_presentation_seller_orders_SellerOrderDetailViewModel, ((Provider) sellerOrderDetailViewModelProvider)).put(LazyClassKeyProvider.com_cravexa_presentation_seller_orders_SellerOrdersViewModel, ((Provider) sellerOrdersViewModelProvider)).put(LazyClassKeyProvider.com_cravexa_presentation_seller_products_SellerProductsViewModel, ((Provider) sellerProductsViewModelProvider)).put(LazyClassKeyProvider.com_cravexa_presentation_seller_profile_SellerProfileViewModel, ((Provider) sellerProfileViewModelProvider)).put(LazyClassKeyProvider.com_cravexa_presentation_seller_reviews_SellerReviewsViewModel, ((Provider) sellerReviewsViewModelProvider)).put(LazyClassKeyProvider.com_cravexa_presentation_auth_signup_SignupViewModel, ((Provider) signupViewModelProvider)).put(LazyClassKeyProvider.com_cravexa_presentation_splash_SplashViewModel, ((Provider) splashViewModelProvider)).put(LazyClassKeyProvider.com_cravexa_presentation_wishlist_WishlistViewModel, ((Provider) wishlistViewModelProvider)).build());
    }

    @Override
    public Map<Class<?>, Object> getHiltViewModelAssistedMap() {
      return Collections.<Class<?>, Object>emptyMap();
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_cravexa_presentation_explore_ExploreViewModel = "com.cravexa.presentation.explore.ExploreViewModel";

      static String com_cravexa_presentation_seller_reviews_SellerReviewsViewModel = "com.cravexa.presentation.seller.reviews.SellerReviewsViewModel";

      static String com_cravexa_presentation_category_CategoryProductsViewModel = "com.cravexa.presentation.category.CategoryProductsViewModel";

      static String com_cravexa_presentation_auth_signup_SignupViewModel = "com.cravexa.presentation.auth.signup.SignupViewModel";

      static String com_cravexa_presentation_search_SearchViewModel = "com.cravexa.presentation.search.SearchViewModel";

      static String com_cravexa_presentation_seller_orders_SellerOrderDetailViewModel = "com.cravexa.presentation.seller.orders.SellerOrderDetailViewModel";

      static String com_cravexa_presentation_auth_phone_PhoneLoginViewModel = "com.cravexa.presentation.auth.phone.PhoneLoginViewModel";

      static String com_cravexa_presentation_seller_publicprofile_PublicSellerProfileViewModel = "com.cravexa.presentation.seller.publicprofile.PublicSellerProfileViewModel";

      static String com_cravexa_presentation_seller_dashboard_SellerDashboardViewModel = "com.cravexa.presentation.seller.dashboard.SellerDashboardViewModel";

      static String com_cravexa_presentation_splash_SplashViewModel = "com.cravexa.presentation.splash.SplashViewModel";

      static String com_cravexa_presentation_customer_orders_OrderTrackingViewModel = "com.cravexa.presentation.customer.orders.OrderTrackingViewModel";

      static String com_cravexa_presentation_auth_forgotpassword_ForgotPasswordViewModel = "com.cravexa.presentation.auth.forgotpassword.ForgotPasswordViewModel";

      static String com_cravexa_presentation_customer_orders_OrdersViewModel = "com.cravexa.presentation.customer.orders.OrdersViewModel";

      static String com_cravexa_presentation_seller_orders_SellerOrdersViewModel = "com.cravexa.presentation.seller.orders.SellerOrdersViewModel";

      static String com_cravexa_presentation_seller_products_SellerProductsViewModel = "com.cravexa.presentation.seller.products.SellerProductsViewModel";

      static String com_cravexa_presentation_customer_profile_CustomerProfileViewModel = "com.cravexa.presentation.customer.profile.CustomerProfileViewModel";

      static String com_cravexa_presentation_seller_earnings_SellerEarningsViewModel = "com.cravexa.presentation.seller.earnings.SellerEarningsViewModel";

      static String com_cravexa_presentation_home_HomeViewModel = "com.cravexa.presentation.home.HomeViewModel";

      static String com_cravexa_presentation_auth_login_LoginViewModel = "com.cravexa.presentation.auth.login.LoginViewModel";

      static String com_cravexa_presentation_product_ProductDetailViewModel = "com.cravexa.presentation.product.ProductDetailViewModel";

      static String com_cravexa_presentation_customer_address_AddressViewModel = "com.cravexa.presentation.customer.address.AddressViewModel";

      static String com_cravexa_presentation_onboarding_OnboardingViewModel = "com.cravexa.presentation.onboarding.OnboardingViewModel";

      static String com_cravexa_presentation_seller_profile_SellerProfileViewModel = "com.cravexa.presentation.seller.profile.SellerProfileViewModel";

      static String com_cravexa_presentation_customer_orders_OrderDetailViewModel = "com.cravexa.presentation.customer.orders.OrderDetailViewModel";

      static String com_cravexa_presentation_seller_products_AddEditProductViewModel = "com.cravexa.presentation.seller.products.AddEditProductViewModel";

      static String com_cravexa_presentation_auth_profile_ProfileSetupViewModel = "com.cravexa.presentation.auth.profile.ProfileSetupViewModel";

      static String com_cravexa_presentation_customer_profile_EditProfileViewModel = "com.cravexa.presentation.customer.profile.EditProfileViewModel";

      static String com_cravexa_presentation_wishlist_WishlistViewModel = "com.cravexa.presentation.wishlist.WishlistViewModel";

      @KeepFieldType
      ExploreViewModel com_cravexa_presentation_explore_ExploreViewModel2;

      @KeepFieldType
      SellerReviewsViewModel com_cravexa_presentation_seller_reviews_SellerReviewsViewModel2;

      @KeepFieldType
      CategoryProductsViewModel com_cravexa_presentation_category_CategoryProductsViewModel2;

      @KeepFieldType
      SignupViewModel com_cravexa_presentation_auth_signup_SignupViewModel2;

      @KeepFieldType
      SearchViewModel com_cravexa_presentation_search_SearchViewModel2;

      @KeepFieldType
      SellerOrderDetailViewModel com_cravexa_presentation_seller_orders_SellerOrderDetailViewModel2;

      @KeepFieldType
      PhoneLoginViewModel com_cravexa_presentation_auth_phone_PhoneLoginViewModel2;

      @KeepFieldType
      PublicSellerProfileViewModel com_cravexa_presentation_seller_publicprofile_PublicSellerProfileViewModel2;

      @KeepFieldType
      SellerDashboardViewModel com_cravexa_presentation_seller_dashboard_SellerDashboardViewModel2;

      @KeepFieldType
      SplashViewModel com_cravexa_presentation_splash_SplashViewModel2;

      @KeepFieldType
      OrderTrackingViewModel com_cravexa_presentation_customer_orders_OrderTrackingViewModel2;

      @KeepFieldType
      ForgotPasswordViewModel com_cravexa_presentation_auth_forgotpassword_ForgotPasswordViewModel2;

      @KeepFieldType
      OrdersViewModel com_cravexa_presentation_customer_orders_OrdersViewModel2;

      @KeepFieldType
      SellerOrdersViewModel com_cravexa_presentation_seller_orders_SellerOrdersViewModel2;

      @KeepFieldType
      SellerProductsViewModel com_cravexa_presentation_seller_products_SellerProductsViewModel2;

      @KeepFieldType
      CustomerProfileViewModel com_cravexa_presentation_customer_profile_CustomerProfileViewModel2;

      @KeepFieldType
      SellerEarningsViewModel com_cravexa_presentation_seller_earnings_SellerEarningsViewModel2;

      @KeepFieldType
      HomeViewModel com_cravexa_presentation_home_HomeViewModel2;

      @KeepFieldType
      LoginViewModel com_cravexa_presentation_auth_login_LoginViewModel2;

      @KeepFieldType
      ProductDetailViewModel com_cravexa_presentation_product_ProductDetailViewModel2;

      @KeepFieldType
      AddressViewModel com_cravexa_presentation_customer_address_AddressViewModel2;

      @KeepFieldType
      OnboardingViewModel com_cravexa_presentation_onboarding_OnboardingViewModel2;

      @KeepFieldType
      SellerProfileViewModel com_cravexa_presentation_seller_profile_SellerProfileViewModel2;

      @KeepFieldType
      OrderDetailViewModel com_cravexa_presentation_customer_orders_OrderDetailViewModel2;

      @KeepFieldType
      AddEditProductViewModel com_cravexa_presentation_seller_products_AddEditProductViewModel2;

      @KeepFieldType
      ProfileSetupViewModel com_cravexa_presentation_auth_profile_ProfileSetupViewModel2;

      @KeepFieldType
      EditProfileViewModel com_cravexa_presentation_customer_profile_EditProfileViewModel2;

      @KeepFieldType
      WishlistViewModel com_cravexa_presentation_wishlist_WishlistViewModel2;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.cravexa.presentation.seller.products.AddEditProductViewModel 
          return (T) new AddEditProductViewModel(viewModelCImpl.savedStateHandle, viewModelCImpl.getSellerProductByIdUseCase(), viewModelCImpl.addSellerProductUseCase(), viewModelCImpl.updateSellerProductUseCase(), viewModelCImpl.getSellerProfileUseCase(), viewModelCImpl.getCategoriesUseCase(), viewModelCImpl.uploadProductImageUseCase());

          case 1: // com.cravexa.presentation.customer.address.AddressViewModel 
          return (T) new AddressViewModel(viewModelCImpl.getAddressesUseCase(), viewModelCImpl.addAddressUseCase(), viewModelCImpl.updateAddressUseCase(), viewModelCImpl.deleteAddressUseCase(), viewModelCImpl.setDefaultAddressUseCase());

          case 2: // com.cravexa.presentation.category.CategoryProductsViewModel 
          return (T) new CategoryProductsViewModel(viewModelCImpl.savedStateHandle, viewModelCImpl.getCategoryProductsUseCase(), viewModelCImpl.getCategoriesUseCase(), viewModelCImpl.getWishlistUseCase(), viewModelCImpl.toggleWishlistUseCase(), viewModelCImpl.addToCartUseCase());

          case 3: // com.cravexa.presentation.customer.profile.CustomerProfileViewModel 
          return (T) new CustomerProfileViewModel(viewModelCImpl.getCustomerProfileUseCase(), viewModelCImpl.logoutUseCase());

          case 4: // com.cravexa.presentation.customer.profile.EditProfileViewModel 
          return (T) new EditProfileViewModel(viewModelCImpl.getCustomerProfileUseCase(), viewModelCImpl.updateCustomerProfileUseCase());

          case 5: // com.cravexa.presentation.explore.ExploreViewModel 
          return (T) new ExploreViewModel(viewModelCImpl.getCategoriesUseCase(), viewModelCImpl.getHomeMarketplaceUseCase(), viewModelCImpl.getWishlistUseCase(), viewModelCImpl.toggleWishlistUseCase(), viewModelCImpl.addToCartUseCase());

          case 6: // com.cravexa.presentation.auth.forgotpassword.ForgotPasswordViewModel 
          return (T) new ForgotPasswordViewModel(viewModelCImpl.forgotPasswordUseCase());

          case 7: // com.cravexa.presentation.home.HomeViewModel 
          return (T) new HomeViewModel(viewModelCImpl.getUserProfileUseCase(), viewModelCImpl.getHomeMarketplaceUseCase(), viewModelCImpl.getWishlistUseCase(), viewModelCImpl.toggleWishlistUseCase(), viewModelCImpl.getCartUseCase(), viewModelCImpl.addToCartUseCase(), viewModelCImpl.logoutUseCase());

          case 8: // com.cravexa.presentation.auth.login.LoginViewModel 
          return (T) new LoginViewModel(viewModelCImpl.loginUseCase(), viewModelCImpl.googleSignInUseCase());

          case 9: // com.cravexa.presentation.onboarding.OnboardingViewModel 
          return (T) new OnboardingViewModel(singletonCImpl.providePreferenceManagerProvider.get());

          case 10: // com.cravexa.presentation.customer.orders.OrderDetailViewModel 
          return (T) new OrderDetailViewModel(viewModelCImpl.getOrderDetailUseCase());

          case 11: // com.cravexa.presentation.customer.orders.OrderTrackingViewModel 
          return (T) new OrderTrackingViewModel(viewModelCImpl.getOrderTrackingUseCase());

          case 12: // com.cravexa.presentation.customer.orders.OrdersViewModel 
          return (T) new OrdersViewModel(viewModelCImpl.getOrdersUseCase());

          case 13: // com.cravexa.presentation.auth.phone.PhoneLoginViewModel 
          return (T) new PhoneLoginViewModel(viewModelCImpl.sendPhoneOtpUseCase(), viewModelCImpl.verifyPhoneOtpUseCase());

          case 14: // com.cravexa.presentation.product.ProductDetailViewModel 
          return (T) new ProductDetailViewModel(viewModelCImpl.savedStateHandle, viewModelCImpl.getProductDetailUseCase(), viewModelCImpl.getWishlistUseCase(), viewModelCImpl.toggleWishlistUseCase(), viewModelCImpl.addToCartUseCase());

          case 15: // com.cravexa.presentation.auth.profile.ProfileSetupViewModel 
          return (T) new ProfileSetupViewModel(viewModelCImpl.getUserProfileUseCase(), viewModelCImpl.saveUserProfileUseCase());

          case 16: // com.cravexa.presentation.seller.publicprofile.PublicSellerProfileViewModel 
          return (T) new PublicSellerProfileViewModel(viewModelCImpl.savedStateHandle, viewModelCImpl.getSellerProductsUseCase(), viewModelCImpl.getWishlistUseCase(), viewModelCImpl.toggleWishlistUseCase(), viewModelCImpl.addToCartUseCase());

          case 17: // com.cravexa.presentation.search.SearchViewModel 
          return (T) new SearchViewModel(viewModelCImpl.savedStateHandle, viewModelCImpl.searchProductsUseCase(), viewModelCImpl.getCategoriesUseCase(), viewModelCImpl.recentSearchUseCases(), viewModelCImpl.getWishlistUseCase(), viewModelCImpl.toggleWishlistUseCase(), viewModelCImpl.addToCartUseCase());

          case 18: // com.cravexa.presentation.seller.dashboard.SellerDashboardViewModel 
          return (T) new SellerDashboardViewModel(viewModelCImpl.getSellerDashboardUseCase(), viewModelCImpl.getSellerOrdersUseCase());

          case 19: // com.cravexa.presentation.seller.earnings.SellerEarningsViewModel 
          return (T) new SellerEarningsViewModel(viewModelCImpl.getSellerEarningsUseCase(), viewModelCImpl.getSellerPayoutsUseCase());

          case 20: // com.cravexa.presentation.seller.orders.SellerOrderDetailViewModel 
          return (T) new SellerOrderDetailViewModel(viewModelCImpl.savedStateHandle, viewModelCImpl.getSellerOrderDetailUseCase(), viewModelCImpl.updateSellerOrderStatusUseCase());

          case 21: // com.cravexa.presentation.seller.orders.SellerOrdersViewModel 
          return (T) new SellerOrdersViewModel(viewModelCImpl.getSellerOrdersUseCase(), viewModelCImpl.updateSellerOrderStatusUseCase());

          case 22: // com.cravexa.presentation.seller.products.SellerProductsViewModel 
          return (T) new SellerProductsViewModel(viewModelCImpl.getSellerKitchenProductsUseCase(), viewModelCImpl.updateProductStockUseCase(), viewModelCImpl.toggleProductAvailabilityUseCase(), viewModelCImpl.deleteSellerProductUseCase());

          case 23: // com.cravexa.presentation.seller.profile.SellerProfileViewModel 
          return (T) new SellerProfileViewModel(viewModelCImpl.getSellerProfileUseCase(), viewModelCImpl.updateSellerProfileUseCase(), viewModelCImpl.submitFssaiUseCase(), viewModelCImpl.logoutUseCase());

          case 24: // com.cravexa.presentation.seller.reviews.SellerReviewsViewModel 
          return (T) new SellerReviewsViewModel(viewModelCImpl.getSellerReviewsUseCase(), viewModelCImpl.replyToReviewUseCase());

          case 25: // com.cravexa.presentation.auth.signup.SignupViewModel 
          return (T) new SignupViewModel(viewModelCImpl.signupUseCase(), viewModelCImpl.googleSignInUseCase());

          case 26: // com.cravexa.presentation.splash.SplashViewModel 
          return (T) new SplashViewModel(singletonCImpl.providePreferenceManagerProvider.get(), viewModelCImpl.getAuthStateUseCase());

          case 27: // com.cravexa.presentation.wishlist.WishlistViewModel 
          return (T) new WishlistViewModel(viewModelCImpl.getWishlistUseCase(), viewModelCImpl.toggleWishlistUseCase(), viewModelCImpl.addToCartUseCase());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends CravexaApp_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    private Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    private ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
        SavedStateHandleHolder savedStateHandleHolderParam) {
      this.singletonCImpl = singletonCImpl;

      initialize(savedStateHandleHolderParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandleHolder savedStateHandleHolderParam) {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle 
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends CravexaApp_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }
  }

  private static final class SingletonCImpl extends CravexaApp_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    private Provider<SellerProductRepositoryImpl> sellerProductRepositoryImplProvider;

    private Provider<PreferenceManager> providePreferenceManagerProvider;

    private Provider<SellerRepositoryImpl> sellerRepositoryImplProvider;

    private Provider<CategoryRepositoryImpl> categoryRepositoryImplProvider;

    private Provider<ImageUploadRepositoryImpl> imageUploadRepositoryImplProvider;

    private Provider<AddressRepositoryImpl> addressRepositoryImplProvider;

    private Provider<ProductRepositoryImpl> productRepositoryImplProvider;

    private Provider<WishlistRepositoryImpl> wishlistRepositoryImplProvider;

    private Provider<CartRepositoryImpl> cartRepositoryImplProvider;

    private Provider<CustomerRepositoryImpl> customerRepositoryImplProvider;

    private Provider<AuthRepositoryImpl> authRepositoryImplProvider;

    private Provider<UserRepositoryImpl> userRepositoryImplProvider;

    private Provider<OrderRepositoryImpl> orderRepositoryImplProvider;

    private Provider<RecentSearchRepositoryImpl> recentSearchRepositoryImplProvider;

    private Provider<SellerOrderRepositoryImpl> sellerOrderRepositoryImplProvider;

    private Provider<SellerEarningsRepositoryImpl> sellerEarningsRepositoryImplProvider;

    private Provider<SellerReviewRepositoryImpl> sellerReviewRepositoryImplProvider;

    private SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.sellerProductRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<SellerProductRepositoryImpl>(singletonCImpl, 0));
      this.providePreferenceManagerProvider = DoubleCheck.provider(new SwitchingProvider<PreferenceManager>(singletonCImpl, 2));
      this.sellerRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<SellerRepositoryImpl>(singletonCImpl, 1));
      this.categoryRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<CategoryRepositoryImpl>(singletonCImpl, 3));
      this.imageUploadRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<ImageUploadRepositoryImpl>(singletonCImpl, 4));
      this.addressRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<AddressRepositoryImpl>(singletonCImpl, 5));
      this.productRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<ProductRepositoryImpl>(singletonCImpl, 6));
      this.wishlistRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<WishlistRepositoryImpl>(singletonCImpl, 7));
      this.cartRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<CartRepositoryImpl>(singletonCImpl, 8));
      this.customerRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<CustomerRepositoryImpl>(singletonCImpl, 9));
      this.authRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<AuthRepositoryImpl>(singletonCImpl, 10));
      this.userRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<UserRepositoryImpl>(singletonCImpl, 11));
      this.orderRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<OrderRepositoryImpl>(singletonCImpl, 12));
      this.recentSearchRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<RecentSearchRepositoryImpl>(singletonCImpl, 13));
      this.sellerOrderRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<SellerOrderRepositoryImpl>(singletonCImpl, 15));
      this.sellerEarningsRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<SellerEarningsRepositoryImpl>(singletonCImpl, 14));
      this.sellerReviewRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<SellerReviewRepositoryImpl>(singletonCImpl, 16));
    }

    @Override
    public void injectCravexaApp(CravexaApp arg0) {
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return Collections.<Boolean>emptySet();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.cravexa.data.repository.SellerProductRepositoryImpl 
          return (T) new SellerProductRepositoryImpl();

          case 1: // com.cravexa.data.repository.SellerRepositoryImpl 
          return (T) new SellerRepositoryImpl(singletonCImpl.providePreferenceManagerProvider.get());

          case 2: // com.cravexa.data.local.PreferenceManager 
          return (T) AppModule_ProvidePreferenceManagerFactory.providePreferenceManager(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 3: // com.cravexa.data.repository.CategoryRepositoryImpl 
          return (T) new CategoryRepositoryImpl();

          case 4: // com.cravexa.data.repository.ImageUploadRepositoryImpl 
          return (T) new ImageUploadRepositoryImpl();

          case 5: // com.cravexa.data.repository.AddressRepositoryImpl 
          return (T) new AddressRepositoryImpl(singletonCImpl.providePreferenceManagerProvider.get());

          case 6: // com.cravexa.data.repository.ProductRepositoryImpl 
          return (T) new ProductRepositoryImpl();

          case 7: // com.cravexa.data.repository.WishlistRepositoryImpl 
          return (T) new WishlistRepositoryImpl(singletonCImpl.providePreferenceManagerProvider.get());

          case 8: // com.cravexa.data.repository.CartRepositoryImpl 
          return (T) new CartRepositoryImpl(singletonCImpl.providePreferenceManagerProvider.get());

          case 9: // com.cravexa.data.repository.CustomerRepositoryImpl 
          return (T) new CustomerRepositoryImpl(singletonCImpl.providePreferenceManagerProvider.get());

          case 10: // com.cravexa.data.repository.AuthRepositoryImpl 
          return (T) new AuthRepositoryImpl(singletonCImpl.providePreferenceManagerProvider.get());

          case 11: // com.cravexa.data.repository.UserRepositoryImpl 
          return (T) new UserRepositoryImpl(singletonCImpl.providePreferenceManagerProvider.get());

          case 12: // com.cravexa.data.repository.OrderRepositoryImpl 
          return (T) new OrderRepositoryImpl();

          case 13: // com.cravexa.data.repository.RecentSearchRepositoryImpl 
          return (T) new RecentSearchRepositoryImpl(singletonCImpl.providePreferenceManagerProvider.get());

          case 14: // com.cravexa.data.repository.SellerEarningsRepositoryImpl 
          return (T) new SellerEarningsRepositoryImpl(singletonCImpl.sellerProductRepositoryImplProvider.get(), singletonCImpl.sellerOrderRepositoryImplProvider.get());

          case 15: // com.cravexa.data.repository.SellerOrderRepositoryImpl 
          return (T) new SellerOrderRepositoryImpl();

          case 16: // com.cravexa.data.repository.SellerReviewRepositoryImpl 
          return (T) new SellerReviewRepositoryImpl();

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
