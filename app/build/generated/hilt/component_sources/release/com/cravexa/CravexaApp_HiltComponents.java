package com.cravexa;

import com.cravexa.core.di.AppModule;
import com.cravexa.core.di.AuthModule;
import com.cravexa.core.di.Phase3Module;
import com.cravexa.core.di.Phase4Module;
import com.cravexa.core.di.Phase5Module;
import com.cravexa.presentation.auth.forgotpassword.ForgotPasswordViewModel_HiltModules;
import com.cravexa.presentation.auth.login.LoginViewModel_HiltModules;
import com.cravexa.presentation.auth.phone.PhoneLoginViewModel_HiltModules;
import com.cravexa.presentation.auth.profile.ProfileSetupViewModel_HiltModules;
import com.cravexa.presentation.auth.signup.SignupViewModel_HiltModules;
import com.cravexa.presentation.category.CategoryProductsViewModel_HiltModules;
import com.cravexa.presentation.customer.address.AddressViewModel_HiltModules;
import com.cravexa.presentation.customer.orders.OrderDetailViewModel_HiltModules;
import com.cravexa.presentation.customer.orders.OrderTrackingViewModel_HiltModules;
import com.cravexa.presentation.customer.orders.OrdersViewModel_HiltModules;
import com.cravexa.presentation.customer.profile.CustomerProfileViewModel_HiltModules;
import com.cravexa.presentation.customer.profile.EditProfileViewModel_HiltModules;
import com.cravexa.presentation.explore.ExploreViewModel_HiltModules;
import com.cravexa.presentation.home.HomeViewModel_HiltModules;
import com.cravexa.presentation.onboarding.OnboardingViewModel_HiltModules;
import com.cravexa.presentation.product.ProductDetailViewModel_HiltModules;
import com.cravexa.presentation.search.SearchViewModel_HiltModules;
import com.cravexa.presentation.seller.dashboard.SellerDashboardViewModel_HiltModules;
import com.cravexa.presentation.seller.earnings.SellerEarningsViewModel_HiltModules;
import com.cravexa.presentation.seller.orders.SellerOrderDetailViewModel_HiltModules;
import com.cravexa.presentation.seller.orders.SellerOrdersViewModel_HiltModules;
import com.cravexa.presentation.seller.products.AddEditProductViewModel_HiltModules;
import com.cravexa.presentation.seller.products.SellerProductsViewModel_HiltModules;
import com.cravexa.presentation.seller.profile.SellerProfileViewModel_HiltModules;
import com.cravexa.presentation.seller.publicprofile.PublicSellerProfileViewModel_HiltModules;
import com.cravexa.presentation.seller.reviews.SellerReviewsViewModel_HiltModules;
import com.cravexa.presentation.splash.SplashViewModel_HiltModules;
import com.cravexa.presentation.wishlist.WishlistViewModel_HiltModules;
import dagger.Binds;
import dagger.Component;
import dagger.Module;
import dagger.Subcomponent;
import dagger.hilt.android.components.ActivityComponent;
import dagger.hilt.android.components.ActivityRetainedComponent;
import dagger.hilt.android.components.FragmentComponent;
import dagger.hilt.android.components.ServiceComponent;
import dagger.hilt.android.components.ViewComponent;
import dagger.hilt.android.components.ViewModelComponent;
import dagger.hilt.android.components.ViewWithFragmentComponent;
import dagger.hilt.android.flags.FragmentGetContextFix;
import dagger.hilt.android.flags.HiltWrapper_FragmentGetContextFix_FragmentGetContextFixModule;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.HiltViewModelFactory;
import dagger.hilt.android.internal.lifecycle.HiltWrapper_DefaultViewModelFactories_ActivityModule;
import dagger.hilt.android.internal.lifecycle.HiltWrapper_HiltViewModelFactory_ActivityCreatorEntryPoint;
import dagger.hilt.android.internal.lifecycle.HiltWrapper_HiltViewModelFactory_ViewModelModule;
import dagger.hilt.android.internal.managers.ActivityComponentManager;
import dagger.hilt.android.internal.managers.FragmentComponentManager;
import dagger.hilt.android.internal.managers.HiltWrapper_ActivityRetainedComponentManager_ActivityRetainedComponentBuilderEntryPoint;
import dagger.hilt.android.internal.managers.HiltWrapper_ActivityRetainedComponentManager_ActivityRetainedLifecycleEntryPoint;
import dagger.hilt.android.internal.managers.HiltWrapper_ActivityRetainedComponentManager_LifecycleModule;
import dagger.hilt.android.internal.managers.HiltWrapper_SavedStateHandleModule;
import dagger.hilt.android.internal.managers.ServiceComponentManager;
import dagger.hilt.android.internal.managers.ViewComponentManager;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.HiltWrapper_ActivityModule;
import dagger.hilt.android.scopes.ActivityRetainedScoped;
import dagger.hilt.android.scopes.ActivityScoped;
import dagger.hilt.android.scopes.FragmentScoped;
import dagger.hilt.android.scopes.ServiceScoped;
import dagger.hilt.android.scopes.ViewModelScoped;
import dagger.hilt.android.scopes.ViewScoped;
import dagger.hilt.components.SingletonComponent;
import dagger.hilt.internal.GeneratedComponent;
import dagger.hilt.migration.DisableInstallInCheck;
import javax.annotation.processing.Generated;
import javax.inject.Singleton;

@Generated("dagger.hilt.processor.internal.root.RootProcessor")
public final class CravexaApp_HiltComponents {
  private CravexaApp_HiltComponents() {
  }

  @Module(
      subcomponents = ServiceC.class
  )
  @DisableInstallInCheck
  @Generated("dagger.hilt.processor.internal.root.RootProcessor")
  abstract interface ServiceCBuilderModule {
    @Binds
    ServiceComponentBuilder bind(ServiceC.Builder builder);
  }

  @Module(
      subcomponents = ActivityRetainedC.class
  )
  @DisableInstallInCheck
  @Generated("dagger.hilt.processor.internal.root.RootProcessor")
  abstract interface ActivityRetainedCBuilderModule {
    @Binds
    ActivityRetainedComponentBuilder bind(ActivityRetainedC.Builder builder);
  }

  @Module(
      subcomponents = ActivityC.class
  )
  @DisableInstallInCheck
  @Generated("dagger.hilt.processor.internal.root.RootProcessor")
  abstract interface ActivityCBuilderModule {
    @Binds
    ActivityComponentBuilder bind(ActivityC.Builder builder);
  }

  @Module(
      subcomponents = ViewModelC.class
  )
  @DisableInstallInCheck
  @Generated("dagger.hilt.processor.internal.root.RootProcessor")
  abstract interface ViewModelCBuilderModule {
    @Binds
    ViewModelComponentBuilder bind(ViewModelC.Builder builder);
  }

  @Module(
      subcomponents = ViewC.class
  )
  @DisableInstallInCheck
  @Generated("dagger.hilt.processor.internal.root.RootProcessor")
  abstract interface ViewCBuilderModule {
    @Binds
    ViewComponentBuilder bind(ViewC.Builder builder);
  }

  @Module(
      subcomponents = FragmentC.class
  )
  @DisableInstallInCheck
  @Generated("dagger.hilt.processor.internal.root.RootProcessor")
  abstract interface FragmentCBuilderModule {
    @Binds
    FragmentComponentBuilder bind(FragmentC.Builder builder);
  }

  @Module(
      subcomponents = ViewWithFragmentC.class
  )
  @DisableInstallInCheck
  @Generated("dagger.hilt.processor.internal.root.RootProcessor")
  abstract interface ViewWithFragmentCBuilderModule {
    @Binds
    ViewWithFragmentComponentBuilder bind(ViewWithFragmentC.Builder builder);
  }

  @Component(
      modules = {
          AppModule.class,
          ApplicationContextModule.class,
          AuthModule.class,
          ActivityRetainedCBuilderModule.class,
          ServiceCBuilderModule.class,
          HiltWrapper_FragmentGetContextFix_FragmentGetContextFixModule.class,
          Phase3Module.class,
          Phase4Module.class,
          Phase5Module.class
      }
  )
  @Singleton
  public abstract static class SingletonC implements CravexaApp_GeneratedInjector,
      FragmentGetContextFix.FragmentGetContextFixEntryPoint,
      HiltWrapper_ActivityRetainedComponentManager_ActivityRetainedComponentBuilderEntryPoint,
      ServiceComponentManager.ServiceComponentBuilderEntryPoint,
      SingletonComponent,
      GeneratedComponent {
  }

  @Subcomponent
  @ServiceScoped
  public abstract static class ServiceC implements ServiceComponent,
      GeneratedComponent {
    @Subcomponent.Builder
    abstract interface Builder extends ServiceComponentBuilder {
    }
  }

  @Subcomponent(
      modules = {
          AddEditProductViewModel_HiltModules.KeyModule.class,
          AddressViewModel_HiltModules.KeyModule.class,
          CategoryProductsViewModel_HiltModules.KeyModule.class,
          ActivityCBuilderModule.class,
          ViewModelCBuilderModule.class,
          CustomerProfileViewModel_HiltModules.KeyModule.class,
          EditProfileViewModel_HiltModules.KeyModule.class,
          ExploreViewModel_HiltModules.KeyModule.class,
          ForgotPasswordViewModel_HiltModules.KeyModule.class,
          HiltWrapper_ActivityRetainedComponentManager_LifecycleModule.class,
          HiltWrapper_SavedStateHandleModule.class,
          HomeViewModel_HiltModules.KeyModule.class,
          LoginViewModel_HiltModules.KeyModule.class,
          OnboardingViewModel_HiltModules.KeyModule.class,
          OrderDetailViewModel_HiltModules.KeyModule.class,
          OrderTrackingViewModel_HiltModules.KeyModule.class,
          OrdersViewModel_HiltModules.KeyModule.class,
          PhoneLoginViewModel_HiltModules.KeyModule.class,
          ProductDetailViewModel_HiltModules.KeyModule.class,
          ProfileSetupViewModel_HiltModules.KeyModule.class,
          PublicSellerProfileViewModel_HiltModules.KeyModule.class,
          SearchViewModel_HiltModules.KeyModule.class,
          SellerDashboardViewModel_HiltModules.KeyModule.class,
          SellerEarningsViewModel_HiltModules.KeyModule.class,
          SellerOrderDetailViewModel_HiltModules.KeyModule.class,
          SellerOrdersViewModel_HiltModules.KeyModule.class,
          SellerProductsViewModel_HiltModules.KeyModule.class,
          SellerProfileViewModel_HiltModules.KeyModule.class,
          SellerReviewsViewModel_HiltModules.KeyModule.class,
          SignupViewModel_HiltModules.KeyModule.class,
          SplashViewModel_HiltModules.KeyModule.class,
          WishlistViewModel_HiltModules.KeyModule.class
      }
  )
  @ActivityRetainedScoped
  public abstract static class ActivityRetainedC implements ActivityRetainedComponent,
      ActivityComponentManager.ActivityComponentBuilderEntryPoint,
      HiltWrapper_ActivityRetainedComponentManager_ActivityRetainedLifecycleEntryPoint,
      GeneratedComponent {
    @Subcomponent.Builder
    abstract interface Builder extends ActivityRetainedComponentBuilder {
    }
  }

  @Subcomponent(
      modules = {
          FragmentCBuilderModule.class,
          ViewCBuilderModule.class,
          HiltWrapper_ActivityModule.class,
          HiltWrapper_DefaultViewModelFactories_ActivityModule.class
      }
  )
  @ActivityScoped
  public abstract static class ActivityC implements MainActivity_GeneratedInjector,
      ActivityComponent,
      DefaultViewModelFactories.ActivityEntryPoint,
      HiltWrapper_HiltViewModelFactory_ActivityCreatorEntryPoint,
      FragmentComponentManager.FragmentComponentBuilderEntryPoint,
      ViewComponentManager.ViewComponentBuilderEntryPoint,
      GeneratedComponent {
    @Subcomponent.Builder
    abstract interface Builder extends ActivityComponentBuilder {
    }
  }

  @Subcomponent(
      modules = {
          AddEditProductViewModel_HiltModules.BindsModule.class,
          AddressViewModel_HiltModules.BindsModule.class,
          CategoryProductsViewModel_HiltModules.BindsModule.class,
          CustomerProfileViewModel_HiltModules.BindsModule.class,
          EditProfileViewModel_HiltModules.BindsModule.class,
          ExploreViewModel_HiltModules.BindsModule.class,
          ForgotPasswordViewModel_HiltModules.BindsModule.class,
          HiltWrapper_HiltViewModelFactory_ViewModelModule.class,
          HomeViewModel_HiltModules.BindsModule.class,
          LoginViewModel_HiltModules.BindsModule.class,
          OnboardingViewModel_HiltModules.BindsModule.class,
          OrderDetailViewModel_HiltModules.BindsModule.class,
          OrderTrackingViewModel_HiltModules.BindsModule.class,
          OrdersViewModel_HiltModules.BindsModule.class,
          PhoneLoginViewModel_HiltModules.BindsModule.class,
          ProductDetailViewModel_HiltModules.BindsModule.class,
          ProfileSetupViewModel_HiltModules.BindsModule.class,
          PublicSellerProfileViewModel_HiltModules.BindsModule.class,
          SearchViewModel_HiltModules.BindsModule.class,
          SellerDashboardViewModel_HiltModules.BindsModule.class,
          SellerEarningsViewModel_HiltModules.BindsModule.class,
          SellerOrderDetailViewModel_HiltModules.BindsModule.class,
          SellerOrdersViewModel_HiltModules.BindsModule.class,
          SellerProductsViewModel_HiltModules.BindsModule.class,
          SellerProfileViewModel_HiltModules.BindsModule.class,
          SellerReviewsViewModel_HiltModules.BindsModule.class,
          SignupViewModel_HiltModules.BindsModule.class,
          SplashViewModel_HiltModules.BindsModule.class,
          WishlistViewModel_HiltModules.BindsModule.class
      }
  )
  @ViewModelScoped
  public abstract static class ViewModelC implements ViewModelComponent,
      HiltViewModelFactory.ViewModelFactoriesEntryPoint,
      GeneratedComponent {
    @Subcomponent.Builder
    abstract interface Builder extends ViewModelComponentBuilder {
    }
  }

  @Subcomponent
  @ViewScoped
  public abstract static class ViewC implements ViewComponent,
      GeneratedComponent {
    @Subcomponent.Builder
    abstract interface Builder extends ViewComponentBuilder {
    }
  }

  @Subcomponent(
      modules = ViewWithFragmentCBuilderModule.class
  )
  @FragmentScoped
  public abstract static class FragmentC implements FragmentComponent,
      DefaultViewModelFactories.FragmentEntryPoint,
      ViewComponentManager.ViewWithFragmentComponentBuilderEntryPoint,
      GeneratedComponent {
    @Subcomponent.Builder
    abstract interface Builder extends FragmentComponentBuilder {
    }
  }

  @Subcomponent
  @ViewScoped
  public abstract static class ViewWithFragmentC implements ViewWithFragmentComponent,
      GeneratedComponent {
    @Subcomponent.Builder
    abstract interface Builder extends ViewWithFragmentComponentBuilder {
    }
  }
}
