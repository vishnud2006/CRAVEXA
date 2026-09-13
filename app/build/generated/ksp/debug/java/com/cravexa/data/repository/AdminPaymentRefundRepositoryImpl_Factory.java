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
public final class AdminPaymentRefundRepositoryImpl_Factory implements Factory<AdminPaymentRefundRepositoryImpl> {
  @Override
  public AdminPaymentRefundRepositoryImpl get() {
    return newInstance();
  }

  public static AdminPaymentRefundRepositoryImpl_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static AdminPaymentRefundRepositoryImpl newInstance() {
    return new AdminPaymentRefundRepositoryImpl();
  }

  private static final class InstanceHolder {
    private static final AdminPaymentRefundRepositoryImpl_Factory INSTANCE = new AdminPaymentRefundRepositoryImpl_Factory();
  }
}
