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
public final class SellerReviewRepositoryImpl_Factory implements Factory<SellerReviewRepositoryImpl> {
  @Override
  public SellerReviewRepositoryImpl get() {
    return newInstance();
  }

  public static SellerReviewRepositoryImpl_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static SellerReviewRepositoryImpl newInstance() {
    return new SellerReviewRepositoryImpl();
  }

  private static final class InstanceHolder {
    private static final SellerReviewRepositoryImpl_Factory INSTANCE = new SellerReviewRepositoryImpl_Factory();
  }
}
