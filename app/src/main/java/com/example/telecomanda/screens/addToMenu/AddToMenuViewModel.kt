package com.example.telecomanda.screens.addToMenu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.telecomanda.enumClass.DishTypes
import com.example.telecomanda.enumClass.DrinkTypes
import com.example.telecomanda.dataClasses.Dish
import com.example.telecomanda.dataClasses.Drink
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AddToMenuViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _isDish = MutableLiveData<Boolean>()

    private val _isDrink = MutableLiveData<Boolean>()
    val isDrink : LiveData<Boolean> = _isDrink

    private val _dishOrDrinkString = MutableLiveData<String>()
    val dishOrDrinkString : LiveData<String> = _dishOrDrinkString

    private val _stateText = MutableLiveData<String>()
    var stateText : MutableLiveData<String> = _stateText

    val dishTypesArray = DishTypes.values().toList()

    val drinkTypesArray = DrinkTypes.values().toList()


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

    fun saveDish(name : String, price : String, type: String, ingredients : List<String>){
        val dishToAdd = Dish(name, price, type, ingredients)

        // Obtener una referencia a la colección "restaurants" para el usuario actual
        val restaurantsRef = db.collection("restaurants").document(auth.currentUser?.email!!)

        // Crear una colección llamada "dishes" dentro del documento del usuario
        val dishesCollectionRef = restaurantsRef.collection("dishes")

        // Agregar el plato a la colección "dishes"
        dishesCollectionRef.document(name).set(dishToAdd)
    }

    fun saveDrink(name : String, price : String, type: String){
        val drinkToAdd = Drink(name, price, type)

        // Obtener una referencia a la colección "restaurants" para el usuario actual
        val restaurantsRef = db.collection("restaurants").document(auth.currentUser?.email!!)

        // Crear una colección llamada "drinks" dentro del documento del usuario
        val dishesCollectionRef = restaurantsRef.collection("drinks")


        // Agregar la bebida a la colección "drinks"
        dishesCollectionRef.document(name).set(drinkToAdd)
    }

    fun updateStateText(text: String) {
        _stateText.value = text
    }




}