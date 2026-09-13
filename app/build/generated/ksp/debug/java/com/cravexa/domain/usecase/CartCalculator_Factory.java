package com.cravexa.domain.usecase;

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
public final class CartCalculator_Factory implements Factory<CartCalculator> {
  @Override
  public CartCalculator get() {
    return newInstance();
  }

  public static CartCalculator_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static CartCalculator newInstance() {
    return new CartCalculator();
  }

  private static final class InstanceHolder {
    private static final CartCalculator_Factory INSTANCE = new CartCalculator_Factory();
  }
}
