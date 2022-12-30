package com.example.gservices.data.categories

import com.example.gservices.models.categories.Categories
import com.example.gservices.utils.UiState

interface CategoryRepository {
    fun getCategoryById(id: String): Categories
    suspend fun getAllCategories(): UiState<List<Categories>>
    fun addCategory(category: Categories)
    suspend fun updateCategory(category: Categories): UiState<Nothing?>
    suspend fun deleteCategory(categoryId: String): UiState<Nothing?>
}