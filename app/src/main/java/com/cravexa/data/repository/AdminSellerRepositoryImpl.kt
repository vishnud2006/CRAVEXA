package com.cravexa.data.repository

import com.cravexa.core.common.Resource
import com.cravexa.domain.model.AdminSeller
import com.cravexa.domain.model.FssaiStatus
import com.cravexa.domain.model.SellerAccountStatus
import com.cravexa.domain.repository.AdminSellerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdminSellerRepositoryImpl @Inject constructor() : AdminSellerRepository {

    private val initialSellers = mutableListOf(
        AdminSeller(
            id = "sel_1",
            sellerName = "Lakshmi Devi",
            businessName = "Lakshmi's Home Kitchen",
            email = "seller@cravexa.com",
            phone = "+91 9845123456",
            status = SellerAccountStatus.PENDING,
            fssaiStatus = FssaiStatus.SUBMITTED,
            fssaiNumber = "10020042000891",
            fssaiDocumentUrl = "https://cravexa.app/docs/fssai_lakshmi.pdf",
            location = "Indiranagar, Bengaluru, Karnataka",
            categoryName = "Pickles & Chutneys",
            bio = "Authentic home-style sun-cured pickles and podis following a 40-year heirloom recipe.",
            createdAt = "01 Aug 2026",
            totalProducts = 4,
            totalOrders = 24,
            rating = 4.9
        ),
        AdminSeller(
            id = "sel_2",
            sellerName = "Sujata Joshi",
            businessName = "Malwa Heritage Sweets",
            email = "sujata.sweets@example.com",
            phone = "+91 9123456780",
            status = SellerAccountStatus.APPROVED,
            fssaiStatus = FssaiStatus.VERIFIED,
            fssaiNumber = "10019012000543",
            fssaiDocumentUrl = "https://cravexa.app/docs/fssai_sujata.pdf",
            location = "Indore, Madhya Pradesh",
            categoryName = "Sweets & Mithai",
            bio = "Pure desi ghee laddoos and festival hampers handcrafted in small batches.",
            createdAt = "10 Aug 2026",
            totalProducts = 6,
            totalOrders = 15,
            rating = 4.8
        ),
        AdminSeller(
            id = "sel_3",
            sellerName = "Geeta Patel",
            businessName = "Ahmedabad Crisps & Khakhra",
            email = "geeta.gujaratikhakhra@example.com",
            phone = "+91 9234567891",
            status = SellerAccountStatus.PENDING,
            fssaiStatus = FssaiStatus.PENDING,
            fssaiNumber = "20023001000782",
            fssaiDocumentUrl = "https://cravexa.app/docs/fssai_geeta.pdf",
            location = "Maninagar, Ahmedabad, Gujarat",
            categoryName = "Snacks & Savouries",
            bio = "Traditional handmade vacuum-packed whole wheat khakhras.",
            createdAt = "25 Aug 2026",
            totalProducts = 2,
            totalOrders = 0,
            rating = 5.0
        ),
        AdminSeller(
            id = "sel_4",
            sellerName = "Amina Begum",
            businessName = "Old Delhi Shahi Masalas",
            email = "amina.masalas@example.com",
            phone = "+91 9345678912",
            status = SellerAccountStatus.REJECTED,
            fssaiStatus = FssaiStatus.REJECTED,
            fssaiNumber = "INVALID-12345",
            fssaiDocumentUrl = null,
            location = "Chandni Chowk, Delhi",
            categoryName = "Spices & Masalas",
            bio = "Hand-ground whole spice blends from Old Delhi.",
            createdAt = "15 Jul 2026",
            totalProducts = 0,
            totalOrders = 0,
            rating = 0.0
        )
    )

    private val _sellersFlow = MutableStateFlow<List<AdminSeller>>(initialSellers)
    override val sellersFlow: Flow<List<AdminSeller>> = _sellersFlow.asStateFlow()

    override fun getSellers(): Flow<Resource<List<AdminSeller>>> = flow {
        emit(Resource.Loading())
        emit(Resource.Success(_sellersFlow.value.toList()))
    }

    override suspend fun updateSellerStatus(sellerId: String, newStatus: SellerAccountStatus): Resource<Unit> {
        return try {
            val list = _sellersFlow.value.toMutableList()
            val index = list.indexOfFirst { it.id == sellerId }
            if (index != -1) {
                list[index] = list[index].copy(status = newStatus)
                _sellersFlow.value = list
                Resource.Success(Unit)
            } else {
                Resource.Error("Seller not found.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to update seller status.")
        }
    }

    override suspend fun updateFssaiStatus(sellerId: String, newFssaiStatus: FssaiStatus): Resource<Unit> {
        return try {
            val list = _sellersFlow.value.toMutableList()
            val index = list.indexOfFirst { it.id == sellerId }
            if (index != -1) {
                list[index] = list[index].copy(fssaiStatus = newFssaiStatus)
                _sellersFlow.value = list
                Resource.Success(Unit)
            } else {
                Resource.Error("Seller not found.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to update FSSAI status.")
        }
    }
}
