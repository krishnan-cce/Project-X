package com.example.gservices.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.gservices.ui.MainActivity
import com.example.gservices.R
import com.example.gservices.databinding.ActivityLoginBinding
import com.example.gservices.utils.observeNetwork
import com.example.gservices.utils.openActivity
import com.example.gservices.utils.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: AuthenticationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        clickEvents()
        observeMessage()
    }


    private fun setupBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        viewModel = ViewModelProvider(this)[AuthenticationViewModel::class.java]
        binding.authVM = viewModel
        binding.lifecycleOwner = this
    }

    fun clickEvents() {
        viewModel.getOnClick().observe(this) {
            when (it) {
                1 -> {
                    openActivity<RegisterActivity>(){}
                }
                2-> {

                }
                3 -> {
                    openActivity<RegisterActivity>(){}
                }

            }
        }
    }

    fun observeMessage(){

        viewModel.loginState.observeNetwork(this,
            onSuccess = { response ->
                openActivity<MainActivity>(){}
                toast(response)
            }, onError = { response ->
                toast(response)
            }, onLoading = {
                // Show the progress bar
            })

        viewModel.errorMsg.observeNetwork(this,
            onSuccess = { response ->
                toast(response)
            }, onError = { response ->
                toast(response)
            }, onLoading = {
                // Show the progress bar
            })

    }
}