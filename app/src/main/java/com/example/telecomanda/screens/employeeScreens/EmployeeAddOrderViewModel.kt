package com.example.telecomanda.screens.employeeScreens

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.telecomanda.dataClasses.Dish
import com.example.telecomanda.dataClasses.Drink
import com.example.telecomanda.dataClasses.Order
import com.example.telecomanda.dataClasses.OrderItem
import com.example.telecomanda.dataClasses.Table
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class EmployeeAddOrderViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    val orderList = mutableStateListOf<OrderItem>()
    val totalPrice = MutableLiveData<Double>()
    val tableList = MutableLiveData<List<Table>>()

    init {
        viewModelScope.launch {
            val quantity = getTableQuantity()
            tableList.value = List(quantity) { Table(number = it + 1) }
        }
    }

    private suspend fun getRestaurantName(): String {
        val email = auth.currentUser?.email ?: return ""
        val document = db.collection("employees").document(email).get().await()
        return document.getString("restaurantName") ?: ""
    }

    private suspend fun getTableQuantity(): Int {
        val restaurantName = getRestaurantName()
        if (restaurantName.isEmpty()) {
            throw IllegalArgumentException("Restaurant name is empty")
        }
        val document = db.collection("restaurants").document(restaurantName).get().await()
        return document.getLong("TableQuantity")?.toInt() ?: 0
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

    fun getDrinkData(onSuccess: (List<Drink>) -> Unit, onFailure: (Exception) -> Unit) {
        viewModelScope.launch {
            try {
                val restaurantName = getRestaurantName()
                if (restaurantName.isNotEmpty()) {
                    val drinksCollectionRef = db.collection("restaurants").document(restaurantName).collection("drinks")
                    val drinks = drinksCollectionRef.get().await()
                    val drinkList = drinks.map { it.toObject(Drink::class.java) }
                    onSuccess(drinkList)
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
                    val dishes = dishesCollectionRef.get().await()
                    val dishList = dishes.map { it.toObject(Dish::class.java) }
                    onSuccess(dishList)
                }
            } catch (e: Exception) {
                onFailure(e)
            }
        }
    }

    fun addDishToList(dish: Dish) {
        val existingItem = orderList.find { it.name == dish.name && it.type == "Dish" }
        if (existingItem != null) {
            existingItem.quantity += 1
        } else {
            orderList.add(OrderItem(dish.name, dish.price, "Dish", 1))
        }
        updateTotalPrice()
    }

    fun addDrinkToList(drink: Drink) {
        val existingItem = orderList.find { it.name == drink.name && it.type == "Drink" }
        if (existingItem != null) {
            existingItem.quantity += 1
        } else {
            orderList.add(OrderItem(drink.name, drink.price, "Drink", 1))
        }
        updateTotalPrice()
    }

    fun updateTotalPrice() {
        totalPrice.value = orderList.sumOf { (it.price.toDoubleOrNull() ?: 0.0) * it.quantity }
    }

    fun resetTotalPrice() {
        totalPrice.value = 0.0
    }

    fun getTableData(tableNumber: Int, onSuccess: (Table) -> Unit, onFailure: (Exception) -> Unit) {
        viewModelScope.launch {
            try {
                val restaurantName = getRestaurantName()
                if (restaurantName.isNotEmpty()) {
                    val document = db.collection("restaurants").document(restaurantName).collection("tables").document(tableNumber.toString()).get().await()
                    val table = document.toObject(Table::class.java)
                    if (table != null) {
                        onSuccess(table)
                    } else {
                        onFailure(Exception("Table data is null"))
                    }
                }
            } catch (e: Exception) {
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
}
