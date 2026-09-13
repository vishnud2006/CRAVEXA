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
public final class SellerOrderRepositoryImpl_Factory implements Factory<SellerOrderRepositoryImpl> {
  @Override
  public SellerOrderRepositoryImpl get() {
    return newInstance();
  }

  public static SellerOrderRepositoryImpl_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static SellerOrderRepositoryImpl newInstance() {
    return new SellerOrderRepositoryImpl();
  }

  private static final class InstanceHolder {
    private static final SellerOrderRepositoryImpl_Factory INSTANCE = new SellerOrderRepositoryImpl_Factory();
  }
}
