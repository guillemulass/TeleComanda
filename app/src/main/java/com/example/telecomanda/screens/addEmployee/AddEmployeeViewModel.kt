package com.example.telecomanda.screens.addEmployee

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AddEmployeeViewModel: ViewModel() {

    private val auth: FirebaseAuth = Firebase.auth
    private val firestore = Firebase.firestore
    private val db = FirebaseFirestore.getInstance()

    var restaurantName by mutableStateOf("")
        private set
    var restaurantUID by mutableStateOf("")
        private set
    var employeeEmail by mutableStateOf("")
        private set

    private var passw by mutableStateOf("")

    var statusText by mutableStateOf("")

    fun registerEmployee(
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ){
        viewModelScope.launch {
            try {
                val id = getRestaurantUID()
                if (restaurantUID == id) {
                    auth.createUserWithEmailAndPassword(employeeEmail, passw).addOnCompleteListener {
                        saveUser()
                        onSuccess()
                    }.addOnFailureListener {
                        onFailure()
                    }
                } else {
                    statusText = "El id no es correcto" +
                            "\n id: $restaurantUID, idfun: $id " +
                            "\n restname: $restaurantName "
                }
            } catch (e: Exception) {
                println("Error de JetPack")
            }
        }
    }

    private suspend fun getRestaurantUID(): String {
        val document = db.collection("restaurantsInfoList").document(restaurantName).get().await()
        return document.getString("restaurantId") ?: ""
    }

    private fun saveUser() {
        viewModelScope.launch {
            val userMap = mapOf(
                "restaurantName" to restaurantName,
                "employeeEmail" to employeeEmail
            )
            firestore.collection("employees").document(employeeEmail).set(userMap).addOnCompleteListener {
                println("Usuario guardado en base de datos correctamente")
            }.addOnFailureListener {
                println("Error guardando usuario en base de datos")
            }
        }
    }

    fun changeRestaurantName(restaurantName: String) {
        this.restaurantName = restaurantName
    }

    fun changeRestaurantUID(uid: String) {
        this.restaurantUID = uid
    }

    fun changeEmail(email: String) {
        this.employeeEmail = email
    }

    fun changePassw(passw: String) {
        this.passw = passw
    }
}
