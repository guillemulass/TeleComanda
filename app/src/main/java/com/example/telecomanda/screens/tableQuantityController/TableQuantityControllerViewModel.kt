package com.example.telecomanda.screens.tableQuantityController

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.example.telecomanda.dataClasses.Table
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TableQuantityControllerViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    val tableQuantity = MutableLiveData<Int>()

    fun getTableQuantity(onQuantityReceived: (Int) -> Unit) {
        val user = auth.currentUser?.email ?: return
        db.collection("restaurants").document(user).get().addOnSuccessListener {
            val tableQuantity = it.get("TableQuantity").toString()
            val quantity = tableQuantity.toIntOrNull() ?: 0
            onQuantityReceived(quantity)
        }
    }

    fun editTableQuantity(onQuantityUpdated: (Int) -> Unit) {
        val user = auth.currentUser?.email ?: return

        getTableQuantity { currentQuantity ->
            val newQuantity = currentQuantity + 1
            generateUniqueCode { uniqueCode ->
                val newTable = Table(number = newQuantity, code = uniqueCode)

                db.collection("restaurants").document(user).collection("tables")
                    .document(newTable.number.toString()).set(newTable)
                    .addOnSuccessListener {
                        db.collection("restaurants").document(user).update("TableQuantity", newQuantity)
                            .addOnSuccessListener {
                                onQuantityUpdated(newQuantity)
                                fetchTables() // Fetch updated list of tables
                            }
                            .addOnFailureListener { e ->
                                // Handle update error
                            }
                    }
                    .addOnFailureListener { e ->
                        // Handle save error
                    }
            }
        }
    }

    private fun generateUniqueCode(onCodeGenerated: (String) -> Unit) {
        val user = auth.currentUser?.email ?: return

        db.collection("restaurants").document(user).collection("tables").get()
            .addOnSuccessListener { result ->
                val existingCodes = result.mapNotNull { it.getString("code") }.toSet()
                var uniqueCode: String

                do {
                    uniqueCode = (100000..999999).random().toString()
                } while (existingCodes.contains(uniqueCode))

                onCodeGenerated(uniqueCode)
            }
            .addOnFailureListener { e ->
                // Handle fetch error
            }
    }

    private fun fetchTables() {
        val user = auth.currentUser?.email ?: return
        db.collection("restaurants").document(user).collection("tables").get()
            .addOnSuccessListener { result ->
                val tables = result.mapNotNull { it.toObject(Table::class.java) }
                // Update LiveData or state with the fetched tables if necessary
            }
            .addOnFailureListener { e ->
                // Handle fetch error
            }
    }
}
