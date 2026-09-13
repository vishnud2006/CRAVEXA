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
public final class AdminComplaintRepositoryImpl_Factory implements Factory<AdminComplaintRepositoryImpl> {
  @Override
  public AdminComplaintRepositoryImpl get() {
    return newInstance();
  }

  public static AdminComplaintRepositoryImpl_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static AdminComplaintRepositoryImpl newInstance() {
    return new AdminComplaintRepositoryImpl();
  }

  private static final class InstanceHolder {
    private static final AdminComplaintRepositoryImpl_Factory INSTANCE = new AdminComplaintRepositoryImpl_Factory();
  }
}
