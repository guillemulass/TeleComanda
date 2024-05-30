package com.example.telecomanda.screens.employeeScreens


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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.telecomanda.dataClasses.Dish
import com.example.telecomanda.dataClasses.Drink
import com.example.telecomanda.dataClasses.Order
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
                    addOrderViewModel.orderList.clear()
                    addOrderViewModel.orderList.addAll(it.orders)
                    addOrderViewModel.updateTotalPrice()
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
            addOrderViewModel = addOrderViewModel,
            navController = navController,
            tableNumber = tableNumber
        )
    }
}

@Composable
fun OrderScreen(
    table: Table,
    dishList: List<Dish>,
    drinkList: List<Drink>,
    totalPrice: Double,
    addOrderViewModel: EmployeeAddOrderViewModel,
    navController: NavHostController,
    tableNumber: Int
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

        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxSize()
        ) {
            items(dishList) { dish ->
                Button(
                    onClick = { addOrderViewModel.addDishToList(dish) },
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
                    onClick = { addOrderViewModel.addDrinkToList(drink) },
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
                        addOrderViewModel.saveTable(table.number, table.code, addOrderViewModel.orderList.toList())
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
