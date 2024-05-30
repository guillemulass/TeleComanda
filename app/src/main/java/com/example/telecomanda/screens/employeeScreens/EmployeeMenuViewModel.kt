package com.example.telecomanda.screens.employeeScreens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.telecomanda.dataClasses.Dish
import com.example.telecomanda.dataClasses.Drink
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class EmployeeMenuViewModel : ViewModel()  {

    private val db = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private suspend fun getRestaurantName(): String {
        val email = auth.currentUser?.email ?: return ""
        val document = db.collection("employees").document(email).get().await()
        return document.getString("restaurantName") ?: ""
    }

    fun getDrinkData(onSuccess: (List<Drink>) -> Unit, onFailure: (Exception) -> Unit) {
        viewModelScope.launch {
            try {
                val restaurantName = getRestaurantName()
                if (restaurantName.isNotEmpty()) {
                    val drinksCollectionRef = db.collection("restaurants").document(restaurantName).collection("drinks")
                    val drinks = drinksCollectionRef.get().await()
                    val drinkList = drinks.map { it.toObject(Drink::class.java) }
                    onSuccess(drinkList)
                } else {
                    onFailure(Exception("Restaurant name is empty"))
                }
            } catch (e: Exception) {
                onFailure(e)
            }
        }
    }

    fun getDishData(onSuccess: (List<Dish>) -> Unit, onFailure: (Exception) -> Unit) {
        viewModelScope.launch {
            try {
                val restaurantName = getRestaurantName()
                if (restaurantName.isNotEmpty()) {
                    val dishesCollectionRef = db.collection("restaurants").document(restaurantName).collection("dishes")
                    val dishes = dishesCollectionRef.get().await()
                    val dishList = dishes.map { it.toObject(Dish::class.java) }
                    onSuccess(dishList)
                } else {
                    onFailure(Exception("Restaurant name is empty"))
                }
            } catch (e: Exception) {
                onFailure(e)
            }
        }
    }
}
