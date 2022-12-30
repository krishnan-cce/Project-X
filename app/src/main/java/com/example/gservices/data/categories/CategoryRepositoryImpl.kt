package com.example.gservices.data.categories

import android.util.Log
import com.example.gservices.data.auth.AuthRepository
import com.example.gservices.models.categories.Categories
import com.example.gservices.utils.UiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class CategoryRepositoryImpl @Inject constructor(private val auth: FirebaseAuth,
                                                 private val firestore : FirebaseFirestore
)
    : CategoryRepository {


    override fun getCategoryById(id: String): Categories {
        // Use the Firestore API to retrieve the category document with the given ID
        val documentTask = firestore.collection("categories").document(id).get()
        var category = Categories("", "")
        // Set a listener to be called when the task succeeds
        documentTask.addOnSuccessListener { document ->
            val data = document.data
            // Map the data from the document to a Category object and update the category variable
            category = Categories(data?.get("categoryName") as String, data?.get("categoryId") as String)
        }
        // Return the category variable, which will be updated when the task succeeds
        return category
    }


    override suspend fun getAllCategories(): UiState<List<Categories>> {
        return try {
            val snapshot = firestore.collection("categories").get().await()
            val categories = mutableListOf<Categories>()
            snapshot.documents.forEach { document ->
                val data = document.data
                val categoryName = data?.get("categoryName") as String?
                val categoryId = data?.get("categoryId") as String?
                val category = Categories(categoryName,categoryId)
                categories.add(category)
            }
            UiState.Success(categories)
        } catch (e: Exception) {
            UiState.Error(e.message ?: "Unknown error")
        }
    }


    override fun addCategory(category: Categories) {

        val docRef = firestore.collection("categories").document()
        val docId = docRef.id
        val data = hashMapOf(
            "categoryName" to category.categoryName,
            "categoryId" to docId
        )
        docRef.set(data)

    }

    override suspend fun updateCategory(category: Categories): UiState<Nothing?> = suspendCoroutine { cont ->
        firestore.collection("categories").document(category.categoryId!!).addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                // Handle the exception
                cont.resume(UiState.Error(exception.message!!))
                return@addSnapshotListener
            }
            if (snapshot != null) {
                // Check if the document exists
                if (snapshot.exists()) {
                    // Use the Firestore API to update the document with the given ID in the "categories" collection
                    val data = hashMapOf(
                        "categoryName" to category.categoryName
                    )
                    snapshot.reference.set(data).addOnSuccessListener {
                        // Resume with Success
                        cont.resume(UiState.Success(null))
                    }.addOnFailureListener {
                        // Resume with Error
                        cont.resume(UiState.Error(it.message!!))
                    }
                } else {
                    // The document does not exist, so resume with Error
                    cont.resume(UiState.Error("Category not found"))
                }
            }
        }
    }

    override suspend fun deleteCategory(categoryId: String): UiState<Nothing?> {
        return try {
            firestore.collection("categories").document(categoryId).delete().await()
            UiState.Success(null)
        } catch (e: Exception) {
            UiState.Error(e.message!!)
        }
    }
}