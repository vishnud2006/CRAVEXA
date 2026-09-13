package com.cravexa.presentation.seller.products;

import androidx.lifecycle.SavedStateHandle;
import com.cravexa.domain.usecase.AddSellerProductUseCase;
import com.cravexa.domain.usecase.GetCategoriesUseCase;
import com.cravexa.domain.usecase.GetSellerProductByIdUseCase;
import com.cravexa.domain.usecase.GetSellerProfileUseCase;
import com.cravexa.domain.usecase.UpdateSellerProductUseCase;
import com.cravexa.domain.usecase.UploadProductImageUseCase;
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
public final class AddEditProductViewModel_Factory implements Factory<AddEditProductViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<GetSellerProductByIdUseCase> getSellerProductByIdUseCaseProvider;

  private final Provider<AddSellerProductUseCase> addSellerProductUseCaseProvider;

  private final Provider<UpdateSellerProductUseCase> updateSellerProductUseCaseProvider;

  private final Provider<GetSellerProfileUseCase> getSellerProfileUseCaseProvider;

  private final Provider<GetCategoriesUseCase> getCategoriesUseCaseProvider;

  private final Provider<UploadProductImageUseCase> uploadProductImageUseCaseProvider;

  public AddEditProductViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<GetSellerProductByIdUseCase> getSellerProductByIdUseCaseProvider,
      Provider<AddSellerProductUseCase> addSellerProductUseCaseProvider,
      Provider<UpdateSellerProductUseCase> updateSellerProductUseCaseProvider,
      Provider<GetSellerProfileUseCase> getSellerProfileUseCaseProvider,
      Provider<GetCategoriesUseCase> getCategoriesUseCaseProvider,
      Provider<UploadProductImageUseCase> uploadProductImageUseCaseProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.getSellerProductByIdUseCaseProvider = getSellerProductByIdUseCaseProvider;
    this.addSellerProductUseCaseProvider = addSellerProductUseCaseProvider;
    this.updateSellerProductUseCaseProvider = updateSellerProductUseCaseProvider;
    this.getSellerProfileUseCaseProvider = getSellerProfileUseCaseProvider;
    this.getCategoriesUseCaseProvider = getCategoriesUseCaseProvider;
    this.uploadProductImageUseCaseProvider = uploadProductImageUseCaseProvider;
  }

  @Override
  public AddEditProductViewModel get() {
    return newInstance(savedStateHandleProvider.get(), getSellerProductByIdUseCaseProvider.get(), addSellerProductUseCaseProvider.get(), updateSellerProductUseCaseProvider.get(), getSellerProfileUseCaseProvider.get(), getCategoriesUseCaseProvider.get(), uploadProductImageUseCaseProvider.get());
  }

  public static AddEditProductViewModel_Factory create(
      Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<GetSellerProductByIdUseCase> getSellerProductByIdUseCaseProvider,
      Provider<AddSellerProductUseCase> addSellerProductUseCaseProvider,
      Provider<UpdateSellerProductUseCase> updateSellerProductUseCaseProvider,
      Provider<GetSellerProfileUseCase> getSellerProfileUseCaseProvider,
      Provider<GetCategoriesUseCase> getCategoriesUseCaseProvider,
      Provider<UploadProductImageUseCase> uploadProductImageUseCaseProvider) {
    return new AddEditProductViewModel_Factory(savedStateHandleProvider, getSellerProductByIdUseCaseProvider, addSellerProductUseCaseProvider, updateSellerProductUseCaseProvider, getSellerProfileUseCaseProvider, getCategoriesUseCaseProvider, uploadProductImageUseCaseProvider);
  }

  public static AddEditProductViewModel newInstance(SavedStateHandle savedStateHandle,
      GetSellerProductByIdUseCase getSellerProductByIdUseCase,
      AddSellerProductUseCase addSellerProductUseCase,
      UpdateSellerProductUseCase updateSellerProductUseCase,
      GetSellerProfileUseCase getSellerProfileUseCase, GetCategoriesUseCase getCategoriesUseCase,
      UploadProductImageUseCase uploadProductImageUseCase) {
    return new AddEditProductViewModel(savedStateHandle, getSellerProductByIdUseCase, addSellerProductUseCase, updateSellerProductUseCase, getSellerProfileUseCase, getCategoriesUseCase, uploadProductImageUseCase);
  }
}
