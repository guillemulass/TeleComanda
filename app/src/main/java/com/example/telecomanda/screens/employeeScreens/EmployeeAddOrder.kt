package com.example.telecomanda.screens.employeeScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.telecomanda.botonbig24sp.BotonBig24sp
import com.example.telecomanda.buttonsmallmenu.ButtonSmallMenu
import com.example.telecomanda.dataClasses.Dish
import com.example.telecomanda.dataClasses.Drink
import com.example.telecomanda.dataClasses.Table
import com.example.telecomanda.footer.Footer
import com.example.telecomanda.header.Header
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
            Text(
                text = "Cargando datos de la mesa...",
                style = TextStyle(color = Color.White, fontSize = 32.sp)
            )
        }
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
) {
    val totalOrderPrice: Double by addOrderViewModel.totalOrderPrice.observeAsState(initial = 0.0)
    var showDishes by remember { mutableStateOf(true) } // Estado para controlar la vista actual

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF161618))
            .padding(top = 35.dp)
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Header(
            modifier = Modifier
                .width(430.dp)
                .height(60.dp)
                .background(Color(0xFF161618)),
            onClick = { navController.popBackStack() }
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 60.dp)
        ) {
            Text(
                text = "Mesa ${table.number}",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 32.sp
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.fillMaxSize()
            ) {

                item {
                    LazyRow(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        item {
                            ButtonSmallMenu(
                                onClick = { showDishes = true },
                                text = "Platos",
                                textColor = if (showDishes) Color.White else Color.Gray
                            )
                        }
                        item { Spacer(modifier = Modifier.width(8.dp)) }

                        item {
                            ButtonSmallMenu(
                                onClick = { showDishes = false },
                                text = "Bebidas",
                                textColor = if (showDishes) Color.Gray else Color.White
                            )
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(24.dp)) }

                if (showDishes) {
                    items(dishList) { dish ->
                        Spacer(modifier = Modifier.height(8.dp))
                        BotonBig24sp(
                            onClick = { addOrderViewModel.addDishToCurrentList(dish) },
                            text = dish.name,
                            modifier = Modifier.width(266.dp)
                        )
                    }
                } else {
                    items(drinkList) { drink ->
                        Spacer(modifier = Modifier.height(8.dp))
                        BotonBig24sp(
                            onClick = { addOrderViewModel.addDrinkToCurrentList(drink) },
                            text = drink.name,
                            modifier = Modifier.width(266.dp)
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Comanda Actual",
                        style = TextStyle(color = Color.White, fontSize = 32.sp)
                    )

                    addOrderViewModel.currentOrderList.forEach { orderItem ->
                        Text(
                            text = "${orderItem.name} - ${orderItem.price}€ | x${orderItem.quantity}",
                            style = TextStyle(color = Color.White, fontSize = 24.sp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Total: ${totalPrice}€",
                        style = TextStyle(color = Color.White, fontSize = 24.sp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Total Order",
                        style = TextStyle(color = Color.White, fontSize = 32.sp)
                    )

                    addOrderViewModel.totalOrderList.forEach { orderItem ->
                        Text(
                            text = "${orderItem.name} - ${orderItem.price}€ | x${orderItem.quantity}",
                            style = TextStyle(color = Color.White, fontSize = 24.sp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Total Order Price: ${totalOrderPrice}€",
                        style = TextStyle(color = Color.White, fontSize = 24.sp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    BotonBig24sp(
                        onClick = {
                            addOrderViewModel.clearCurrentOrderList()
                        },
                        text = "Limpiar Comanda",
                        modifier = Modifier.width(266.dp).height(52.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    BotonBig24sp(
                        onClick = {
                            val combinedOrderList = addOrderViewModel.totalOrderList.toMutableList()
                            combinedOrderList.addAll(addOrderViewModel.currentOrderList)
                            addOrderViewModel.totalOrderList.clear()
                            addOrderViewModel.totalOrderList.addAll(combinedOrderList)
                            addOrderViewModel.saveTable(table.number, table.code, combinedOrderList)
                            addOrderViewModel.clearCurrentOrderList()
                            navController.popBackStack()
                        },
                        text = "Confirmar Comanda",
                        modifier = Modifier.width(266.dp).height(52.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    BotonBig24sp(
                        onClick = {
                            addOrderViewModel.closeTable(table.number)
                            navController.popBackStack()
                        },
                        text = "Cerrar Mesa",
                        modifier = Modifier.width(266.dp).height(52.dp)
                    )

                    Spacer(modifier = Modifier.height(75.dp))

                }
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier.fillMaxSize()
        ) {
            Footer(
                modifier = Modifier
                    .width(430.dp)
                    .height(54.dp)
                    .background(Color(0xFF161618))
            )
            Spacer(
                modifier = Modifier
                    .height(16.dp)
                    .fillMaxWidth()
                    .background(Color(0xFF161618))
            )
        }
    }
}
