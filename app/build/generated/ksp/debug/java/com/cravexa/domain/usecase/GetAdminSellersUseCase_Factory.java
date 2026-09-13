package com.cravexa.domain.usecase;

import com.cravexa.domain.repository.AdminSellerRepository;
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
public final class GetAdminSellersUseCase_Factory implements Factory<GetAdminSellersUseCase> {
  private final Provider<AdminSellerRepository> repositoryProvider;

  public GetAdminSellersUseCase_Factory(Provider<AdminSellerRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public GetAdminSellersUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static GetAdminSellersUseCase_Factory create(
      Provider<AdminSellerRepository> repositoryProvider) {
    return new GetAdminSellersUseCase_Factory(repositoryProvider);
  }

  public static GetAdminSellersUseCase newInstance(AdminSellerRepository repository) {
    return new GetAdminSellersUseCase(repository);
  }
}
