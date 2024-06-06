package com.example.telecomanda.screens.logIn

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class LogInViewModel : ViewModel()  {
    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set

    private val auth: FirebaseAuth = Firebase.auth

    fun changeEmail(email:String) {
        this.email = email
    }
    fun changePassword(password:String) {
        this.password = password
    }

    fun login(onSuccess: () -> Unit){
        viewModelScope.launch {
            try {
                // DCS - Utiliza el servicio de autenticación de Firebase para validar al usuario
                // por email y contraseña
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            onSuccess()
                        } else {
                            Log.d("ERROR EN FIREBASE","Usuario y/o contrasena incorrectos")
                        }
                    }
            } catch (e: Exception){
                Log.d("ERROR EN JETPACK", "ERROR: ${e.localizedMessage}")
            }
        }
    }

    fun checkAdminEmail(email: String, onResult: (Boolean) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val documentReference = db.collection("restaurantsEmail").document(email)

        documentReference.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    onResult(true)
                } else {
                    onResult(false)
                }
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
                onResult(false)
            }
    }

    fun checkEmployeeEmail(email: String, onResult: (Boolean) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val documentReference = db.collection("employeesEmails").document(email)

        documentReference.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    onResult(true)
                } else {
                    onResult(false)
                }
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
                onResult(false)
            }
    }

}