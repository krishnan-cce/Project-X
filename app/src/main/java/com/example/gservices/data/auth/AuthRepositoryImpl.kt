package com.example.gservices.data.auth

import com.example.gservices.models.user.User
import com.example.gservices.utils.AuthConstants.USERS
import com.example.gservices.utils.Network
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val auth: FirebaseAuth,
                                             private val firestore : FirebaseFirestore)
    : AuthRepository {

    private val currentUser = auth.currentUser

    override suspend fun signUp(user: User, password: String): Network<Void> {
        val usersRef = firestore.collection(USERS)
        val query = usersRef.whereEqualTo("username", user.username)
        val snapshot = query.get().await()

        if (snapshot.isEmpty) {
            // username is available, proceed with creating the user
            return try {
                auth.createUserWithEmailAndPassword(user.email!!, password).await()
                if (currentUser != null) {
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(user.username)
                        .build()
                    currentUser.updateProfile(profileUpdates).await()
                    firestore.collection(USERS).document(currentUser.uid).set(user).await()
                }
                Network.Success("User Registered Successfully")
            } catch (e: Exception) {
                Network.Error(e.message!!, "An Error Occured")
            }
        } else {
            // username is already in use, return error
            return Network.Error("Username already in use", "Error")
        }
    }

    override suspend fun signIn(email: String, password: String): Network<Void> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Network.Success("Singin Succesful")
        } catch (e: Exception) {
            Network.Error(e.message!!, "An Error Occured !")
        }
    }

    override suspend fun signOut(): Network<Void> {
        return try {

            auth.signOut()
            Network.Success("Logout Successful")
        } catch (e: Exception) {
            Network.Error(e.message!!, "An Error Occured")
        }
    }

    override suspend fun isSignedIn(): Boolean {
        return auth.currentUser != null
    }


}