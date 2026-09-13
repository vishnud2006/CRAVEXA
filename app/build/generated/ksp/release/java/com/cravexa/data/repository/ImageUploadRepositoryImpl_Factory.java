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
public final class ImageUploadRepositoryImpl_Factory implements Factory<ImageUploadRepositoryImpl> {
  @Override
  public ImageUploadRepositoryImpl get() {
    return newInstance();
  }

  public static ImageUploadRepositoryImpl_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static ImageUploadRepositoryImpl newInstance() {
    return new ImageUploadRepositoryImpl();
  }

  private static final class InstanceHolder {
    private static final ImageUploadRepositoryImpl_Factory INSTANCE = new ImageUploadRepositoryImpl_Factory();
  }
}
