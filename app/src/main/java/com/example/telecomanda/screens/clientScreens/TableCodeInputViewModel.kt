package com.example.telecomanda.screens.clientScreens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.telecomanda.dataClasses.Table
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class TableCodeInputViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    var tableCode by mutableStateOf("")
        private set
    var tableNumber by mutableStateOf("")
        private set

    var restaurantName by mutableStateOf("")
        private set

    fun updateTableCode(code: String) {
        tableCode = code
    }

    fun updateTableNumber(number: String) {
        tableNumber = number
    }

    fun updateRestaurantName(name: String) {
        restaurantName = name
    }

    fun checkTableCode(onSuccess: () -> Unit, onFailure: () -> Unit) {
        viewModelScope.launch {
            try {
                // Referencia al documento del restaurante en Firestore
                val restaurantRef = db.collection("restaurants").document(restaurantName)
                // Referencia a la colección "tables" dentro del documento del restaurante, filtrando por "code"
                val tableRef = restaurantRef.collection("tables").whereEqualTo("code", tableCode)

                // Ejecuta la consulta
                val documents = tableRef.get().await()
                // Si se encontraron documentos, significa que el código de la mesa es válido
                if (!documents.isEmpty) {
                    onSuccess()  // Llama a la función de éxito
                } else {
                    onFailure()  // Llama a la función de fallo
                }
            } catch (e: Exception) {
                println("Error checking table code: ${e.message}")
                onFailure()  // Llama a la función de fallo
            }
        }
    }

    fun getTableNumberByCode(
        tableCode: String,
        onSuccess: (Int?) -> Unit,
        onFailure: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                val tablesCollection = db.collection("restaurants").document(restaurantName).collection("tables")
                val tablesSnapshot = tablesCollection.get().await()
                for (document in tablesSnapshot.documents) {
                    val table = document.toObject(Table::class.java)
                    if (table?.code == tableCode) {
                        onSuccess(table.number)
                        return@launch
                    }
                }
                onSuccess(null)
            } catch (e: Exception) {
                println("Error obteniendo el número de mesa: ${e.message}")
                onFailure()
            }
        }
    }
}
