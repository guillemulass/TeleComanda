package com.example.telecomanda.screens.addOrder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.example.telecomanda.dataClasses.Dish
import com.example.telecomanda.dataClasses.Drink
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.telecomanda.dataClasses.OrderItem
import androidx.compose.runtime.mutableStateListOf
import com.example.telecomanda.dataClasses.Order
import com.example.telecomanda.dataClasses.Table

class AddOrderViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    val orderList = mutableStateListOf<OrderItem>()

    private val _totalPrice = MutableLiveData<Double>()
    var totalPrice : MutableLiveData<Double> = _totalPrice

    val tableList = MutableLiveData<List<Table>>()

    init {
        // Inicializar lista de mesas (por ejemplo, 10 mesas)
        tableList.value = List(10) { Table(number = it + 1) }
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
        orderList.add(OrderItem(dish.name, dish.price, "Dish"))
        updateTotalPrice()
    }

    fun addDrinkToList(drink: Drink) {
        orderList.add(OrderItem(drink.name, drink.price, "Drink"))
        updateTotalPrice()
    }

    private fun updateTotalPrice() {
        totalPrice.value = orderList.sumOf { it.price.toDoubleOrNull() ?: 0.0 }
    }


    fun addOrderToTable(tableNumber: Int, order: Order) {
        tableList.value?.let { tables ->
            val updatedTables = tables.map { table ->
                if (table.number == tableNumber) {
                    table.copy(orders = (table.orders + order).toMutableList())
                } else {
                    table
                }
            }
            tableList.value = updatedTables
        }
    }

}
