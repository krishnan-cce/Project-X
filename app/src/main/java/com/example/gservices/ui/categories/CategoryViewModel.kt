package com.example.gservices.ui.categories

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.gservices.data.auth.AuthRepository
import com.example.gservices.data.categories.CategoryRepository
import com.example.gservices.data.user.UserRepository
import com.example.gservices.models.categories.Categories
import com.example.gservices.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CategoryViewModel @Inject constructor(application: Application,
                                            private val repository : CategoryRepository

)
    : AndroidViewModel(application){





    val categories = liveData {
            emit(UiState.Loading)
            try {
                val result = repository.getAllCategories()
                emit(result)
            } catch (e: Exception) {
                emit(UiState.Error(e.message!!))
            }
    }



    fun addCategory(category: Categories) {
        repository.addCategory(category)
    }

    fun updateCategory(category: Categories, callback: (UiState<Nothing?>) -> Unit) {
        viewModelScope.launch {
            callback(repository.updateCategory(category))
        }
    }

    fun deleteCategory(categoryId: String): LiveData<UiState<Nothing?>> {
        return liveData {
            val result = repository.deleteCategory(categoryId)
            emit(result)
        }
    }

}