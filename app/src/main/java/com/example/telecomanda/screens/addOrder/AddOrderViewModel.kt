package com.example.telecomanda.screens.addOrder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.example.telecomanda.dataClasses.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AddOrderViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    val currentOrderList = mutableStateListOf<OrderItem>()
    val totalOrderList = mutableStateListOf<OrderItem>()
    private val _totalPrice = MutableLiveData<Double>()
    val totalPrice: MutableLiveData<Double> = _totalPrice
    val tableList = MutableLiveData<List<Table>>()

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

    fun updateTotalPrice() {
        _totalPrice.value = currentOrderList.sumOf { (it.price.toDoubleOrNull() ?: 0.0) * it.quantity }
    }

    fun resetTotalPrice() {
        _totalPrice.value = 0.0
    }

    fun clearCurrentOrderList() {
        currentOrderList.clear()
        updateTotalPrice()
    }

    fun getTableData(tableNumber: Int, onSuccess: (Table) -> Unit, onFailure: (Exception) -> Unit) {
        viewModelScope.launch {
            try {
                val restaurantName = getRestaurantName()
                println("Restaurant name: $restaurantName")
                if (restaurantName.isNotEmpty()) {
                    val documentReference = db.collection("restaurants").document(restaurantName).collection("tables").document(tableNumber.toString())
                    val documentSnapshot = documentReference.get().await()
                    if (documentSnapshot.exists()) {
                        val table = documentSnapshot.toObject(Table::class.java)
                        if (table != null) {
                            println("Table data: $table")
                            onSuccess(table)
                        } else {
                            val exception = Exception("Table data is null")
                            println(exception.message)
                            onFailure(exception)
                        }
                    } else {
                        val exception = Exception("Table not found")
                        println(exception.message)
                        onFailure(exception)
                    }
                } else {
                    val exception = Exception("Restaurant name is empty")
                    println(exception.message)
                    onFailure(exception)
                }
            } catch (e: Exception) {
                println("Error fetching table data: ${e.message}")
                onFailure(e)
            }
        }
    }

    fun addOrderToTable(tableNumber: Int, order: Order) {
        tableList.value?.let { tables ->
            val updatedTables = tables.map { table ->
                if (table.number == tableNumber) {
                    val updatedOrders = table.orders.toMutableList()
                    order.items.forEach { newItem ->
                        val existingItem = updatedOrders.find { it.name == newItem.name && it.type == newItem.type }
                        if (existingItem != null) {
                            existingItem.quantity += newItem.quantity
                        } else {
                            updatedOrders.add(newItem)
                        }
                    }
                    table.copy(orders = updatedOrders)
                } else {
                    table
                }
            }
            tableList.value = updatedTables
        }
        totalOrderList.addAll(order.items)
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

    private fun getTableQuantity(onQuantityReceived: (Int) -> Unit) {
        val user = auth.currentUser?.email ?: return
        db.collection("restaurants").document(user).get().addOnSuccessListener {
            val tableQuantity = it.get("TableQuantity").toString()
            val quantity = tableQuantity.toIntOrNull() ?: 0
            onQuantityReceived(quantity)
        }
    }

    fun fetchTables() {
        viewModelScope.launch {
            val user = auth.currentUser?.email ?: return@launch
            val restaurantName = getRestaurantName()
            if (restaurantName.isNotEmpty()) {
                val result = db.collection("restaurants").document(restaurantName).collection("tables").get().await()
                val tables = result.mapNotNull { it.toObject(Table::class.java) }
                tableList.value = tables
            }
        }
    }
}
