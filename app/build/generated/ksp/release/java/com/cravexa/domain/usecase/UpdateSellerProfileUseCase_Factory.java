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
public final class UpdateSellerProfileUseCase_Factory implements Factory<UpdateSellerProfileUseCase> {
  private final Provider<SellerRepository> sellerRepositoryProvider;

  public UpdateSellerProfileUseCase_Factory(Provider<SellerRepository> sellerRepositoryProvider) {
    this.sellerRepositoryProvider = sellerRepositoryProvider;
  }

  @Override
  public UpdateSellerProfileUseCase get() {
    return newInstance(sellerRepositoryProvider.get());
  }

  public static UpdateSellerProfileUseCase_Factory create(
      Provider<SellerRepository> sellerRepositoryProvider) {
    return new UpdateSellerProfileUseCase_Factory(sellerRepositoryProvider);
  }

  public static UpdateSellerProfileUseCase newInstance(SellerRepository sellerRepository) {
    return new UpdateSellerProfileUseCase(sellerRepository);
  }
}
