package com.cravexa.domain.usecase;

import com.cravexa.domain.repository.ImageUploadRepository;
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
public final class UploadProductImageUseCase_Factory implements Factory<UploadProductImageUseCase> {
  private final Provider<ImageUploadRepository> imageUploadRepositoryProvider;

  public UploadProductImageUseCase_Factory(
      Provider<ImageUploadRepository> imageUploadRepositoryProvider) {
    this.imageUploadRepositoryProvider = imageUploadRepositoryProvider;
  }

  @Override
  public UploadProductImageUseCase get() {
    return newInstance(imageUploadRepositoryProvider.get());
  }

  public static UploadProductImageUseCase_Factory create(
      Provider<ImageUploadRepository> imageUploadRepositoryProvider) {
    return new UploadProductImageUseCase_Factory(imageUploadRepositoryProvider);
  }

  public static UploadProductImageUseCase newInstance(ImageUploadRepository imageUploadRepository) {
    return new UploadProductImageUseCase(imageUploadRepository);
  }
}
