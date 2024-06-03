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
                val restaurantName = getRestaurantName()
                if (restaurantName.isNotEmpty()) {
                    val document = db.collection("restaurants").document(restaurantName).get().await()
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
                val restaurantName = getRestaurantName()
                if (restaurantName.isNotEmpty()) {
                    launch {
                        val currentQuantity = getTableQuantityAsync(restaurantName)
                        val newQuantity = currentQuantity + 1
                        val uniqueCode = generateUniqueCodeAsync(restaurantName)

                        val newTable = Table(number = newQuantity, code = uniqueCode)
                        db.collection("restaurants").document(restaurantName).collection("tables")
                            .document(newTable.number.toString()).set(newTable).await()

                        db.collection("restaurants").document(restaurantName).update("TableQuantity", newQuantity).await()

                        onQuantityUpdated(newQuantity)
                        fetchTables(restaurantName)
                    }
                } else {
                    onQuantityUpdated(0)
                }
            } catch (e: Exception) {
                onQuantityUpdated(0)
            }
        }
    }

    fun deleteLastTable(onQuantityUpdated: (Int) -> Unit) {
        viewModelScope.launch {
            try {
                val restaurantName = getRestaurantName()
                if (restaurantName.isNotEmpty()) {
                    launch {
                        val currentQuantity = getTableQuantityAsync(restaurantName)
                        if (currentQuantity > 0) {
                            val lastTableNumber = currentQuantity
                            db.collection("restaurants").document(restaurantName).collection("tables")
                                .document(lastTableNumber.toString()).delete().await()

                            val newQuantity = currentQuantity - 1
                            db.collection("restaurants").document(restaurantName).update("TableQuantity", newQuantity).await()

                            onQuantityUpdated(newQuantity)
                            fetchTables(restaurantName)
                        }
                    }
                } else {
                    onQuantityUpdated(0)
                }
            } catch (e: Exception) {
                onQuantityUpdated(0)
            }
        }
    }

    fun deleteTable(tableNumber: Int, onQuantityUpdated: (Int) -> Unit) {
        viewModelScope.launch {
            try {
                val restaurantName = getRestaurantName()
                if (restaurantName.isNotEmpty()) {
                    launch {
                        db.collection("restaurants").document(restaurantName).collection("tables")
                            .document(tableNumber.toString()).delete().await()

                        val currentQuantity = getTableQuantityAsync(restaurantName)
                        val newQuantity = if (currentQuantity > 0) currentQuantity - 1 else 0
                        db.collection("restaurants").document(restaurantName).update("TableQuantity", newQuantity).await()

                        onQuantityUpdated(newQuantity)
                        fetchTables(restaurantName)
                    }
                } else {
                    onQuantityUpdated(0)
                }
            } catch (e: Exception) {
                onQuantityUpdated(0)
            }
        }
    }

    private suspend fun getTableQuantityAsync(restaurantName: String): Int {
        val document = db.collection("restaurants").document(restaurantName).get().await()
        return document.getLong("TableQuantity")?.toInt() ?: 0
    }

    private suspend fun generateUniqueCodeAsync(restaurantName: String): String {
        val result = db.collection("restaurants").document(restaurantName).collection("tables").get().await()
        val existingCodes = result.mapNotNull { it.getString("code") }.toSet()
        var uniqueCode: String

        do {
            uniqueCode = (100000..999999).random().toString()
        } while (existingCodes.contains(uniqueCode))

        return uniqueCode
    }

    private suspend fun fetchTables(restaurantName: String) {
        val result = db.collection("restaurants").document(restaurantName).collection("tables").get().await()
        val tables = result.mapNotNull { it.toObject(Table::class.java) }
        // Update LiveData or state with the fetched tables if necessary
    }
}
