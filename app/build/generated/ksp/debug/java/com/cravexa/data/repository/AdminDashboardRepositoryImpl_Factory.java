package com.cravexa.data.repository;

import com.cravexa.domain.repository.AdminComplaintRepository;
import com.cravexa.domain.repository.AdminOrderManagementRepository;
import com.cravexa.domain.repository.AdminPaymentRefundRepository;
import com.cravexa.domain.repository.AdminProductModerationRepository;
import com.cravexa.domain.repository.AdminSellerRepository;
import com.cravexa.domain.repository.AdminUserRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class AdminDashboardRepositoryImpl_Factory implements Factory<AdminDashboardRepositoryImpl> {
  private final Provider<AdminUserRepository> userRepositoryProvider;

  private final Provider<AdminSellerRepository> sellerRepositoryProvider;

  private final Provider<AdminProductModerationRepository> productRepositoryProvider;

  private final Provider<AdminOrderManagementRepository> orderRepositoryProvider;

  private final Provider<AdminPaymentRefundRepository> paymentRefundRepositoryProvider;

  private final Provider<AdminComplaintRepository> complaintRepositoryProvider;

  public AdminDashboardRepositoryImpl_Factory(Provider<AdminUserRepository> userRepositoryProvider,
      Provider<AdminSellerRepository> sellerRepositoryProvider,
      Provider<AdminProductModerationRepository> productRepositoryProvider,
      Provider<AdminOrderManagementRepository> orderRepositoryProvider,
      Provider<AdminPaymentRefundRepository> paymentRefundRepositoryProvider,
      Provider<AdminComplaintRepository> complaintRepositoryProvider) {
    this.userRepositoryProvider = userRepositoryProvider;
    this.sellerRepositoryProvider = sellerRepositoryProvider;
    this.productRepositoryProvider = productRepositoryProvider;
    this.orderRepositoryProvider = orderRepositoryProvider;
    this.paymentRefundRepositoryProvider = paymentRefundRepositoryProvider;
    this.complaintRepositoryProvider = complaintRepositoryProvider;
  }

  @Override
  public AdminDashboardRepositoryImpl get() {
    return newInstance(userRepositoryProvider.get(), sellerRepositoryProvider.get(), productRepositoryProvider.get(), orderRepositoryProvider.get(), paymentRefundRepositoryProvider.get(), complaintRepositoryProvider.get());
  }

  public static AdminDashboardRepositoryImpl_Factory create(
      Provider<AdminUserRepository> userRepositoryProvider,
      Provider<AdminSellerRepository> sellerRepositoryProvider,
      Provider<AdminProductModerationRepository> productRepositoryProvider,
      Provider<AdminOrderManagementRepository> orderRepositoryProvider,
      Provider<AdminPaymentRefundRepository> paymentRefundRepositoryProvider,
      Provider<AdminComplaintRepository> complaintRepositoryProvider) {
    return new AdminDashboardRepositoryImpl_Factory(userRepositoryProvider, sellerRepositoryProvider, productRepositoryProvider, orderRepositoryProvider, paymentRefundRepositoryProvider, complaintRepositoryProvider);
  }

  public static AdminDashboardRepositoryImpl newInstance(AdminUserRepository userRepository,
      AdminSellerRepository sellerRepository, AdminProductModerationRepository productRepository,
      AdminOrderManagementRepository orderRepository,
      AdminPaymentRefundRepository paymentRefundRepository,
      AdminComplaintRepository complaintRepository) {
    return new AdminDashboardRepositoryImpl(userRepository, sellerRepository, productRepository, orderRepository, paymentRefundRepository, complaintRepository);
  }
}
