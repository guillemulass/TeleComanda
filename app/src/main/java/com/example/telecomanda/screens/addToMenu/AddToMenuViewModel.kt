package com.example.telecomanda.screens.addToMenu

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.telecomanda.EnumClass.DishTypes
import com.example.telecomanda.EnumClass.DrinkTypes
import com.example.telecomanda.dataClasses.Dish
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AddToMenuViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _isDish = MutableLiveData<Boolean>()
    val isDish : LiveData<Boolean> = _isDish

    private val _isDrink = MutableLiveData<Boolean>()
    val isDrink : LiveData<Boolean> = _isDrink

    private val _dishOrDrinkString = MutableLiveData<String>()
    val dishOrDrinkString : LiveData<String> = _dishOrDrinkString

    private val _stateText = MutableLiveData<String>()
    var stateText : MutableLiveData<String> = _stateText

    val dishTypesArray = DishTypes.values().toList()

    val drinkTypesArray = DrinkTypes.values().toList()



    fun changeDishTypeSelector(dishTypes: DishTypes) {
        // Implementa la lógica según tus necesidades, por ejemplo:
        when (dishTypes) {
            DishTypes.Primero -> {
                // Realizar acciones cuando se selecciona "Primero"
            }
            DishTypes.Segundo -> {
                // Realizar acciones cuando se selecciona "Segundo"
            }
            DishTypes.Postre -> {
                // Realizar acciones cuando se selecciona "Postre"
            }
        }
    }

    init{
        _isDish.value = true
        _isDrink.value = false
        _dishOrDrinkString.value = "Plato"
    }

    fun drinkOrDishAlternator(){
        if (_isDish.value!!){
            _isDish.value = false
            _isDrink.value = true
            _dishOrDrinkString.value = "Bebida"
        } else{
            _isDish.value = true
            _isDrink.value = false
            _dishOrDrinkString.value = "Plato"
        }
    }

    fun saveDish(name : String, price : String){
        val dishToAdd = Dish(name, price)

        // Obtener una referencia a la colección "restaurants" para el usuario actual
        val restaurantsRef = db.collection("restaurants").document(auth.currentUser?.email!!)

        // Crear una colección llamada "items" dentro del documento del usuario
        val itemsCollectionRef = restaurantsRef.collection("items")

        // Crear una colección llamada "dishes" dentro del documento del usuario
        val dishesCollectionRef = restaurantsRef.collection("dishes")

        // Agregar el libro a la colección "books"
        itemsCollectionRef.document(name).set(dishToAdd)
    }

    fun saveDrink(name : String, price : String){
        val dishToAdd = Dish(name, price)

        // Obtener una referencia a la colección "restaurants" para el usuario actual
        val restaurantsRef = db.collection("restaurants").document(auth.currentUser?.email!!)

        // Crear una colección llamada "items" dentro del documento del usuario
        val itemsCollectionRef = restaurantsRef.collection("items").document()

        // Crear una colección llamada "dishes" dentro del documento del usuario
        val dishesCollectionRef = itemsCollectionRef.collection("drinks")

        // Agregar el libro a la colección "books"
        dishesCollectionRef.document(name).set(dishToAdd)
    }

    fun updateStateText(text: String) {
        _stateText.value = text
    }


}