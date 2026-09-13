package com.cravexa.domain.usecase;

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
public final class GetSellerProfileUseCase_Factory implements Factory<GetSellerProfileUseCase> {
  private final Provider<SellerRepository> sellerRepositoryProvider;

  public GetSellerProfileUseCase_Factory(Provider<SellerRepository> sellerRepositoryProvider) {
    this.sellerRepositoryProvider = sellerRepositoryProvider;
  }

  @Override
  public GetSellerProfileUseCase get() {
    return newInstance(sellerRepositoryProvider.get());
  }

  public static GetSellerProfileUseCase_Factory create(
      Provider<SellerRepository> sellerRepositoryProvider) {
    return new GetSellerProfileUseCase_Factory(sellerRepositoryProvider);
  }

  public static GetSellerProfileUseCase newInstance(SellerRepository sellerRepository) {
    return new GetSellerProfileUseCase(sellerRepository);
  }
}
