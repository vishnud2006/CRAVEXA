package com.cravexa.domain.repository

import android.net.Uri
import com.cravexa.core.common.Resource

interface ImageUploadRepository {
    suspend fun uploadProductImage(uri: Uri): Resource<String>
    suspend fun uploadProductImages(uris: List<Uri>): Resource<List<String>>
}
