package com.example.telecomanda.screens.addEmployee

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.telecomanda.dataClasses.Dish
import com.example.telecomanda.dataClasses.Employee
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AddEmployeeViewModel: ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _stateText = MutableLiveData<String>()
    var stateText : MutableLiveData<String> = _stateText

    fun addEmployee(name : String, password : String){
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

/*

    private val _employeeName = MutableLiveData<String>()
    var employeeName : MutableLiveData<String> = _employeeName

    private val _employeePassword = MutableLiveData<String>()
    var employeePassword : MutableLiveData<String> = _employeePassword

    fun updateEmployeeName(text: String) {
        _employeeName.value = text
    }
    fun updateEmployeePassword(text: String) {
        _employeeName.value = text
    }

    init{
        _employeeName.value = ""
        _employeePassword.value = ""

    }

    fun addEmployee(){
        val employeeToAdd = Employee(employeeName.value!!, employeeName.value!!)

        // Obtener una referencia a la colección "restaurants" para el usuario actual
        val restaurantsRef = db.collection("restaurants").document(auth.currentUser?.email!!)

        // Crear una colección llamada "employees" dentro del documento del usuario
        val dishesCollectionRef = restaurantsRef.collection("employees")

        dishesCollectionRef.document(employeeName.value!!).set(employeeToAdd)
    }
 */


}