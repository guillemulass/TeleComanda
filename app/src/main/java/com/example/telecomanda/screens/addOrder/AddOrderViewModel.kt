package com.example.telecomanda.screens.addOrder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.example.telecomanda.dataClasses.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.runtime.mutableStateListOf

class AddOrderViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    val orderList = mutableStateListOf<OrderItem>()

    private val _totalPrice = MutableLiveData<Double>()
    val totalPrice: MutableLiveData<Double> = _totalPrice

    val tableList = MutableLiveData<List<Table>>()
    val tableQuantity = MutableLiveData<Int>()

    init {
        fetchTables()
    }

    fun fetchTables() {
        val user = auth.currentUser?.email ?: return
        db.collection("restaurants").document(user).collection("tables").get()
            .addOnSuccessListener { result ->
                val tables = result.mapNotNull { it.toObject(Table::class.java) }
                tableList.value = tables
            }
            .addOnFailureListener { e ->
                // Handle fetch error
            }
    }


    fun getDrinkData(onSuccess: (List<Drink>) -> Unit, onFailure: (Exception) -> Unit) {
        val drinksCollectionRef = db.collection("restaurants").document(auth.currentUser?.email!!).collection("drinks")
        drinksCollectionRef.get()
            .addOnSuccessListener { drinks ->
                val drinkList = mutableListOf<Drink>()
                for (drink in drinks) {
                    val drinkToAdd = drink.toObject(Drink::class.java)
                    drinkList.add(drinkToAdd)
                }
                onSuccess(drinkList)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    fun getDishData(onSuccess: (List<Dish>) -> Unit, onFailure: (Exception) -> Unit) {
        val dishesCollectionRef = db.collection("restaurants").document(auth.currentUser?.email!!).collection("dishes")
        dishesCollectionRef.get()
            .addOnSuccessListener { dishes ->
                val dishList = mutableListOf<Dish>()
                for (dish in dishes) {
                    val dishToAdd = dish.toObject(Dish::class.java)
                    dishList.add(dishToAdd)
                }
                onSuccess(dishList)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
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
        _totalPrice.value = orderList.sumOf { (it.price.toDoubleOrNull() ?: 0.0) * it.quantity }
    }

    fun resetTotalPrice() {
        _totalPrice.value = 0.0
    }


    fun getTableData(tableNumber: Int, onSuccess: (Table) -> Unit, onFailure: (Exception) -> Unit) {
        val user = auth.currentUser?.email ?: return
        db.collection("restaurants").document(user).collection("tables").document(tableNumber.toString()).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val table = document.toObject(Table::class.java)
                    if (table != null) {
                        onSuccess(table)
                    } else {
                        onFailure(Exception("Table data is null"))
                    }
                } else {
                    onFailure(Exception("Table not found"))
                }
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
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

    fun saveTable(number: Int, orders: List<OrderItem>) {
        val tableToSave = Table(number, "", orders.toMutableList(), )
        val restaurantsRef = db.collection("restaurants").document(auth.currentUser?.email!!)
        val tablesCollectionRef = restaurantsRef.collection("tables")
        tablesCollectionRef.document(number.toString()).set(tableToSave)
    }


}