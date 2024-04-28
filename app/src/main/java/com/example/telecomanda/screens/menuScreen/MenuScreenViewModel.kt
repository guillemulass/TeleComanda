package com.example.telecomanda.screens.menuScreen

import androidx.lifecycle.ViewModel
import com.example.telecomanda.dataClasses.Dish
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MenuScreenViewModel : ViewModel()  {

    private val db = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun getDishData(onSuccess: (List<Dish>) -> Unit, onFailure: (Exception) -> Unit) {
        // Obtener una referencia a la colección "restaurants" para el usuario actual
        val restaurantsRef = db.collection("restaurants").document(auth.currentUser?.email!!)

        // Obtener una referencia a la colección "items" dentro del documento del usuario
        val itemsCollectionRef = restaurantsRef.collection("items").document()

        // Obtener una referencia a la colección "dishes" dentro del documento del usuario
        val dishesCollectionRef = itemsCollectionRef.collection("dishes")

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

    fun getDrinkData(onSuccess: (List<Dish>) -> Unit, onFailure: (Exception) -> Unit) {
        // Obtener una referencia a la colección "restaurants" para el usuario actual
        val restaurantsRef = db.collection("restaurants").document(auth.currentUser?.email!!)

        // Obtener una referencia a la colección "items" dentro del documento del usuario
        val itemsCollectionRef = restaurantsRef.collection("items").document()

        // Obtener una referencia a la colección "drinks" dentro del documento del usuario
        val drinksCollectionRef = itemsCollectionRef.collection("drinks")

        // Obtener todos los documentos de la colección "drinks"
        drinksCollectionRef.get()
            .addOnSuccessListener { drinks ->
                val drinkList = mutableListOf<Dish>()
                for (drink in drinks) {
                    // Convertir cada documento en un objeto Dish y añadirlo a la lista
                    val drinkToadd = drink.toObject(Dish::class.java)
                    drinkList.add(drinkToadd)
                }
                onSuccess(drinkList)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

}
