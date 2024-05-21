package com.example.telecomanda.screens.tableController

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.telecomanda.dataClasses.Employee
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TableControllerViewModel: ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _stateText = MutableLiveData<String>()
    var stateText: MutableLiveData<String> = _stateText

    fun addEmployee(name: String, password: String) {
        val employeeToAdd = Employee(name, password)

        // Obtener una referencia a la colección "restaurants" para el usuario actual
        val restaurantsRef = db.collection("restaurants").document(auth.currentUser?.email!!)

        // Crear una colección llamada "employees" dentro del documento del usuario
        val dishesCollectionRef = restaurantsRef.collection("employees")

        dishesCollectionRef.document(name).set(employeeToAdd)
    }

    fun updateStateText(text: String) {
        _stateText.value = text
    }

}