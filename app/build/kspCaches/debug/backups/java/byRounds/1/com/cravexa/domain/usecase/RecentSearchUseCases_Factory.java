package com.cravexa.domain.usecase;

import com.cravexa.domain.repository.RecentSearchRepository;
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
public final class RecentSearchUseCases_Factory implements Factory<RecentSearchUseCases> {
  private final Provider<RecentSearchRepository> recentSearchRepositoryProvider;

  public RecentSearchUseCases_Factory(
      Provider<RecentSearchRepository> recentSearchRepositoryProvider) {
    this.recentSearchRepositoryProvider = recentSearchRepositoryProvider;
  }

  @Override
  public RecentSearchUseCases get() {
    return newInstance(recentSearchRepositoryProvider.get());
  }

  public static RecentSearchUseCases_Factory create(
      Provider<RecentSearchRepository> recentSearchRepositoryProvider) {
    return new RecentSearchUseCases_Factory(recentSearchRepositoryProvider);
  }

  public static RecentSearchUseCases newInstance(RecentSearchRepository recentSearchRepository) {
    return new RecentSearchUseCases(recentSearchRepository);
  }
}
