package com.example.telecomanda.screens.closeRegister

import androidx.lifecycle.ViewModel
import com.example.telecomanda.dataClasses.OrderItem
import com.example.telecomanda.dataClasses.Table
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class CloseRegisterViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private suspend fun getRestaurantName(): String {
        val email = auth.currentUser?.email ?: return ""
        val document = db.collection("restaurantsEmail").document(email).get().await()
        return document.getString("restaurantName") ?: ""
    }

    suspend fun getTodaysTotal(): Double {
        val restaurantName = getRestaurantName()
        val document = db.collection("restaurants").document(restaurantName).get().await()
        return document.getDouble("todaysTotal") ?: 0.0
    }

    suspend fun getRestaurantState(): Boolean {
        val restaurantName = getRestaurantName()
        val document = db.collection("restaurants").document(restaurantName).get().await()
        return document.getBoolean("restaurantOpen") ?: false
    }

    suspend fun toggleRestaurantState() {
        val restaurantName = getRestaurantName()
        val documentReference = db.collection("restaurants").document(restaurantName)
        val currentState = getRestaurantState()
        val newState = !currentState

        documentReference.update("restaurantOpen", newState).await()
    }



    suspend fun closeRegister(todaysTotal: Double) {
        val restaurantName = getRestaurantName()
        val tablesCollection = db.collection("restaurants").document(restaurantName).collection("tables")
        val tables = tablesCollection.get().await()
        var total = todaysTotal

        // Sumar todas las comandas de las mesas al total de hoy
        for (tableDocument in tables) {
            val table = tableDocument.toObject(Table::class.java)
            table.orders?.forEach { order ->
                total += (order.price.toDoubleOrNull() ?: 0.0) * order.quantity
            }
            // Eliminar todas las comandas de la mesa
            tablesCollection.document(tableDocument.id).update("orders", emptyList<OrderItem>()).await()
        }

        // Actualizar el total de hoy en Firebase
        db.collection("restaurants").document(restaurantName).update("todaysTotal", total).await()

        // Restablecer el total de hoy a 0
        db.collection("restaurants").document(restaurantName).update("todaysTotal", 0).await()
    }
}
