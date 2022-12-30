package com.example.gservices.ui.auth

import android.app.Application
import android.util.Log
import androidx.core.text.HtmlCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.gservices.data.auth.AuthRepository
import com.example.gservices.data.user.UserRepository
import com.example.gservices.models.user.User
import com.example.gservices.utils.Network
import com.example.gservices.utils.isValidEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(application: Application,
                                                  private val repository: AuthRepository,
                                                  private val userRepository : UserRepository
)
    : AndroidViewModel(application){



    val name = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val userName = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    val loginEmail = MutableLiveData<String>()
    val loginPassword = MutableLiveData<String>()

    val signInText = HtmlCompat.fromHtml("Already have an account ? <u>Sign In</u>.", HtmlCompat.FROM_HTML_MODE_LEGACY)
    val signUpText = HtmlCompat.fromHtml("Don't have account ? <u>Sign Up</u>.", HtmlCompat.FROM_HTML_MODE_LEGACY)

    private var onClick = MutableLiveData<Int>()
    fun getOnClick(): LiveData<Int> = onClick

    private var _errorMsg = MutableLiveData<Network<String>>()
    val errorMsg: LiveData<Network<String>>
        get() = _errorMsg



    fun onCLickListener(option : Int){
        onClick.value = option
    }


     fun registerClicked() {
        val name = name.value?.trim() ?: ""
        val userName = userName.value?.trim() ?: ""
        val email = email.value?.trim() ?: ""
        val password = password.value?.trim() ?: ""

        when {
            name.isEmpty() -> {
                _errorMsg.postValue(Network.Error("Enter Name"))
            }

            userName.length < 2 -> {
                _errorMsg.postValue(Network.Error("Enter Username"))
            }

            email.isEmpty() -> {
                _errorMsg.postValue(Network.Error("Enter Email"))
            }
            !email.isValidEmail() -> {
                _errorMsg.postValue(Network.Error("Enter valid Email"))
            }

            password.isEmpty() -> {
                _errorMsg.postValue(Network.Error("Enter password"))
            }
            password.length < 6 -> {
                _errorMsg.postValue(Network.Error("Enter valid password"))
            }

            else -> {
                val userData = User(
                    userId = "",
                    email = email,
                    name = name,
                    username = userName,
                    password = password,
                    bio = "Hey There !",
                )
                viewModelScope.launch{
                    signUp(userData,password)
                }
            }
        }
    }
    private val _signUpResult = MutableLiveData<Network<Void>>()
    val signUpResult: LiveData<Network<Void>>
        get() = _signUpResult
    suspend fun signUp(user: User, password: String) {
        viewModelScope.launch {
            _signUpResult.postValue(Network.Loading())
            val result = repository.signUp(user, password)
            _signUpResult.value = result

        }

    }


    fun loginClicked(){
        val loginEmail = loginEmail.value?.trim() ?: ""
        val loginPassword = loginPassword.value?.trim() ?: ""
        when {
            loginEmail.isEmpty() -> {
                _errorMsg.postValue(Network.Error("Enter Email"))
            }

            loginPassword.length < 2 -> {
                _errorMsg.postValue(Network.Error("Enter Password"))
            }
            else -> {
                viewModelScope.launch {
                    logIn(loginEmail,loginPassword)
                }
            }
        }
    }
    private val _loginState = MutableLiveData<Network<Void>>()
    val loginState: LiveData<Network<Void>>
        get() = _loginState
    suspend fun logIn(email: String, password: String){
        viewModelScope.launch {
            _loginState.postValue(Network.Loading())
            val result = repository.signIn(email,password)
            _loginState.value = result
        }
    }



    fun onLogOut(){
        viewModelScope.launch {
            logOut()
        }
    }
    private val _logoutState = MutableLiveData<Network<Void>>()
    val logoutState: LiveData<Network<Void>>
        get() = _logoutState
    suspend fun logOut(){
        viewModelScope.launch {
            _logoutState.postValue(Network.Loading())
            val result = repository.signOut()
            _logoutState.value = result
        }
    }

    private val _isSignedIn = MutableLiveData<Boolean>()
    val isSignedIn: LiveData<Boolean>
        get() = _isSignedIn
    fun checkIsSignedIn() {
        viewModelScope.launch {
            _isSignedIn.value = repository.isSignedIn()
            Log.d("UserViewModel", "isSignedIn = ${_isSignedIn.value}")
        }
    }




}