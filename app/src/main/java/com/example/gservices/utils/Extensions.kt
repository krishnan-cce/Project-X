package com.example.gservices.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun String.isValidEmail() =
    isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun Context.toast(message: String) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

inline fun <reified T : Activity> Context.openActivity(block: Intent.() -> Unit = {}) {
    startActivity(Intent(this, T::class.java).apply(block))
}

fun <T> LiveData<Network<T>>.observeNetwork(owner: LifecycleOwner, onSuccess: (String) -> Unit, onError: (String) -> Unit, onLoading: () -> Unit) {
    this.observe(owner, Observer {
        when (it) {
            is Network.Success -> it.data?.let { it1 -> onSuccess(it1) }
            is Network.Error -> it.message?.let { it1 -> onError(it1) }
            is Network.Loading -> onLoading()
            else -> {
                // Do nothing for loading state
            }
        }
    })
}