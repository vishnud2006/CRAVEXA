package com.cravexa.domain.usecase;

import com.cravexa.domain.repository.SellerEarningsRepository;
import com.cravexa.domain.repository.SellerRepository;
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
public final class GetSellerDashboardUseCase_Factory implements Factory<GetSellerDashboardUseCase> {
  private final Provider<SellerEarningsRepository> earningsRepositoryProvider;

  private final Provider<SellerRepository> sellerRepositoryProvider;

  public GetSellerDashboardUseCase_Factory(
      Provider<SellerEarningsRepository> earningsRepositoryProvider,
      Provider<SellerRepository> sellerRepositoryProvider) {
    this.earningsRepositoryProvider = earningsRepositoryProvider;
    this.sellerRepositoryProvider = sellerRepositoryProvider;
  }

  @Override
  public GetSellerDashboardUseCase get() {
    return newInstance(earningsRepositoryProvider.get(), sellerRepositoryProvider.get());
  }

  public static GetSellerDashboardUseCase_Factory create(
      Provider<SellerEarningsRepository> earningsRepositoryProvider,
      Provider<SellerRepository> sellerRepositoryProvider) {
    return new GetSellerDashboardUseCase_Factory(earningsRepositoryProvider, sellerRepositoryProvider);
  }

  public static GetSellerDashboardUseCase newInstance(SellerEarningsRepository earningsRepository,
      SellerRepository sellerRepository) {
    return new GetSellerDashboardUseCase(earningsRepository, sellerRepository);
  }
}
