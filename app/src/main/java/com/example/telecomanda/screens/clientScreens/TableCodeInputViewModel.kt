package com.example.telecomanda.screens.clientScreens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class TableCodeInputViewModel : ViewModel() {

    var tableCode by mutableStateOf("")
        private set

    var restaurantEmail by mutableStateOf("")
        private set

    private val db = FirebaseFirestore.getInstance()

    private val _isValidTable = MutableLiveData<Boolean>()
    val isValidTable: LiveData<Boolean> = _isValidTable

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

    fun updateRestaurantEmail(email: String) {
        restaurantEmail = email
    }
}
