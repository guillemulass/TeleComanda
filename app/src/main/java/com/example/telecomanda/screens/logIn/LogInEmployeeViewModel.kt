package com.example.telecomanda.screens.logIn

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LogInEmployeeViewModel : ViewModel()  {

    private val db = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun getData(restaurantEmail : String, employeeName : String, employeePassword: String,
                onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit
    ) {
        // Obtener una referencia al documento del empleado en la colección correspondiente
        val employeeRef = db.collection("restaurants")
            .document(restaurantEmail)
            .collection("employees")
            .document(employeeName)

        // Obtener el documento del empleado
        employeeRef.get().addOnSuccessListener { document ->
            if (document != null && document.exists()) {
                // El documento existe, obtener los datos
                val name = document.getString("name")
                val password = document.getString("password")

                // Hacer lo que necesites con la contraseña obtenida
                if (password != null) {
                    if (password == employeePassword){
                        onSuccess("true")
                    } else {
                        onSuccess("Contraseña incorrecta")
                    }
                } else {
                    onSuccess("El campo password está ausente en el documento")
                }
            } else {
                // El documento está vacío, lo que significa que el nombre de usuario no existe
                onSuccess("Nombre de usuario incorrecto")
            }
        }.addOnFailureListener { exception ->
            onFailure(exception)
        }
    }
}
