package com.example.telecomanda.screens.addOrder

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.compose.runtime.livedata.observeAsState
import com.example.telecomanda.dataClasses.Dish
import com.example.telecomanda.dataClasses.Drink
import com.example.telecomanda.dataClasses.Order
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun AddOrder(
    navController: NavHostController,
    tableNumber: Int,
    addOrderViewModel: AddOrderViewModel
) {
    val auth: FirebaseAuth = Firebase.auth
    var dishList by remember { mutableStateOf(emptyList<Dish>()) }
    var drinkList by remember { mutableStateOf(emptyList<Drink>()) }
    val totalPrice: Double by addOrderViewModel.totalPrice.observeAsState(initial = 0.0)

    LaunchedEffect(Unit) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            addOrderViewModel.getDishData(
                onSuccess = { dishes ->
                    dishList = dishes
                },
                onFailure = {
                    println(it)
                }
            )
            addOrderViewModel.getDrinkData(
                onSuccess = { drinks ->
                    drinkList = drinks
                },
                onFailure = {
                    println(it)
                }
            )
            addOrderViewModel.getTableData(
                tableNumber,
                onSuccess = { table ->
                    addOrderViewModel.orderList.clear() // Limpiar la comanda actual
                    addOrderViewModel.orderList.addAll(table.orders) // Cargar los datos de la mesa seleccionada
                    addOrderViewModel.updateTotalPrice()
                },
                onFailure = {
                    println(it)
                }
            )
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxSize().padding(top = 35.dp)
        ) {
            item {
                Text(
                    text = "TeleComanda",
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 40.sp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Crear Comanda para Mesa $tableNumber",
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 30.sp)
                )

                Spacer(modifier = Modifier.height(32.dp))
            }

            items(dishList) { dish ->
                Button(
                    onClick = {
                        addOrderViewModel.addDishToList(dish)
                    },
                    modifier = Modifier
                ) {
                    Text(text = dish.name)
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            items(drinkList) { drink ->
                Button(
                    onClick = {
                        addOrderViewModel.addDrinkToList(drink)
                    },
                    modifier = Modifier
                ) {
                    Text(text = drink.name)
                }
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        addOrderViewModel.addOrderToTable(tableNumber, Order(addOrderViewModel.orderList))
                        addOrderViewModel.saveTable(tableNumber, addOrderViewModel.orderList.toList())
                        addOrderViewModel.orderList.clear()
                        addOrderViewModel.resetTotalPrice()
                        navController.popBackStack()
                    },
                    modifier = Modifier
                ) {
                    Text(text = "Confirmar Comanda")
                }
            }

            items(addOrderViewModel.orderList) { orderItem ->
                Text(text = "${orderItem.name} - ${orderItem.price}€ | x${orderItem.quantity}")
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Total: ${totalPrice}€")
            }
        }
    }
}


