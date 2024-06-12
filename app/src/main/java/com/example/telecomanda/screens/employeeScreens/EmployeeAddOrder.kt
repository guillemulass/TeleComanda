package com.example.telecomanda.screens.employeeScreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.telecomanda.dataClasses.Dish
import com.example.telecomanda.dataClasses.Drink
import com.example.telecomanda.dataClasses.Table
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun EmployeeAddOrder(
    tableNumber: Int,
    navController: NavHostController
) {
    val addOrderViewModel: EmployeeAddOrderViewModel = viewModel()

    val auth: FirebaseAuth = Firebase.auth
    var dishList by remember { mutableStateOf(emptyList<Dish>()) }
    var drinkList by remember { mutableStateOf(emptyList<Drink>()) }
    var table by remember { mutableStateOf<Table?>(null) }
    val totalPrice: Double by addOrderViewModel.totalPrice.observeAsState(initial = 0.0)
    val totalOrderPrice: Double by addOrderViewModel.totalOrderPrice.observeAsState(initial = 0.0)

    LaunchedEffect(tableNumber, auth.currentUser?.uid) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            addOrderViewModel.getDishData(
                onSuccess = { dishes -> dishList = dishes },
                onFailure = { println(it) }
            )
            addOrderViewModel.getDrinkData(
                onSuccess = { drinks -> drinkList = drinks },
                onFailure = { println(it) }
            )
            addOrderViewModel.getTableData(
                tableNumber,
                onSuccess = {
                    table = it
                    addOrderViewModel.totalOrderList.clear()
                    addOrderViewModel.totalOrderList.addAll(it.orders)
                    addOrderViewModel.updateTotalPrice()
                    addOrderViewModel.updateTotalOrderPrice()
                },
                onFailure = { println(it) }
            )
        }
    }

    table?.let {
        OrderScreen(
            table = it,
            dishList = dishList,
            drinkList = drinkList,
            totalPrice = totalPrice,
            totalOrderPrice = totalOrderPrice,
            addOrderViewModel = addOrderViewModel,
            navController = navController
        )
    }
}

@Composable
fun OrderScreen(
    table: Table,
    dishList: List<Dish>,
    drinkList: List<Drink>,
    totalPrice: Double,
    totalOrderPrice: Double,
    addOrderViewModel: EmployeeAddOrderViewModel,
    navController: NavHostController,
) {
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

        // Display available dishes and drinks
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

        // Display current order
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

        // Display total order
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
                addOrderViewModel.addCurrentOrderToTotalOrder()
                addOrderViewModel.saveTable(table.number, table.code, addOrderViewModel.totalOrderList.toList())
                addOrderViewModel.clearCurrentOrderList()
                navController.popBackStack()
            },
            modifier = Modifier
        ) {
            Text(text = "Confirmar Comanda")
        }
    }
}
