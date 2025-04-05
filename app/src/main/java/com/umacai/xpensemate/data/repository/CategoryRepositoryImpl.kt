package com.umacai.xpensemate.data.repository

import com.umacai.xpensemate.data.local.dao.CategoryDao
import com.umacai.xpensemate.data.local.entity.CategoryEntity
import com.umacai.xpensemate.domain.repository.CategoryRepository
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao
) : CategoryRepository {

    override suspend fun insertCategory(category: CategoryEntity): Long {
        return categoryDao.insertCategory(category)
    }

    override suspend fun getAllCategories(): List<CategoryEntity> {
        return categoryDao.getAllCategories()
    }

    override suspend fun deleteCategory(categoryId: Int) {
        categoryDao.deleteCategory(categoryId)
    }

    override suspend fun getCategoryIdFromSMSText(smsText: String): Int? {
        val category = categoryDao.getCategoryIdFromSMSText(smsText)
        return category?.id

    }
}