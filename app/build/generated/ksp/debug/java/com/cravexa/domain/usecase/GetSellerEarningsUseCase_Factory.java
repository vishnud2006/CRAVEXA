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
public final class GetSellerEarningsUseCase_Factory implements Factory<GetSellerEarningsUseCase> {
  private final Provider<SellerEarningsRepository> earningsRepositoryProvider;

  public GetSellerEarningsUseCase_Factory(
      Provider<SellerEarningsRepository> earningsRepositoryProvider) {
    this.earningsRepositoryProvider = earningsRepositoryProvider;
  }

  @Override
  public GetSellerEarningsUseCase get() {
    return newInstance(earningsRepositoryProvider.get());
  }

  public static GetSellerEarningsUseCase_Factory create(
      Provider<SellerEarningsRepository> earningsRepositoryProvider) {
    return new GetSellerEarningsUseCase_Factory(earningsRepositoryProvider);
  }

  public static GetSellerEarningsUseCase newInstance(SellerEarningsRepository earningsRepository) {
    return new GetSellerEarningsUseCase(earningsRepository);
  }
}
