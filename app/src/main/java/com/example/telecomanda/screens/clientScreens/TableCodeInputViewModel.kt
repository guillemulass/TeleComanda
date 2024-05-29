package com.example.telecomanda.screens.clientScreens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.telecomanda.dataClasses.Table
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class TableCodeInputViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val firestore = Firebase.firestore

    private val _isValidTable = MutableLiveData<Boolean>()

    var tableCode by mutableStateOf("")
        private set

    var restaurantName by mutableStateOf("")
        private set

    private var restaurantEmail by mutableStateOf("")

    fun checkTableCode(onSuccess: () -> Unit, onFailure: () -> Unit) {
        val restaurantRef = db.collection("restaurants").document(restaurantEmail)
        val tableRef = restaurantRef.collection("tables").whereEqualTo("code", tableCode)

        tableRef.get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    _isValidTable.value = true
                    onSuccess()
                } else {
                    _isValidTable.value = false
                    onFailure()
                }
            }
            .addOnFailureListener {
                _isValidTable.value = false
                onFailure()
            }
    }

    fun updateTableCode(code: String) {
        tableCode = code
    }

    fun updateRestaurantName(name: String) {
        restaurantName = name
    }

    fun getRestaurantEmailByName(
        restaurantName: String,
        onSuccess: (String?) -> Unit,
        onFailure: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                val document =
                    firestore.collection("restaurantsInfoList").document(restaurantName).get()
                        .await()
                if (document.exists()) {
                    val email = document.getString("restaurantEmail")
                    onSuccess(email)
                } else {
                    onSuccess(null)
                }
            } catch (e: Exception) {
                println("Error obteniendo el email del restaurante: ${e.message}")
                onFailure()
            }
        }
    }

    fun updateRestaurantEmail(email: String) {
        restaurantEmail = email
    }

    fun getTableNumberByCode(
        restaurantEmail: String,
        tableCode: String,
        onSuccess: (Int?) -> Unit,
        onFailure: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                val tablesCollection = db.collection("restaurants").document(restaurantEmail).collection("tables")
                val tablesSnapshot = tablesCollection.get().await()
                for (document in tablesSnapshot.documents) {
                    val table = document.toObject(Table::class.java)
                    if (table?.code == tableCode) {
                        onSuccess(table.number)
                        return@launch
                    }
                }
                onSuccess(null)
            } catch (e: Exception) {
                println("Error obteniendo el número de mesa: ${e.message}")
                onFailure()
            }
        }
    }
}
