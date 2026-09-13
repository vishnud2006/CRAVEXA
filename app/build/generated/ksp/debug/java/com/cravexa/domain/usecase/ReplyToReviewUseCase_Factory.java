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
public final class ReplyToReviewUseCase_Factory implements Factory<ReplyToReviewUseCase> {
  private final Provider<SellerReviewRepository> reviewRepositoryProvider;

  public ReplyToReviewUseCase_Factory(Provider<SellerReviewRepository> reviewRepositoryProvider) {
    this.reviewRepositoryProvider = reviewRepositoryProvider;
  }

  @Override
  public ReplyToReviewUseCase get() {
    return newInstance(reviewRepositoryProvider.get());
  }

  public static ReplyToReviewUseCase_Factory create(
      Provider<SellerReviewRepository> reviewRepositoryProvider) {
    return new ReplyToReviewUseCase_Factory(reviewRepositoryProvider);
  }

  public static ReplyToReviewUseCase newInstance(SellerReviewRepository reviewRepository) {
    return new ReplyToReviewUseCase(reviewRepository);
  }
}
