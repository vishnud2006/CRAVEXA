package com.cravexa.domain.repository

import com.cravexa.core.common.Resource
import com.cravexa.domain.model.AdminComplaint
import com.cravexa.domain.model.ComplaintStatus
import kotlinx.coroutines.flow.Flow

interface AdminComplaintRepository {
    val complaintsFlow: Flow<List<AdminComplaint>>
    fun getComplaints(): Flow<Resource<List<AdminComplaint>>>
    suspend fun updateComplaintStatus(ticketId: String, newStatus: ComplaintStatus, internalNotes: String): Resource<Unit>
}
