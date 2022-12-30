package com.example.gservices.data.user

import com.example.gservices.models.user.User
import com.example.gservices.utils.UiState

interface UserRepository {
    suspend fun getUsers(): UiState<List<User>>
    suspend fun addUser(user: User): UiState<Unit>
    suspend fun updateUser(user: User): UiState<Unit>
    suspend fun deleteUser(id: String): UiState<Unit>
    suspend fun getCurrentUser(): UiState<User>
}