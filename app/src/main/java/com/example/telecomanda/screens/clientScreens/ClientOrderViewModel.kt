package com.example.telecomanda.screens.clientScreens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.telecomanda.dataClasses.Dish
import com.example.telecomanda.dataClasses.Drink
import com.example.telecomanda.dataClasses.OrderItem
import com.example.telecomanda.dataClasses.Table
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ClientOrderViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _orderList = MutableStateFlow<List<OrderItem>>(emptyList())
    val orderList: StateFlow<List<OrderItem>> = _orderList

    private val _totalPrice = MutableStateFlow(0.0)
    val totalPrice: StateFlow<Double> = _totalPrice

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun getDrinkData(restaurantEmail: String, onSuccess: (List<Drink>) -> Unit, onFailure: (Exception) -> Unit) {
        val drinksCollectionRef = db.collection("restaurants").document(restaurantEmail).collection("drinks")
        drinksCollectionRef.get()
            .addOnSuccessListener { drinks ->
                val drinkList = drinks.map { it.toObject(Drink::class.java) }
                onSuccess(drinkList)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
                _errorMessage.value = "Error al obtener las bebidas: ${exception.message}"
            }
    }

    fun getDishData(restaurantEmail: String, onSuccess: (List<Dish>) -> Unit, onFailure: (Exception) -> Unit) {
        val dishesCollectionRef = db.collection("restaurants").document(restaurantEmail).collection("dishes")
        dishesCollectionRef.get()
            .addOnSuccessListener { dishes ->
                val dishList = dishes.map { it.toObject(Dish::class.java) }
                onSuccess(dishList)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
                _errorMessage.value = "Error al obtener los platos: ${exception.message}"
            }
    }

    fun addDishToList(dish: Dish) {
        val existingItem = _orderList.value.find { it.name == dish.name && it.type == "Dish" }
        if (existingItem != null) {
            val updatedList = _orderList.value.map {
                if (it.name == dish.name && it.type == "Dish") it.copy(quantity = it.quantity + 1) else it
            }
            _orderList.value = updatedList
        } else {
            _orderList.value = _orderList.value + OrderItem(dish.name, dish.price, "Dish", 1)
        }
        updateTotalPrice()
    }

    fun addDrinkToList(drink: Drink) {
        val existingItem = _orderList.value.find { it.name == drink.name && it.type == "Drink" }
        if (existingItem != null) {
            val updatedList = _orderList.value.map {
                if (it.name == drink.name && it.type == "Drink") it.copy(quantity = it.quantity + 1) else it
            }
            _orderList.value = updatedList
        } else {
            _orderList.value = _orderList.value + OrderItem(drink.name, drink.price, "Drink", 1)
        }
        updateTotalPrice()
    }

    private fun updateTotalPrice() {
        _totalPrice.value = _orderList.value.sumOf { (it.price.toDoubleOrNull() ?: 0.0) * it.quantity }
    }
    
    fun sendOrderToServer(tableNumber: Int, restaurantEmail: String, tableCode: String) {
        val orderItems = _orderList.value
        println("zulepo")
        println("tableNumber $tableNumber")
        println("tableCode $tableCode")
        println("restaurantEmail $restaurantEmail")

        val tableDocument = db.collection("restaurants").document(restaurantEmail)
            .collection("tables").document(tableNumber.toString())

        tableDocument.get().addOnSuccessListener { document ->
            if (document.exists()) {
                val existingTable = document.toObject(Table::class.java)
                val updatedOrders = existingTable?.orders?.toMutableList() ?: mutableListOf()
                orderItems.forEach { newItem ->
                    val existingItem = updatedOrders.find { it.name == newItem.name && it.type == newItem.type }
                    if (existingItem != null) {
                        existingItem.quantity += newItem.quantity
                    } else {
                        updatedOrders.add(newItem)
                    }
                }
                tableDocument.update("orders", updatedOrders)
            } else {
                val newTable = Table(number = tableNumber, code = tableCode, orders = orderItems.toMutableList())
                tableDocument.set(newTable)
            }
            clearOrderList()
        }.addOnFailureListener { e ->
            _errorMessage.value = "Error al enviar el pedido: ${e.message}"
        }
    }


    private fun clearOrderList() {
        _orderList.value = emptyList()
        updateTotalPrice()
    }

    fun getRestaurantEmailByName(
        restaurantName: String,
        onSuccess: (String?) -> Unit,
        onFailure: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                val document = db.collection("restaurantsInfoList").document(restaurantName).get().await()
                if (document.exists()) {
                    val email = document.getString("restaurantEmail")
                    onSuccess(email)
                } else {
                    onSuccess(null)
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error obteniendo el email del restaurante: ${e.message}"
                onFailure()
            }
        }
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
                _errorMessage.value = "Error obteniendo el número de mesa: ${e.message}"
                onFailure()
            }
        }
    }
}
