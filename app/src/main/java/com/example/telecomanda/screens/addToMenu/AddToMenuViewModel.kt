package com.example.telecomanda.screens.addToMenu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.telecomanda.enumClass.DishTypes
import com.example.telecomanda.enumClass.DrinkTypes
import com.example.telecomanda.dataClasses.Dish
import com.example.telecomanda.dataClasses.Drink
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AddToMenuViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _isDish = MutableLiveData<Boolean>()

    private val _isDrink = MutableLiveData<Boolean>()
    val isDrink: LiveData<Boolean> = _isDrink

    private val _dishOrDrinkString = MutableLiveData<String>()
    val dishOrDrinkString: LiveData<String> = _dishOrDrinkString

    private val _stateText = MutableLiveData<String>()
    val stateText: LiveData<String> = _stateText

    val dishTypesArray = DishTypes.values().toList()
    val drinkTypesArray = DrinkTypes.values().toList()

    init {
        _isDish.value = true
        _isDrink.value = false
        _dishOrDrinkString.value = "Plato"
    }

    private var restaurantName = MutableLiveData<String>()

    private suspend fun getRestaurantName(): String {
        val email = auth.currentUser?.email ?: return ""
        val document = db.collection("restaurantsEmail").document(email).get().await()
        return document.getString("restaurantName") ?: ""
    }

    fun drinkOrDishAlternator() {
        if (_isDish.value!!) {
            _isDish.value = false
            _isDrink.value = true
            _dishOrDrinkString.value = "Bebida"
        } else {
            _isDish.value = true
            _isDrink.value = false
            _dishOrDrinkString.value = "Plato"
        }
    }

    fun saveDish(name: String, price: String, type: String, ingredients: List<String>) {
        viewModelScope.launch {
            try {
                val restaurantName = getRestaurantName()
                if (restaurantName.isNotEmpty()) {
                    val dishToAdd = Dish(name, price, type, ingredients)
                    val restaurantsRef = db.collection("restaurants").document(restaurantName)
                    val dishesCollectionRef = restaurantsRef.collection("dishes")
                    dishesCollectionRef.document(name).set(dishToAdd).await()
                    updateStateText("Plato guardado correctamente")
                } else {
                    updateStateText("Error al obtener el nombre del restaurante")
                }
            } catch (e: Exception) {
                updateStateText("Error al guardar el plato: ${e.message}")
            }
        }
    }

    fun saveDrink(name: String, price: String, type: String) {
        viewModelScope.launch {
            try {
                val restaurantName = getRestaurantName()
                if (restaurantName.isNotEmpty()) {
                    val drinkToAdd = Drink(name, price, type)
                    val restaurantsRef = db.collection("restaurants").document(restaurantName)
                    val drinksCollectionRef = restaurantsRef.collection("drinks")
                    drinksCollectionRef.document(name).set(drinkToAdd).await()
                    updateStateText("Bebida guardada correctamente")
                } else {
                    updateStateText("Error al obtener el nombre del restaurante")
                }
            } catch (e: Exception) {
                updateStateText("Error al guardar la bebida: ${e.message}")
            }
        }
    }

    private fun updateStateText(text: String) {
        _stateText.value = text
    }
}