package com.example.gservices.ui.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.gservices.ui.MainActivity
import com.example.gservices.R
import com.example.gservices.databinding.ActivityRegisterBinding
import com.example.gservices.utils.observeNetwork
import com.example.gservices.utils.openActivity
import com.example.gservices.utils.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity: AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: AuthenticationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()

        observeMessage()
        clickEvents()

    }

    private fun setupBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)
        viewModel = ViewModelProvider(this)[AuthenticationViewModel::class.java]
        binding.authVM = viewModel
        binding.lifecycleOwner = this
    }

    fun clickEvents() {
        viewModel.getOnClick().observe(this) {
            when (it) {
                1 -> {
                    openActivity<LoginActivity>(){}
                }
                2-> {

                }
                3 -> {
                    openActivity<LoginActivity>(){}
                }

            }
        }
    }
    fun observeMessage(){


        viewModel.signUpResult.observeNetwork(this,
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