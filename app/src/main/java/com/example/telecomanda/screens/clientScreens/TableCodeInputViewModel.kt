package com.example.telecomanda.screens.clientScreens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.telecomanda.dataClasses.Table
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class TableCodeInputViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val firestore = Firebase.firestore

    private val _isValidTable = MutableLiveData<Boolean>()
    val isValidTable: LiveData<Boolean> = _isValidTable


    var tableCode by mutableStateOf("")
        private set

    var restaurantName by mutableStateOf("")
        private set

    var restaurantEmail_ by mutableStateOf("")
        private set


    private val _tableNumber = MutableLiveData<Int?>()
    val tableNumber: LiveData<Int?> = _tableNumber

    fun checkTableCode(onSuccess: () -> Unit, onFailure: () -> Unit) {
        // Referencia al documento del restaurante en Firestore
        val restaurantRef = db.collection("restaurants").document(restaurantEmail_)
        // Referencia a la colección "tables" dentro del documento del restaurante, filtrando por "code"
        val tableRef = restaurantRef.collection("tables").whereEqualTo("code", tableCode)

        // Ejecuta la consulta
        tableRef.get()
            .addOnSuccessListener { documents ->
                // Si se encontraron documentos, significa que el código de la mesa es válido
                if (!documents.isEmpty) {
                    _isValidTable.value = true
                    onSuccess()  // Llama a la función de éxito
                } else {
                    _isValidTable.value = false
                    onFailure()  // Llama a la función de fallo
                }
            }
            .addOnFailureListener {
                // En caso de fallo en la consulta, también establece la mesa como no válida
                _isValidTable.value = false
                onFailure()  // Llama a la función de fallo
            }
    }




    fun updateTableCode(code: String) {
        tableCode = code
    }

    fun updateRestaurantName(name: String) {
        restaurantName = name
    }

    fun updateRestaurantEmail(email: String) {
        restaurantEmail_ = email
    }

    fun getTableNumberByCode(
        restaurantEmail: String,
        tableCode: String,
        onSuccess: (Int?) -> Unit,
        onFailure: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                val tablesCollection = db.collection("restaurants").document(restaurantEmail).collection("tables")
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

    fun getTableNumberByCodex(
        restaurantEmail: String,
        tableCode: String,
    ) {
        viewModelScope.launch {
            try {
                val tablesCollection =
                    db.collection("restaurants").document(restaurantEmail).collection("tables")
                val tablesSnapshot = tablesCollection.get().await()
                for (document in tablesSnapshot.documents) {
                    val table = document.toObject(Table::class.java)
                    if (table?.code == tableCode) {
                        _tableNumber.postValue(table.number) // Actualiza LiveData
                        return@launch
                    }
                }
                _tableNumber.postValue(null) // No se encontró la mesa
            } catch (e: Exception) {
                println("Error obteniendo el número de mesa: ${e.message}")
            }
        }
    }

    suspend fun getTableNumberByCodeSuspend(
        restaurantEmail: String,
        tableCode: String
    ): Int? {
        return try {
            val tablesCollection = db.collection("restaurants").document(restaurantEmail).collection("tables")
            val tablesSnapshot = tablesCollection.get().await()
            for (document in tablesSnapshot.documents) {
                val table = document.toObject(Table::class.java)
                if (table?.code == tableCode) {
                    _tableNumber.value = table.number
                    return table.number
                }
            }
            null
        } catch (e: Exception) {
            println("Error obteniendo el número de mesa: ${e.message}")
            null
        }
    }

    fun getTableNumberByCodexx(
        restaurantEmail: String,
        tableCode: String,
        onSuccess: (Int?) -> Unit,
        onFailure: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                val tablesCollection = db.collection("restaurants").document(restaurantEmail).collection("tables")
                tablesCollection.get()
                    .addOnSuccessListener { tablesSnapshot ->
                        for (document in tablesSnapshot.documents) {
                            val table = document.toObject(Table::class.java)
                            if (table?.code == tableCode) {
                                onSuccess(table.number)
                                return@addOnSuccessListener
                            }
                        }
                        onSuccess(null) // No se encontró la mesa
                    }
                    .addOnFailureListener { e ->
                        println("Error obteniendo el número de mesa: ${e.message}")
                        onFailure()
                    }
            } catch (e: Exception) {
                println("Error obteniendo el número de mesa: ${e.message}")
                onFailure()
            }
        }
    }

    fun getRestaurantEmailByName(
        restaurantName: String,
        onSuccess: (String?) -> Unit,
        onFailure: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                val document =
                    firestore.collection("restaurantsInfoList").document(restaurantName).get()
                        .await()
                if (document.exists()) {
                    val email = document.getString("restaurantEmail")
                    updateRestaurantEmail(email!!)
                    onSuccess(email)
                } else {
                    onSuccess(null)
                }
            } catch (e: Exception) {
                println("Error obteniendo el email del restaurante: ${e.message}")
                onFailure()
            }
        }
    }


    fun getTableNumberr(restaurantEmail: String, tableCode: String, callback: (String?) -> Unit) {
        val database = FirebaseDatabase.getInstance()
        val tablesRef = database.getReference("/restaurants/$restaurantEmail/tables")

        tablesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (tableSnapshot in snapshot.children) {
                    val code = tableSnapshot.child("codigo").getValue(String::class.java)
                    if (code == tableCode) {
                        val tableNumber = tableSnapshot.key
                        callback(tableNumber)
                        return
                    }
                }
                callback(null) // Table not found
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors
                callback(null)
            }
        })
    }
}
