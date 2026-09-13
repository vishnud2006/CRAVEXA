package com.cravexa.data.repository

import com.cravexa.core.common.Resource
import com.cravexa.domain.model.AdminComplaint
import com.cravexa.domain.model.ComplaintCategory
import com.cravexa.domain.model.ComplaintStatus
import com.cravexa.domain.repository.AdminComplaintRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdminComplaintRepositoryImpl @Inject constructor() : AdminComplaintRepository {

    private val initialComplaints = mutableListOf(
        AdminComplaint(
            id = "cmp_101",
            ticketNumber = "TKT-8901",
            customerName = "Vikram Aditya",
            sellerName = "Lakshmi's Home Kitchen",
            category = ComplaintCategory.PACKAGING_ISSUE,
            description = "The glass jar seal was damaged during delivery transit.",
            status = ComplaintStatus.OPEN,
            internalNotes = "Contacted delivery partner. Awaiting parcel photos from customer.",
            date = "30 Aug 2026, 03:00 PM"
        ),
        AdminComplaint(
            id = "cmp_102",
            ticketNumber = "TKT-8842",
            customerName = "Rahul Verma",
            sellerName = "Malwa Heritage Sweets",
            category = ComplaintCategory.ORDER_ISSUE,
            description = "Order was scheduled for 28th August evening, received next day morning.",
            status = ComplaintStatus.IN_REVIEW,
            internalNotes = "Seller dispatched on time; courier delayed at hub.",
            date = "29 Aug 2026, 11:30 AM"
        ),
        AdminComplaint(
            id = "cmp_103",
            ticketNumber = "TKT-8710",
            customerName = "Sneha Reddy",
            sellerName = "Lakshmi's Home Kitchen",
            category = ComplaintCategory.PRODUCT_QUALITY,
            description = "Inquiry regarding ingredients allergen certification.",
            status = ComplaintStatus.RESOLVED,
            internalNotes = "Verified sesame oil cold-pressing cert with seller. Customer informed.",
            date = "26 Aug 2026, 04:15 PM"
        )
    )

    private val _complaintsFlow = MutableStateFlow<List<AdminComplaint>>(initialComplaints)
    override val complaintsFlow: Flow<List<AdminComplaint>> = _complaintsFlow.asStateFlow()

    override fun getComplaints(): Flow<Resource<List<AdminComplaint>>> = flow {
        emit(Resource.Loading())
        emit(Resource.Success(_complaintsFlow.value.toList()))
    }

    override suspend fun updateComplaintStatus(
        ticketId: String,
        newStatus: ComplaintStatus,
        internalNotes: String
    ): Resource<Unit> {
        return try {
            val list = _complaintsFlow.value.toMutableList()
            val index = list.indexOfFirst { it.id == ticketId }
            if (index != -1) {
                list[index] = list[index].copy(
                    status = newStatus,
                    internalNotes = internalNotes.ifBlank { list[index].internalNotes }
                )
                _complaintsFlow.value = list
                Resource.Success(Unit)
            } else {
                Resource.Error("Complaint ticket not found.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to update complaint ticket.")
        }
    }
}
