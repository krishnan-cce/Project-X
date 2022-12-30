package com.example.gservices.data.auth

import com.example.gservices.models.user.User
import com.example.gservices.utils.Network


interface AuthRepository {
    suspend fun signUp(user: User, password: String): Network<Void>
    suspend fun signIn(email: String, password: String): Network<Void>
    suspend fun signOut(): Network<Void>
    suspend fun isSignedIn(): Boolean
}