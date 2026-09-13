package com.cravexa.presentation.seller.reviews;

import com.cravexa.domain.usecase.GetSellerReviewsUseCase;
import com.cravexa.domain.usecase.ReplyToReviewUseCase;
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
public final class SellerReviewsViewModel_Factory implements Factory<SellerReviewsViewModel> {
  private final Provider<GetSellerReviewsUseCase> getSellerReviewsUseCaseProvider;

  private final Provider<ReplyToReviewUseCase> replyToReviewUseCaseProvider;

  public SellerReviewsViewModel_Factory(
      Provider<GetSellerReviewsUseCase> getSellerReviewsUseCaseProvider,
      Provider<ReplyToReviewUseCase> replyToReviewUseCaseProvider) {
    this.getSellerReviewsUseCaseProvider = getSellerReviewsUseCaseProvider;
    this.replyToReviewUseCaseProvider = replyToReviewUseCaseProvider;
  }

  @Override
  public SellerReviewsViewModel get() {
    return newInstance(getSellerReviewsUseCaseProvider.get(), replyToReviewUseCaseProvider.get());
  }

  public static SellerReviewsViewModel_Factory create(
      Provider<GetSellerReviewsUseCase> getSellerReviewsUseCaseProvider,
      Provider<ReplyToReviewUseCase> replyToReviewUseCaseProvider) {
    return new SellerReviewsViewModel_Factory(getSellerReviewsUseCaseProvider, replyToReviewUseCaseProvider);
  }

  public static SellerReviewsViewModel newInstance(GetSellerReviewsUseCase getSellerReviewsUseCase,
      ReplyToReviewUseCase replyToReviewUseCase) {
    return new SellerReviewsViewModel(getSellerReviewsUseCase, replyToReviewUseCase);
  }
}
