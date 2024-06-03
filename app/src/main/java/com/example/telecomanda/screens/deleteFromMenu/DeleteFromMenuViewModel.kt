package com.example.telecomanda.screens.deleteFromMenu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.telecomanda.dataClasses.Dish
import com.example.telecomanda.dataClasses.Drink
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class DeleteFromMenuViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private suspend fun getRestaurantName(): String {
        val email = auth.currentUser?.email ?: return ""
        val document = db.collection("restaurantsEmail").document(email).get().await()
        return document.getString("restaurantName") ?: ""
    }

    fun getDrinkData(onSuccess: (List<Drink>) -> Unit, onFailure: (Exception) -> Unit) {
        viewModelScope.launch {
            try {
                val drinksCollectionRef = db.collection("restaurants").document(getRestaurantName()).collection("drinks")
                val drinks = drinksCollectionRef.get().await()
                val drinkList = drinks.map { it.toObject(Drink::class.java) }
                onSuccess(drinkList)
            } catch (exception: Exception) {
                onFailure(exception)
            }
        }
    }

    fun getDishData(onSuccess: (List<Dish>) -> Unit, onFailure: (Exception) -> Unit) {
        viewModelScope.launch {
            try {
                val dishesCollectionRef = db.collection("restaurants").document(getRestaurantName()).collection("dishes")
                val dishes = dishesCollectionRef.get().await()
                val dishList = dishes.map { it.toObject(Dish::class.java) }
                onSuccess(dishList)
            } catch (exception: Exception) {
                onFailure(exception)
            }
        }
    }

    suspend fun deleteItemFromMenu(type: String, name: String) {
        val restaurantName = getRestaurantName()
        val collectionRef = db.collection("restaurants").document(restaurantName).collection(if (type == "Dish") "dishes" else "drinks")
        val documents = collectionRef.whereEqualTo("name", name).get().await()
        for (document in documents) {
            collectionRef.document(document.id).delete().await()
        }
    }
}
