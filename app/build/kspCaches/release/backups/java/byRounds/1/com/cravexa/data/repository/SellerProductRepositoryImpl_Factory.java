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
public final class SellerProductRepositoryImpl_Factory implements Factory<SellerProductRepositoryImpl> {
  @Override
  public SellerProductRepositoryImpl get() {
    return newInstance();
  }

  public static SellerProductRepositoryImpl_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static SellerProductRepositoryImpl newInstance() {
    return new SellerProductRepositoryImpl();
  }

  private static final class InstanceHolder {
    private static final SellerProductRepositoryImpl_Factory INSTANCE = new SellerProductRepositoryImpl_Factory();
  }
}
