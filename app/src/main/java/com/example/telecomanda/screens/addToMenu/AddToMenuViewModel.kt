package com.example.telecomanda.screens.addToMenu

import android.net.Uri
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
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AddToMenuViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val storage = FirebaseStorage.getInstance()

    private val _isDrink = MutableLiveData<Boolean>()
    val isDrink: LiveData<Boolean> = _isDrink

    private val _dishOrDrinkString = MutableLiveData<String>()
    val dishOrDrinkString: LiveData<String> = _dishOrDrinkString

    private val _stateText = MutableLiveData<String>()
    val stateText: LiveData<String> = _stateText

    val dishTypesArray = DishTypes.values().toList()
    val drinkTypesArray = DrinkTypes.values().toList()

    init {
        _isDrink.value = false
        _dishOrDrinkString.value = "Plato"
    }

    private suspend fun getRestaurantName(): String {
        val email = auth.currentUser?.email ?: return ""
        val document = db.collection("restaurantsEmail").document(email).get().await()
        return document.getString("restaurantName") ?: ""
    }

    fun drinkOrDishAlternator() {
        if (_isDrink.value == true) {
            _isDrink.value = false
            _dishOrDrinkString.value = "Plato"
        } else {
            _isDrink.value = true
            _dishOrDrinkString.value = "Bebida"
        }
    }

    fun saveDish(name: String, price: String, type: String, ingredients: List<String>, imageUri: Uri?) {
        viewModelScope.launch {
            try {
                val restaurantName = getRestaurantName()
                if (restaurantName.isNotEmpty()) {
                    val imageUrl = imageUri?.let { uploadImageToStorage(it) }
                    val dishToAdd = Dish(name, price, type, ingredients, imageUrl)
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

    fun saveDrink(name: String, price: String, type: String, imageUri: Uri?) {
        viewModelScope.launch {
            try {
                val restaurantName = getRestaurantName()
                if (restaurantName.isNotEmpty()) {
                    val imageUrl = if (imageUri != null) uploadImageToStorage(imageUri) else null
                    if (imageUrl == null && imageUri != null) {
                        updateStateText("Error al subir la imagen")
                        return@launch
                    }
                    val drinkToAdd = Drink(name, price, type, imageUrl)
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

    private suspend fun uploadImageToStorage(imageUri: Uri): String? {
        return try {
            val storageRef = storage.reference.child("images/${imageUri.lastPathSegment}")
            storageRef.putFile(imageUri).await()
            storageRef.downloadUrl.await().toString()
        } catch (e: Exception) {
            updateStateText("Error al subir la imagen: ${e.message}")
            null
        }
    }

    private fun updateStateText(text: String) {
        _stateText.value = text
    }
}
