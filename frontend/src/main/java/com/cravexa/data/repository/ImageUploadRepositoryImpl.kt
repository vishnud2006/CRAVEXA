package com.cravexa.data.repository

import android.net.Uri
import com.cravexa.core.common.Resource
import com.cravexa.domain.repository.ImageUploadRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageUploadRepositoryImpl @Inject constructor() : ImageUploadRepository {

    override suspend fun uploadProductImage(imageUri: Uri): Resource<String> {
        return try {
            // Local URI representation - Cloudinary endpoint abstraction will connect here
            Resource.Success(imageUri.toString())
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to process image URI.")
        }
    }

    override suspend fun uploadProductImages(imageUris: List<Uri>): Resource<List<String>> {
        return try {
            Resource.Success(imageUris.map { it.toString() })
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to process images.")
        }
    }
}

