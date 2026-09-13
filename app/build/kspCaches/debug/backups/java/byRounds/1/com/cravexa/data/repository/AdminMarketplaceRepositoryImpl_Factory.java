package com.cravexa.data.repository;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
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
public final class AdminMarketplaceRepositoryImpl_Factory implements Factory<AdminMarketplaceRepositoryImpl> {
  @Override
  public AdminMarketplaceRepositoryImpl get() {
    return newInstance();
  }

  public static AdminMarketplaceRepositoryImpl_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static AdminMarketplaceRepositoryImpl newInstance() {
    return new AdminMarketplaceRepositoryImpl();
  }

  private static final class InstanceHolder {
    private static final AdminMarketplaceRepositoryImpl_Factory INSTANCE = new AdminMarketplaceRepositoryImpl_Factory();
  }
}
