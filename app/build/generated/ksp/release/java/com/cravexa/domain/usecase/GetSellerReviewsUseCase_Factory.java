package com.cravexa.domain.usecase;

import com.cravexa.domain.repository.SellerReviewRepository;
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
public final class GetSellerReviewsUseCase_Factory implements Factory<GetSellerReviewsUseCase> {
  private final Provider<SellerReviewRepository> reviewRepositoryProvider;

  public GetSellerReviewsUseCase_Factory(
      Provider<SellerReviewRepository> reviewRepositoryProvider) {
    this.reviewRepositoryProvider = reviewRepositoryProvider;
  }

  @Override
  public GetSellerReviewsUseCase get() {
    return newInstance(reviewRepositoryProvider.get());
  }

  public static GetSellerReviewsUseCase_Factory create(
      Provider<SellerReviewRepository> reviewRepositoryProvider) {
    return new GetSellerReviewsUseCase_Factory(reviewRepositoryProvider);
  }

  public static GetSellerReviewsUseCase newInstance(SellerReviewRepository reviewRepository) {
    return new GetSellerReviewsUseCase(reviewRepository);
  }
}
