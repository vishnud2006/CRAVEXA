package com.cravexa.domain.usecase;

import com.cravexa.domain.repository.SellerEarningsRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class GetSellerPayoutsUseCase_Factory implements Factory<GetSellerPayoutsUseCase> {
  private final Provider<SellerEarningsRepository> earningsRepositoryProvider;

  public GetSellerPayoutsUseCase_Factory(
      Provider<SellerEarningsRepository> earningsRepositoryProvider) {
    this.earningsRepositoryProvider = earningsRepositoryProvider;
  }

  @Override
  public GetSellerPayoutsUseCase get() {
    return newInstance(earningsRepositoryProvider.get());
  }

  public static GetSellerPayoutsUseCase_Factory create(
      Provider<SellerEarningsRepository> earningsRepositoryProvider) {
    return new GetSellerPayoutsUseCase_Factory(earningsRepositoryProvider);
  }

  public static GetSellerPayoutsUseCase newInstance(SellerEarningsRepository earningsRepository) {
    return new GetSellerPayoutsUseCase(earningsRepository);
  }
}
