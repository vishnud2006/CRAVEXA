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
public final class OrderRepositoryImpl_Factory implements Factory<OrderRepositoryImpl> {
  @Override
  public OrderRepositoryImpl get() {
    return newInstance();
  }

  public static OrderRepositoryImpl_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static OrderRepositoryImpl newInstance() {
    return new OrderRepositoryImpl();
  }

  private static final class InstanceHolder {
    private static final OrderRepositoryImpl_Factory INSTANCE = new OrderRepositoryImpl_Factory();
  }
}
