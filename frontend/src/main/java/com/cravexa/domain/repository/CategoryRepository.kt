package com.cravexa.domain.repository

import com.cravexa.core.common.Resource
import com.cravexa.domain.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getCategories(): Flow<Resource<List<Category>>>
    fun getCategoryById(categoryId: String): Flow<Resource<Category>>
}

