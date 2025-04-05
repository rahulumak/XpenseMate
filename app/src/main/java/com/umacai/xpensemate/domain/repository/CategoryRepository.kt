package com.umacai.xpensemate.domain.repository

import com.umacai.xpensemate.data.local.entity.CategoryEntity

interface CategoryRepository {

    suspend fun insertCategory(category: CategoryEntity): Long

    suspend fun getAllCategories(): List<CategoryEntity>

    suspend fun deleteCategory(categoryId: Int)

    suspend fun getCategoryIdFromSMSText(smsText: String): Int?
}