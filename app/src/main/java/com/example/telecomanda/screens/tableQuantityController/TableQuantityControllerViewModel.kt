package com.example.telecomanda.screens.tableQuantityController

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.telecomanda.dataClasses.Table
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class TableQuantityControllerViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    val tableQuantity = MutableLiveData<Int>()

    private suspend fun getRestaurantName(): String {
        val email = auth.currentUser?.email ?: return ""
        val document = db.collection("restaurantsEmail").document(email).get().await()
        return document.getString("restaurantName") ?: ""
    }

    fun getTableQuantity(onQuantityReceived: (Int) -> Unit) {
        viewModelScope.launch {
            try {
                val user = getRestaurantName()
                if (user.isNotEmpty()) {
                    val document = db.collection("restaurants").document(user).get().await()
                    val tableQuantity = document.getLong("TableQuantity")?.toInt() ?: 0
                    onQuantityReceived(tableQuantity)
                } else {
                    onQuantityReceived(0)
                }
            } catch (e: Exception) {
                onQuantityReceived(0)
            }
        }
    }

    fun editTableQuantity(onQuantityUpdated: (Int) -> Unit) {
        viewModelScope.launch {
            try {
                val user = getRestaurantName()
                if (user.isNotEmpty()) {
                    launch {
                        val currentQuantity = getTableQuantityAsync(user)
                        val newQuantity = currentQuantity + 1
                        val uniqueCode = generateUniqueCodeAsync(user)

                        val newTable = Table(number = newQuantity, code = uniqueCode)
                        db.collection("restaurants").document(user).collection("tables")
                            .document(newTable.number.toString()).set(newTable).await()

                        db.collection("restaurants").document(user).update("TableQuantity", newQuantity).await()

                        onQuantityUpdated(newQuantity)
                        fetchTables(user)
                    }
                } else {
                    onQuantityUpdated(0)
                }
            } catch (e: Exception) {
                onQuantityUpdated(0)
            }
        }
    }

    private suspend fun getTableQuantityAsync(user: String): Int {
        val document = db.collection("restaurants").document(user).get().await()
        return document.getLong("TableQuantity")?.toInt() ?: 0
    }

    private suspend fun generateUniqueCodeAsync(user: String): String {
        val result = db.collection("restaurants").document(user).collection("tables").get().await()
        val existingCodes = result.mapNotNull { it.getString("code") }.toSet()
        var uniqueCode: String

        do {
            uniqueCode = (100000..999999).random().toString()
        } while (existingCodes.contains(uniqueCode))

        return uniqueCode
    }

    private suspend fun fetchTables(user: String) {
        val result = db.collection("restaurants").document(user).collection("tables").get().await()
        val tables = result.mapNotNull { it.toObject(Table::class.java) }
        // Update LiveData or state with the fetched tables if necessary
    }
}
