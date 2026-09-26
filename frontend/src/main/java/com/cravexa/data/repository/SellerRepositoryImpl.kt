package com.cravexa.data.repository

import com.cravexa.core.common.Resource
import com.cravexa.data.local.PreferenceManager
import com.cravexa.domain.model.FssaiStatus
import com.cravexa.domain.model.SellerAccountStatus
import com.cravexa.domain.model.SellerProfile
import com.cravexa.domain.repository.SellerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SellerRepositoryImpl @Inject constructor(
    private val preferenceManager: PreferenceManager
) : SellerRepository {

    override val currentSellerProfile: Flow<SellerProfile?> = preferenceManager.sellerProfile

    override suspend fun getSellerProfile(): Resource<SellerProfile> {
        val stored = preferenceManager.sellerProfile.firstOrNull()
        if (stored != null) return Resource.Success(stored)

        val user = preferenceManager.userProfile.firstOrNull()
        return if (user != null) {
            val initial = SellerProfile(
                id = user.id,
                userId = user.id,
                sellerName = user.name,
                businessName = user.sellerBusinessName ?: "${user.name}'s Home Kitchen",
                email = user.email,
                phone = user.phone,
                profileImage = user.profileImage,
                about = "Passionate home chef handcrafting authentic regional delicacies with traditional family recipes and pure ingredients.",
                foodCategories = listOfNotNull(user.sellerFoodCategory ?: "Handmade Pickles & Chutneys"),
                businessAddress = user.sellerAddress ?: "Indiranagar",
                city = "Bengaluru",
                state = "Karnataka",
                pincode = "560038",
                sellerSince = "August 2026",
                accountStatus = SellerAccountStatus.PENDING,
                fssaiStatus = FssaiStatus.NOT_PROVIDED,
                isFssaiSubmitted = false,
                rating = 0.0,
                totalReviews = 0,
                totalDishes = 0
            )
            preferenceManager.saveSellerProfile(initial)
            Resource.Success(initial)
        } else {
            Resource.Error("Seller profile not found.")
        }
    }

    override suspend fun updateSellerProfile(profile: SellerProfile): Resource<SellerProfile> {
        return try {
            val updated = profile.copy(updatedAt = System.currentTimeMillis())
            preferenceManager.saveSellerProfile(updated)
            Resource.Success(updated)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to update seller profile.")
        }
    }

    override suspend fun submitFssai(fssaiNumber: String, documentUrl: String?): Resource<SellerProfile> {
        return try {
            val current = preferenceManager.sellerProfile.firstOrNull() ?: SellerProfile()
            val updated = current.copy(
                fssaiNumber = fssaiNumber.trim(),
                fssaiStatus = FssaiStatus.PENDING, // Verification pending by CRAVEXA operations
                fssaiDocumentUrl = documentUrl,
                isFssaiSubmitted = true,
                updatedAt = System.currentTimeMillis()
            )
            preferenceManager.saveSellerProfile(updated)
            Resource.Success(updated)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to submit FSSAI details.")
        }
    }
}

