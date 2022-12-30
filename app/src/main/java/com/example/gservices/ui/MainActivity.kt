package com.example.gservices.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.gservices.R
import com.example.gservices.databinding.ActivityMainBinding
import com.example.gservices.models.categories.Categories
import com.example.gservices.models.user.User
import com.example.gservices.ui.auth.AuthenticationViewModel
import com.example.gservices.ui.auth.LoginActivity
import com.example.gservices.ui.categories.CategoryViewModel
import com.example.gservices.utils.*
import com.example.gservices.utils.AuthConstants.CURRENT_USER_TOKEN
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var sessionManager: SessionManager

    private lateinit var binding: ActivityMainBinding
    private lateinit var authViewModel: AuthenticationViewModel
    private lateinit var mainViewModel: MainViewModel
    private lateinit var catViewModel: CategoryViewModel

    private var selectedCategory: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBinding()
        observeMessage()

        saveCurrentUser()

        val user = sessionManager.getData(this, CURRENT_USER_TOKEN, User::class.java)

        if (user != null) {
            binding.textView5.text = "ID " + user.name.toString()
        }

        catViewModel.categories.observe(this, Observer { result ->
            when (result) {
                is UiState.Loading -> {
                    // Show a loading indicator
                }
                is UiState.Success -> {
                    val categoryNames = result.data.map { it.categoryName }
                    val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categoryNames)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spinnerCategories.adapter = adapter
                    binding.spinnerCategories.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            val selectedCategory = parent?.getItemAtPosition(position) as String
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                    }
                }
                is UiState.Error -> {
                    // Show an error message
                    toast("Error loading categories: ${result.message}")
                }
            }

        })







    }




    override fun onResume() {
        super.onResume()

        authViewModel.isSignedIn.observe(this, Observer { isSignedIn ->
            Log.d("MainActivity", "isSignedIn = $isSignedIn")
            if (isSignedIn) {
                // The user is signed in, show the main activity
            } else {
                openActivity<LoginActivity>(){}

            }
        })
        authViewModel.checkIsSignedIn()
    }

    private fun setupBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        authViewModel = ViewModelProvider(this)[AuthenticationViewModel::class.java]
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        catViewModel = ViewModelProvider(this)[CategoryViewModel::class.java]
        binding.authVM = authViewModel
        binding.mainVM = mainViewModel
        binding.lifecycleOwner = this
    }

    fun observeMessage(){

        authViewModel.logoutState.observeNetwork(this,
            onSuccess = { response ->
                sessionManager.clearAllSession(this)
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
                toast(response)
            }, onError = { response ->
                toast(response)
            }, onLoading = {
                // Show the progress bar
            })

    }

    fun saveCurrentUser(){
        mainViewModel.currentUser.observe(this, Observer { user ->
            when (user) {
                is UiState.Success -> {
                    val user = User(
                        username = user.data.username,
                        email = user.data.email,
                        userId = user.data.userId,
                        name = user.data.name,
                        imageUrl = user.data.imageUrl
                    )

                    sessionManager.saveData(
                        context = this,
                        data = user,
                        key = CURRENT_USER_TOKEN
                    )

                }
                is UiState.Error -> {
                    // Handle the error
                }
                is UiState.Loading -> {
                    // Show a loading indicator
                }
            }
        })
        mainViewModel.getUser()
    }



}