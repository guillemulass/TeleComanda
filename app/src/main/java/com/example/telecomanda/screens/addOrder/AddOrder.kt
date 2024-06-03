package com.example.telecomanda.screens.addOrder

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.telecomanda.dataClasses.Dish
import com.example.telecomanda.dataClasses.Drink
import com.example.telecomanda.dataClasses.Table
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun AddOrder(
    tableNumber: Int,
    navController: NavHostController,
    addOrderViewModel: AddOrderViewModel
) {
    val auth: FirebaseAuth = Firebase.auth
    var dishList by remember { mutableStateOf(emptyList<Dish>()) }
    var drinkList by remember { mutableStateOf(emptyList<Drink>()) }
    var table by remember { mutableStateOf<Table?>(null) }
    val totalPrice: Double by addOrderViewModel.totalPrice.observeAsState(initial = 0.0)

    LaunchedEffect(tableNumber, auth.currentUser?.uid) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            addOrderViewModel.getDishData(
                onSuccess = { dishList = it },
                onFailure = { println("Failed to load dishes: $it") }
            )
            addOrderViewModel.getDrinkData(
                onSuccess = { drinkList = it },
                onFailure = { println("Failed to load drinks: $it") }
            )
            addOrderViewModel.getTableData(
                tableNumber,
                onSuccess = {
                    table = it
                    addOrderViewModel.totalOrderList.clear()
                    addOrderViewModel.totalOrderList.addAll(it.orders)
                    addOrderViewModel.currentOrderList.clear()
                    addOrderViewModel.updateTotalPrice()
                },
                onFailure = { println("Failed to load table data: $it") }
            )
        }
    }

    table?.let {
        OrderScreen(
            table = it,
            dishList = dishList,
            drinkList = drinkList,
            totalPrice = totalPrice,
            addOrderViewModel = addOrderViewModel,
            navController = navController
        )
    } ?: run {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(text = "Cargando datos de la mesa...")
        }
    }
}

@Composable
fun OrderScreen(
    table: Table,
    dishList: List<Dish>,
    drinkList: List<Drink>,
    totalPrice: Double,
    addOrderViewModel: AddOrderViewModel,
    navController: NavHostController,
) {
    val totalOrderPrice: Double by addOrderViewModel.totalOrderPrice.observeAsState(initial = 0.0)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Mesa ${table.number}",
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 30.sp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navController.popBackStack() }) {
            Text(text = "Volver")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxHeight(0.3f)
        ) {
            items(dishList) { dish ->
                Button(
                    onClick = { addOrderViewModel.addDishToCurrentList(dish) },
                    modifier = Modifier
                ) {
                    Text(text = dish.name)
                }
            }

            items(drinkList) { drink ->
                Button(
                    onClick = { addOrderViewModel.addDrinkToCurrentList(drink) },
                    modifier = Modifier
                ) {
                    Text(text = drink.name)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Current Order")
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxHeight(0.3f)
        ) {
            items(addOrderViewModel.currentOrderList) { orderItem ->
                Text(text = "${orderItem.name} - ${orderItem.price}€ | x${orderItem.quantity}")
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Total: ${totalPrice}€")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Total Order")
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxHeight(0.3f)
        ) {
            items(addOrderViewModel.totalOrderList) { orderItem ->
                Text(text = "${orderItem.name} - ${orderItem.price}€ | x${orderItem.quantity}")
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Total Order Price: ${totalOrderPrice}€")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val combinedOrderList = addOrderViewModel.totalOrderList.toMutableList()
                combinedOrderList.addAll(addOrderViewModel.currentOrderList)
                addOrderViewModel.totalOrderList.clear()
                addOrderViewModel.totalOrderList.addAll(combinedOrderList)
                addOrderViewModel.saveTable(table.number, table.code, combinedOrderList)
                addOrderViewModel.clearCurrentOrderList()
                navController.popBackStack()
            },
            modifier = Modifier
        ) {
            Text(text = "Confirmar Comanda")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                addOrderViewModel.closeTable(table.number)
                navController.popBackStack()
            },
            modifier = Modifier
        ) {
            Text(text = "Cerrar Mesa")
        }
    }
}
