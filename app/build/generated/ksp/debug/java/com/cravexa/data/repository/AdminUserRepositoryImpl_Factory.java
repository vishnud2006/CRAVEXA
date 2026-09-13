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
public final class AdminUserRepositoryImpl_Factory implements Factory<AdminUserRepositoryImpl> {
  @Override
  public AdminUserRepositoryImpl get() {
    return newInstance();
  }

  public static AdminUserRepositoryImpl_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static AdminUserRepositoryImpl newInstance() {
    return new AdminUserRepositoryImpl();
  }

  private static final class InstanceHolder {
    private static final AdminUserRepositoryImpl_Factory INSTANCE = new AdminUserRepositoryImpl_Factory();
  }
}
