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
    var restaurantEmail by mutableStateOf("")
        private set
    private var passw by mutableStateOf("")
    var statusText by mutableStateOf("")

    fun registerRestaurant(
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                // Verificar si el nombre del restaurante ya está en uso
                firestore.collection("restaurants").document(restaurantName).get()
                    .addOnSuccessListener { document ->
                        if (document.exists()) {
                            // El nombre del restaurante ya está en uso
                            statusText = "Nombre de restaurante ya usado"
                            onFailure()
                        } else {
                            // El nombre del restaurante no está en uso, proceder con el registro
                            auth.createUserWithEmailAndPassword(restaurantEmail, passw)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        val user = task.result.user
                                        user?.let {
                                            saveRestaurantInfoList(it.uid)
                                            saveRestaurantEmail(restaurantEmail, restaurantName) // Guardar el nombre del restaurante en la ruta especificada
                                            onSuccess()
                                        }
                                    } else {
                                        onFailure()
                                    }
                                }
                                .addOnFailureListener {
                                    onFailure()
                                }
                        }
                    }
                    .addOnFailureListener {
                        onFailure()
                    }
            } catch (e: Exception) {
                println("Error de JetPack")
                onFailure()
            }
        }
    }


    private fun saveRestaurantInfoList(uid: String) {
        val restaurantInfoRef = db.collection("restaurants").document(restaurantName)

        val data = hashMapOf(
            "restaurantName" to restaurantName,
            "restaurantEmail" to restaurantEmail,
            "restaurantId" to uid
        )

        restaurantInfoRef.set(data).addOnCompleteListener {
            println("Información del restaurante guardada correctamente")
        }.addOnFailureListener {
            println("Error guardando la información del restaurante")
        }
    }

    private fun saveRestaurantEmail(email: String, name: String) {
        val restaurantEmailRef = db.collection("restaurantsEmail").document(email)

        val data = hashMapOf(
            "restaurantName" to name
        )

        restaurantEmailRef.set(data).addOnCompleteListener {
            println("Nombre del restaurante guardado correctamente en /restaurantsEmail")
        }.addOnFailureListener {
            println("Error guardando el nombre del restaurante en /restaurantsEmail")
        }
    }

    fun changeRestaurantName(restaurantName: String) {
        this.restaurantName = restaurantName
    }

    fun changeEmail(email: String) {
        this.restaurantEmail = email
    }

    fun changePassw(passw: String) {
        this.passw = passw
    }
}
