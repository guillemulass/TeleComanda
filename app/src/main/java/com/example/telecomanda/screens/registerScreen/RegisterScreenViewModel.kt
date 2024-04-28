package com.example.telecomanda.screens.registerScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.telecomanda.dataClasses.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class RegisterScreenViewModel : ViewModel() {

    private val auth: FirebaseAuth = Firebase.auth
    private val firestore = Firebase.firestore
    private val db = FirebaseFirestore.getInstance()

    var restaurantName by mutableStateOf("")
        private set
    var email by mutableStateOf("")
        private set
    private var passw by mutableStateOf("")

    fun registerRestaurant(
        onSuccess : () -> Unit,
        onFailure:() -> Unit
    ) {

        //saveUserName()

        viewModelScope.launch {
            try {
                auth.createUserWithEmailAndPassword(email,passw).addOnCompleteListener {
                    saveUser(UserModel(email, restaurantName))
                    onSuccess()
                }.addOnFailureListener {
                    onFailure()
                }
            }
            catch (e:Exception) {
                println("Error de JetPack")
            }

        }

    }

    private fun saveUserName(){

        // Obtener una referencia a la colección "restaurants" para el usuario actual
        val userBooksRef = db.collection("restaurants").document(auth.currentUser?.email!!)

        // Agregar el libro a la colección "books"
        userBooksRef.set(restaurantName)
    }

    private fun saveUser(userToAdd:UserModel) {
        viewModelScope.launch {
            firestore.collection("restaurants").document(email).set(userToAdd).addOnCompleteListener {
                println("Restaurante guardado en base de datos correctamente")
            }.addOnFailureListener {
                println("Error guardando restaurante en base de datos")
            }
        }

    }
    fun changeRestaurantName(restaurantName:String) {
        this.restaurantName = restaurantName
    }
    fun changeEmail(email:String) {
        this.email = email
    }
    fun changePassw(passw:String) {
        this.passw = passw
    }

}