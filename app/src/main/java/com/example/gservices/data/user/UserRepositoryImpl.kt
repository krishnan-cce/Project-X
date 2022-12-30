package com.example.gservices.data.user

import com.example.gservices.models.user.User
import com.example.gservices.utils.AuthConstants.USERS
import com.example.gservices.utils.UiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class UserRepositoryImpl @Inject constructor(private val auth: FirebaseAuth,
                                             private val firestore : FirebaseFirestore
) : UserRepository {

    override suspend fun getCurrentUser(): UiState<User> {
        val currentUser = auth.currentUser ?: return UiState.Error("No current user")
        return suspendCoroutine { continuation ->
            firestore.collection(USERS).document(currentUser.uid).addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    continuation.resume(UiState.Error(exception.message!!))
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    continuation.resume(UiState.Success(snapshot.toObject(User::class.java)!!))
                } else {
                    continuation.resume(UiState.Error("User not found"))
                }
            }
        }
    }


    override suspend fun getUsers(): UiState<List<User>> {
        return try {
            val querySnapshot = firestore.collection(USERS).get().await()
            UiState.Success(querySnapshot.toObjects(User::class.java))
        } catch (e: Exception) {
            UiState.Error("Error getting users", e.localizedMessage)
        }
    }

    override suspend fun addUser(user: User): UiState<Unit> {
        return try {
            firestore.collection(USERS).document(user.userId!!).set(user).await()
            UiState.Success(Unit)
        } catch (e: Exception) {
            UiState.Error("Error adding user", e.localizedMessage)
        }
    }

    override suspend fun updateUser(user: User): UiState<Unit> {
        return try {
            firestore.collection(USERS).document(user.userId!!).set(user).await()
            UiState.Success(Unit)
        } catch (e: Exception) {
            UiState.Error("Error updating user", e.localizedMessage)
        }
    }

    override suspend fun deleteUser(id: String): UiState<Unit> {
        return try {
            firestore.collection(USERS).document(id).delete().await()
            UiState.Success(Unit)
        } catch (e: Exception) {
            UiState.Error("Error deleting user", e.localizedMessage)
        }
    }

}