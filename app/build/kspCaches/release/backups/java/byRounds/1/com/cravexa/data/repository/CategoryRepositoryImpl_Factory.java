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
public final class CategoryRepositoryImpl_Factory implements Factory<CategoryRepositoryImpl> {
  @Override
  public CategoryRepositoryImpl get() {
    return newInstance();
  }

  public static CategoryRepositoryImpl_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static CategoryRepositoryImpl newInstance() {
    return new CategoryRepositoryImpl();
  }

  private static final class InstanceHolder {
    private static final CategoryRepositoryImpl_Factory INSTANCE = new CategoryRepositoryImpl_Factory();
  }
}
