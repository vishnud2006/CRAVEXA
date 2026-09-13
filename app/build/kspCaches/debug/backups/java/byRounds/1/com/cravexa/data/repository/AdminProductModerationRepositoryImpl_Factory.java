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
public final class AdminProductModerationRepositoryImpl_Factory implements Factory<AdminProductModerationRepositoryImpl> {
  @Override
  public AdminProductModerationRepositoryImpl get() {
    return newInstance();
  }

  public static AdminProductModerationRepositoryImpl_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static AdminProductModerationRepositoryImpl newInstance() {
    return new AdminProductModerationRepositoryImpl();
  }

  private static final class InstanceHolder {
    private static final AdminProductModerationRepositoryImpl_Factory INSTANCE = new AdminProductModerationRepositoryImpl_Factory();
  }
}
