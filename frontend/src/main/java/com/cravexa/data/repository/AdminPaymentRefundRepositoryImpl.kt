package com.cravexa.data.repository

import com.cravexa.core.common.Resource
import com.cravexa.domain.model.AdminPayment
import com.cravexa.domain.model.AdminRefund
import com.cravexa.domain.model.AdminRefundStatus
import com.cravexa.domain.model.PaymentStatus
import com.cravexa.domain.repository.AdminPaymentRefundRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdminPaymentRefundRepositoryImpl @Inject constructor() : AdminPaymentRefundRepository {

    private val initialPayments = mutableListOf(
        AdminPayment(
            id = "pay_101",
            orderNumber = "CRV-89421",
            customerName = "Ananya Sharma",
            amount = 799.0,
            status = PaymentStatus.PAID,
            paymentMethod = "UPI (Google Pay)",
            referenceId = "UPI-REF-90238491823",
            date = "31 Aug 2026, 09:15 AM"
        ),
        AdminPayment(
            id = "pay_102",
            orderNumber = "CRV-89350",
            customerName = "Rahul Verma",
            amount = 329.0,
            status = PaymentStatus.PAID,
            paymentMethod = "Credit Card (HDFC)",
            referenceId = "CC-AUTH-884029104",
            date = "31 Aug 2026, 08:30 AM"
        ),
        AdminPayment(
            id = "pay_103",
            orderNumber = "CRV-89104",
            customerName = "Sneha Reddy",
            amount = 849.0,
            status = PaymentStatus.PAID,
            paymentMethod = "UPI (PhonePe)",
            referenceId = "UPI-REF-7749102485",
            date = "30 Aug 2026, 06:45 PM"
        )
    )

    private val initialRefunds = mutableListOf(
        AdminRefund(
            id = "ref_101",
            orderNumber = "CRV-87901",
            customerName = "Vikram Aditya",
            sellerName = "Lakshmi's Home Kitchen",
            amount = 280.0,
            reason = "Courier delayed delivery; glass seal broken in transit.",
            status = AdminRefundStatus.REQUESTED,
            date = "30 Aug 2026, 02:15 PM"
        ),
        AdminRefund(
            id = "ref_102",
            orderNumber = "CRV-86450",
            customerName = "Meera Nair",
            sellerName = "Malwa Heritage Sweets",
            amount = 450.0,
            reason = "Item out of stock after order confirmation.",
            status = AdminRefundStatus.COMPLETED,
            date = "24 Aug 2026, 10:00 AM"
        )
    )

    private val _paymentsFlow = MutableStateFlow<List<AdminPayment>>(initialPayments)
    private val _refundsFlow = MutableStateFlow<List<AdminRefund>>(initialRefunds)

    override val paymentsFlow: Flow<List<AdminPayment>> = _paymentsFlow.asStateFlow()
    override val refundsFlow: Flow<List<AdminRefund>> = _refundsFlow.asStateFlow()

    override fun getPayments(): Flow<Resource<List<AdminPayment>>> = flow {
        emit(Resource.Loading())
        emit(Resource.Success(_paymentsFlow.value.toList()))
    }

    override fun getRefunds(): Flow<Resource<List<AdminRefund>>> = flow {
        emit(Resource.Loading())
        emit(Resource.Success(_refundsFlow.value.toList()))
    }

    override suspend fun updateRefundStatus(refundId: String, newStatus: AdminRefundStatus): Resource<Unit> {
        return try {
            val list = _refundsFlow.value.toMutableList()
            val index = list.indexOfFirst { it.id == refundId }
            if (index != -1) {
                list[index] = list[index].copy(status = newStatus)
                _refundsFlow.value = list
                Resource.Success(Unit)
            } else {
                Resource.Error("Refund request not found.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to update refund status.")
        }
    }
}
