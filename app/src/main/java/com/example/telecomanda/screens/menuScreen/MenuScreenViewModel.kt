package com.example.telecomanda.screens.menuScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.telecomanda.dataClasses.Dish
import com.example.telecomanda.dataClasses.Drink
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MenuScreenViewModel : ViewModel()  {

    private val db = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private suspend fun getRestaurantName(): String {
        val email = auth.currentUser?.email ?: return ""
        val document = db.collection("restaurantsEmail").document(email).get().await()
        return document.getString("restaurantName") ?: ""
    }

    fun getDrinkData(onSuccess: (List<Drink>) -> Unit, onFailure: (Exception) -> Unit) {
        viewModelScope.launch {
            // Obtener una referencia a la colección "drinks" en la nueva ruta
            val drinksCollectionRef = db.collection("restaurants").document(getRestaurantName()).collection("drinks")

            // Obtener todos los documentos de la colección "drinks"
            drinksCollectionRef.get()
                .addOnSuccessListener { drinks ->
                    val drinkList = mutableListOf<Drink>()
                    for (drink in drinks) {
                        // Convertir cada documento en un objeto Drink y añadirlo a la lista
                        val drinkToadd = drink.toObject(Drink::class.java)
                        drinkList.add(drinkToadd)
                    }
                    onSuccess(drinkList)
                }
                .addOnFailureListener { exception ->
                    onFailure(exception)
                }
        }
    }

    fun getDishData(onSuccess: (List<Dish>) -> Unit, onFailure: (Exception) -> Unit) {
        viewModelScope.launch {
            // Obtener una referencia a la colección "dishes" en la nueva ruta
            val dishesCollectionRef = db.collection("restaurants").document(getRestaurantName()).collection("dishes")

            // Obtener todos los documentos de la colección "dishes"
            dishesCollectionRef.get()
                .addOnSuccessListener { dishes ->
                    val dishList = mutableListOf<Dish>()
                    for (dish in dishes) {
                        // Convertir cada documento en un objeto Dish y añadirlo a la lista
                        val dishToAdd = dish.toObject(Dish::class.java)
                        dishList.add(dishToAdd)
                    }
                    onSuccess(dishList)
                }
                .addOnFailureListener { exception ->
                    onFailure(exception)
                }
        }
    }
}
