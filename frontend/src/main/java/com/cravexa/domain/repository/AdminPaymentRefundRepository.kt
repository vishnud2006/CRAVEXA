package com.cravexa.domain.repository

import com.cravexa.core.common.Resource
import com.cravexa.domain.model.AdminPayment
import com.cravexa.domain.model.AdminRefund
import com.cravexa.domain.model.AdminRefundStatus
import kotlinx.coroutines.flow.Flow

interface AdminPaymentRefundRepository {
    val paymentsFlow: Flow<List<AdminPayment>>
    val refundsFlow: Flow<List<AdminRefund>>
    fun getPayments(): Flow<Resource<List<AdminPayment>>>
    fun getRefunds(): Flow<Resource<List<AdminRefund>>>
    suspend fun updateRefundStatus(refundId: String, newStatus: AdminRefundStatus): Resource<Unit>
}
