package com.example.telecomanda.screens.addOrder

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.telecomanda.dataClasses.Dish
import com.example.telecomanda.dataClasses.Drink
import com.example.telecomanda.dataClasses.OrderItem
import com.example.telecomanda.dataClasses.Table
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AddOrderViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    val currentOrderList = mutableStateListOf<OrderItem>()
    val totalOrderList = mutableStateListOf<OrderItem>()
    val tableList = MutableLiveData<List<Table>>()

    private val _totalPrice = MutableLiveData<Double>()
    val totalPrice: MutableLiveData<Double> = _totalPrice

    private val _totalOrderPrice = MutableLiveData<Double>()
    val totalOrderPrice: MutableLiveData<Double> = _totalOrderPrice

    init {
        fetchTables()
    }

    private suspend fun getRestaurantName(): String {
        val email = auth.currentUser?.email ?: return ""
        val document = db.collection("restaurantsEmail").document(email).get().await()
        return document.getString("restaurantName") ?: ""
    }

    fun getDrinkData(onSuccess: (List<Drink>) -> Unit, onFailure: (Exception) -> Unit) {
        viewModelScope.launch {
            try {
                val restaurantName = getRestaurantName()
                if (restaurantName.isNotEmpty()) {
                    val drinksCollectionRef = db.collection("restaurants").document(restaurantName).collection("drinks")
                    val drinksSnapshot = drinksCollectionRef.get().await()
                    val drinkList = drinksSnapshot.mapNotNull { it.toObject(Drink::class.java) }
                    onSuccess(drinkList)
                } else {
                    onFailure(Exception("Restaurant name is empty"))
                }
            } catch (e: Exception) {
                onFailure(e)
            }
        }
    }

    fun getDishData(onSuccess: (List<Dish>) -> Unit, onFailure: (Exception) -> Unit) {
        viewModelScope.launch {
            try {
                val restaurantName = getRestaurantName()
                if (restaurantName.isNotEmpty()) {
                    val dishesCollectionRef = db.collection("restaurants").document(restaurantName).collection("dishes")
                    val dishesSnapshot = dishesCollectionRef.get().await()
                    val dishList = dishesSnapshot.mapNotNull { it.toObject(Dish::class.java) }
                    onSuccess(dishList)
                } else {
                    onFailure(Exception("Restaurant name is empty"))
                }
            } catch (e: Exception) {
                onFailure(e)
            }
        }
    }

    fun addDishToCurrentList(dish: Dish) {
        val existingItem = currentOrderList.find { it.name == dish.name && it.type == "Dish" }
        if (existingItem != null) {
            existingItem.quantity += 1
        } else {
            currentOrderList.add(OrderItem(dish.name, dish.price, "Dish", 1))
        }
        updateTotalPrice()
    }

    fun addDrinkToCurrentList(drink: Drink) {
        val existingItem = currentOrderList.find { it.name == drink.name && it.type == "Drink" }
        if (existingItem != null) {
            existingItem.quantity += 1
        } else {
            currentOrderList.add(OrderItem(drink.name, drink.price, "Drink", 1))
        }
        updateTotalPrice()
    }

    fun combineOrderLists(currentList: List<OrderItem>, totalList: List<OrderItem>): List<OrderItem> {
        val combinedList = mutableListOf<OrderItem>()
        val itemMap = mutableMapOf<String, OrderItem>()

        // Agrega los elementos del totalList al mapa
        totalList.forEach { item ->
            val key = "${item.name}-${item.type}"
            itemMap[key] = item
        }

        // Agrega o combina los elementos del currentList
        currentList.forEach { item ->
            val key = "${item.name}-${item.type}"
            val existingItem = itemMap[key]
            if (existingItem != null) {
                existingItem.quantity += item.quantity
            } else {
                itemMap[key] = item
            }
        }

        combinedList.addAll(itemMap.values)
        return combinedList
    }


    fun updateTotalPrice() {
        _totalPrice.value = currentOrderList.sumOf { (it.price.toDoubleOrNull() ?: 0.0) * it.quantity }
    }

    private fun updateTotalOrderPrice() {
        _totalOrderPrice.value = totalOrderList.sumOf { (it.price.toDoubleOrNull() ?: 0.0) * it.quantity }
    }

    fun clearCurrentOrderList() {
        currentOrderList.clear()
        updateTotalPrice()
    }

    fun getTableData(tableNumber: Int, onSuccess: (Table) -> Unit, onFailure: (Exception) -> Unit) {
        viewModelScope.launch {
            try {
                val restaurantName = getRestaurantName()
                if (restaurantName.isNotEmpty()) {
                    val documentReference = db.collection("restaurants").document(restaurantName).collection("tables").document(tableNumber.toString())
                    val documentSnapshot = documentReference.get().await()
                    if (documentSnapshot.exists()) {
                        val table = documentSnapshot.toObject(Table::class.java)
                        if (table != null) {
                            totalOrderList.clear()
                            totalOrderList.addAll(table.orders)
                            updateTotalOrderPrice() // Actualizar el precio total del pedido
                            onSuccess(table)
                        } else {
                            onFailure(Exception("Table data is null"))
                        }
                    } else {
                        onFailure(Exception("Table not found"))
                    }
                } else {
                    onFailure(Exception("Restaurant name is empty"))
                }
            } catch (e: Exception) {
                onFailure(e)
            }
        }
    }

    fun saveTable(number: Int, code: String, orders: List<OrderItem>) {
        viewModelScope.launch {
            val restaurantName = getRestaurantName()
            if (restaurantName.isNotEmpty()) {
                val tableToSave = Table(number, code, orders.toMutableList())
                db.collection("restaurants").document(restaurantName).collection("tables").document(number.toString()).set(tableToSave).await()
            }
        }
    }

    fun fetchTables() {
        viewModelScope.launch {
            val restaurantName = getRestaurantName()
            if (restaurantName.isNotEmpty()) {
                val result = db.collection("restaurants").document(restaurantName).collection("tables").get().await()
                val tables = result.mapNotNull { it.toObject(Table::class.java) }
                tableList.value = tables
            }
        }
    }

    fun closeTable(tableNumber: Int) {
        viewModelScope.launch {
            try {
                val restaurantName = getRestaurantName()
                if (restaurantName.isNotEmpty()) {
                    // Obtener datos de la mesa
                    val tableRef = db.collection("restaurants").document(restaurantName).collection("tables").document(tableNumber.toString())
                    val tableSnapshot = tableRef.get().await()
                    val table = tableSnapshot.toObject(Table::class.java)

                    if (table != null) {
                        // Sumar total de la mesa a todaysTotal
                        val todaysTotalRef = db.collection("restaurants").document(restaurantName)
                        val todaysTotalSnapshot = todaysTotalRef.get().await()
                        var todaysTotal = todaysTotalSnapshot.getDouble("todaysTotal") ?: 0.0

                        val tableTotal = table.orders.sumOf { (it.price.toDoubleOrNull() ?: 0.0) * it.quantity }
                        todaysTotal += tableTotal

                        todaysTotalRef.update("todaysTotal", todaysTotal).await()

                        // Vaciar comanda de la mesa
                        tableRef.update("orders", emptyList<OrderItem>()).await()

                        // Actualizar vista
                        totalOrderList.clear()
                        updateTotalOrderPrice()
                    }
                }
            } catch (e: Exception) {
                println("Error al cerrar la mesa: $e")
            }
        }
    }
}