package com.example.telecomanda.screens.clientScreens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.telecomanda.dataClasses.Dish
import com.example.telecomanda.dataClasses.Drink
import com.example.telecomanda.dataClasses.OrderItem
import com.example.telecomanda.dataClasses.Table
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ClientOrderViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _currentOrderList = MutableStateFlow<List<OrderItem>>(emptyList())
    val currentOrderList: StateFlow<List<OrderItem>> = _currentOrderList

    private val _totalOrderList = MutableStateFlow<List<OrderItem>>(emptyList())
    val totalOrderList: StateFlow<List<OrderItem>> = _totalOrderList

    private val _currentOrderPrice = MutableStateFlow(0.0)
    val currentOrderPrice: StateFlow<Double> = _currentOrderPrice

    private val _totalOrderPrice = MutableStateFlow(0.0)
    val totalOrderPrice: StateFlow<Double> = _totalOrderPrice

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private var orderListenerRegistration: ListenerRegistration? = null

    suspend fun getRestaurantState(restaurantName: String): Boolean {
        val document = db.collection("restaurants").document(restaurantName).get().await()
        return document.getBoolean("restaurantOpen") ?: false
    }

    fun getDrinkData(restaurantName: String, onSuccess: (List<Drink>) -> Unit, onFailure: (Exception) -> Unit) {
        viewModelScope.launch {
            try {
                val drinksCollectionRef = db.collection("restaurants").document(restaurantName).collection("drinks")
                val drinks = drinksCollectionRef.get().await()
                val drinkList = drinks.map { it.toObject(Drink::class.java) }
                onSuccess(drinkList)
            } catch (exception: Exception) {
                onFailure(exception)
                _errorMessage.value = "Error al obtener las bebidas: ${exception.message}"
            }
        }
    }

    fun getDishData(restaurantName: String, onSuccess: (List<Dish>) -> Unit, onFailure: (Exception) -> Unit) {
        viewModelScope.launch {
            try {
                val dishesCollectionRef = db.collection("restaurants").document(restaurantName).collection("dishes")
                val dishes = dishesCollectionRef.get().await()
                val dishList = dishes.map { it.toObject(Dish::class.java) }
                onSuccess(dishList)
            } catch (exception: Exception) {
                onFailure(exception)
                _errorMessage.value = "Error al obtener los platos: ${exception.message}"
            }
        }
    }

    fun addDishToCurrentList(dish: Dish) {
        val existingItem = _currentOrderList.value.find { it.name == dish.name && it.type == "Dish" }
        if (existingItem != null) {
            val updatedList = _currentOrderList.value.map {
                if (it.name == dish.name && it.type == "Dish") it.copy(quantity = it.quantity + 1) else it
            }
            _currentOrderList.value = updatedList
        } else {
            _currentOrderList.value = _currentOrderList.value + OrderItem(dish.name, dish.price, "Dish", 1)
        }
        updateCurrentOrderPrice()
    }

    fun addDrinkToCurrentList(drink: Drink) {
        val existingItem = _currentOrderList.value.find { it.name == drink.name && it.type == "Drink" }
        if (existingItem != null) {
            val updatedList = _currentOrderList.value.map {
                if (it.name == drink.name && it.type == "Drink") it.copy(quantity = it.quantity + 1) else it
            }
            _currentOrderList.value = updatedList
        } else {
            _currentOrderList.value = _currentOrderList.value + OrderItem(drink.name, drink.price, "Drink", 1)
        }
        updateCurrentOrderPrice()
    }

    private fun updateCurrentOrderPrice() {
        _currentOrderPrice.value = _currentOrderList.value.sumOf { (it.price.toDoubleOrNull() ?: 0.0) * it.quantity }
    }

    private fun updateTotalOrderPrice() {
        _totalOrderPrice.value = _totalOrderList.value.sumOf { (it.price.toDoubleOrNull() ?: 0.0) * it.quantity }
    }

    fun sendOrderToServer(tableNumber: Int, restaurantName: String, tableCode: String) {
        val currentOrderItems = _currentOrderList.value

        val tableDocument = db.collection("restaurants").document(restaurantName)
            .collection("tables").document(tableNumber.toString())

        viewModelScope.launch {
            try {
                val document = tableDocument.get().await()
                if (document.exists()) {
                    val existingTable = document.toObject(Table::class.java)
                    val updatedOrders = existingTable?.orders?.toMutableList() ?: mutableListOf()
                    currentOrderItems.forEach { newItem ->
                        val existingItem = updatedOrders.find { it.name == newItem.name && it.type == newItem.type }
                        if (existingItem != null) {
                            existingItem.quantity += newItem.quantity
                        } else {
                            updatedOrders.add(newItem)
                        }
                    }
                    tableDocument.update("orders", updatedOrders).await()
                } else {
                    val newTable = Table(number = tableNumber, code = tableCode, orders = currentOrderItems.toMutableList())
                    tableDocument.set(newTable).await()
                }
                clearCurrentOrderList()
            } catch (e: Exception) {
                _errorMessage.value = "Error al enviar el pedido: ${e.message}"
            }
        }
    }

    fun saveCommand(restaurantName: String, tableNumber: String) {
        viewModelScope.launch {
            try {
                val commandString = currentOrderList.value.joinToString(separator = "\n") {
                    "${it.name} - ${it.price}€ | x${it.quantity}"
                }

                db.collection("restaurants")
                    .document(restaurantName)
                    .collection("notificationText")
                    .add(mapOf(
                        "command" to commandString,
                        "tableNumber" to tableNumber
                    ))
                    .await()
            } catch (e: Exception) {
                _errorMessage.value = "Error al guardar la comanda: ${e.message}"
            }
        }
    }

    fun clearCurrentOrderList() {
        _currentOrderList.value = emptyList()
        updateCurrentOrderPrice()
    }

    fun listenToOrderUpdates(tableNumber: Int, restaurantName: String) {
        val tableDocument = db.collection("restaurants").document(restaurantName)
            .collection("tables").document(tableNumber.toString())

        orderListenerRegistration = tableDocument.addSnapshotListener { snapshot, error ->
            if (error != null) {
                _errorMessage.value = "Error al escuchar las actualizaciones del pedido: ${error.message}"
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val table = snapshot.toObject(Table::class.java)
                table?.orders?.let { orders ->
                    _totalOrderList.value = orders
                    updateTotalOrderPrice() // Actualizar el precio total del pedido
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        orderListenerRegistration?.remove()
    }
}
