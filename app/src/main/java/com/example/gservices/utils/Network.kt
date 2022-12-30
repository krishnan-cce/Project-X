package com.example.gservices.utils

sealed class Network<T>(
    val data: String? = null,
    val message: String? = null
) {

    class Success<T>(data: String) : Network<T>(data)
    class Error<T>(message: String, data: String? = null) : Network<T>(data, message)
    class Loading<T> : Network<T>()
}
