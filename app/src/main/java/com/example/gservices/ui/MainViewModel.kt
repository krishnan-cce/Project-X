package com.example.gservices.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.gservices.data.auth.AuthRepository
import com.example.gservices.data.user.UserRepository
import com.example.gservices.models.user.User
import com.example.gservices.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(application: Application,
                                        private val repository: AuthRepository,
                                        private val userRepository : UserRepository

)
    : AndroidViewModel(application){



    private val _currentUser = MutableLiveData<UiState<User>>()
    val currentUser: LiveData<UiState<User>>
    get() = _currentUser
    fun getUser(){
        viewModelScope.launch {
            _currentUser.value = UiState.Loading
            _currentUser.value = userRepository.getCurrentUser()

        }
    }

}