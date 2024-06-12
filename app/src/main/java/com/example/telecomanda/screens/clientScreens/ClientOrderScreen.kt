package com.example.telecomanda.screens.clientScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.telecomanda.botonbig24sp.BotonBig24sp
import com.example.telecomanda.buttonsmallmenu.ButtonSmallMenu
import com.example.telecomanda.dataClasses.Dish
import com.example.telecomanda.dataClasses.Drink
import com.example.telecomanda.footer.Footer
import com.example.telecomanda.header.Header

@Composable
fun ClientOrderScreen(
    navController: NavHostController,
    tableNumber: String,
    restaurantName: String,
    tableCode: String
) {
    val clientOrderViewModel: ClientOrderViewModel = viewModel()
    var drinks by remember { mutableStateOf(listOf<Drink>()) }
    var dishes by remember { mutableStateOf(listOf<Dish>()) }
    var showDrinks by remember { mutableStateOf(true) }
    val currentOrderList by clientOrderViewModel.currentOrderList.collectAsState()
    val totalOrderList by clientOrderViewModel.totalOrderList.collectAsState()
    val currentOrderPrice by clientOrderViewModel.currentOrderPrice.collectAsState()
    val totalOrderPrice by clientOrderViewModel.totalOrderPrice.collectAsState()
    val errorMessage by clientOrderViewModel.errorMessage.collectAsState()

    LaunchedEffect(restaurantName) {
        clientOrderViewModel.getDrinkData(
            restaurantName,
            onSuccess = { fetchedDrinks ->
                drinks = fetchedDrinks
            },
            onFailure = {}
        )
        clientOrderViewModel.getDishData(
            restaurantName,
            onSuccess = { fetchedDishes ->
                dishes = fetchedDishes
            },
            onFailure = {}
        )
        clientOrderViewModel.listenToOrderUpdates(tableNumber.toInt(), restaurantName)
    }

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
                .background(Color(0xFF161618))
            ,
            onClick = { navController.popBackStack() }
        )

        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 60.dp)
        ) {

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }

            item {
                Text(
                    text = "Mesa: $tableNumber",
                    style = TextStyle(color = Color.White, fontSize = 32.sp)
                )

                Text(
                    text = restaurantName,
                    style = TextStyle(color = Color.White, fontSize = 32.sp)
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                BotonBig24sp(
                    onClick = {
                        navController.navigate("client_menu_screen/$restaurantName")
                    },
                    text = "Ver Carta",
                    modifier = Modifier
                        .width(150.dp)
                        .height(52.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                errorMessage?.let {
                    Text(
                        text = "Error: $it",
                        style = TextStyle(color = Color.Red, fontSize = 24.sp),
                    )
                }
            }

            item {
                LazyRow(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        ButtonSmallMenu(
                            onClick = { showDrinks = true },
                            text = "Bebidas",
                            textColor = if (showDrinks) Color.White else Color.Gray
                        )
                    }

                    item { Spacer(modifier = Modifier.width(16.dp)) }

                    item {
                        ButtonSmallMenu(
                            onClick = { showDrinks = false },
                            text = "Platos",
                            textColor = if (showDrinks) Color.Gray else Color.White
                        )
                    }
                }
            }

            if (showDrinks && drinks.isNotEmpty()) {
                items(drinks) { drink ->
                    Spacer(modifier = Modifier.height(8.dp))
                    BotonBig24sp(
                        onClick = { clientOrderViewModel.addDrinkToCurrentList(drink) },
                        text = drink.name,
                        modifier = Modifier.width(266.dp)
                    )
                }
            } else if (!showDrinks && dishes.isNotEmpty()) {
                items(dishes) { dish ->
                    Spacer(modifier = Modifier.height(8.dp))
                    BotonBig24sp(
                        onClick = { clientOrderViewModel.addDishToCurrentList(dish) },
                        text = dish.name,
                        modifier = Modifier.width(266.dp)
                    )
                }
            } else {
                item {
                    Text(
                        text = "No ${if (showDrinks) "bebidas" else "platos"} disponibles",
                        style = TextStyle(color = Color.Red, fontSize = 24.sp),
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                Text(text = "Comanda Actual",
                    style = TextStyle(color = Color.White, fontSize = 24.sp),
                )
            }

            items(currentOrderList) { orderItem ->
                Text(text = "${orderItem.name} - ${orderItem.price}€ | x${orderItem.quantity}",
                    style = TextStyle(color = Color.White, fontSize = 24.sp),
                )
            }

            item {
                Text(text = "Total: ${currentOrderPrice}€",
                    style = TextStyle(color = Color.White, fontSize = 24.sp),
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                Text(text = "Comanda Completa",
                    style = TextStyle(color = Color.White, fontSize = 24.sp),
                )
            }

            items(totalOrderList) { orderItem ->
                Text(
                    text = "${orderItem.name} - ${orderItem.price}€ | x${orderItem.quantity}",
                    style = TextStyle(color = Color.White, fontSize = 24.sp),
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Total: ${totalOrderPrice}€",
                    style = TextStyle(color = Color.White, fontSize = 24.sp),
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                BotonBig24sp(
                    onClick = { clientOrderViewModel.clearCurrentOrderList() },
                    text = "Vaciar Comanda Actual",
                    modifier = Modifier
                        .width(266.dp)
                        .height(80.dp)
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                BotonBig24sp(
                    onClick = {
                        clientOrderViewModel.sendOrderToServer(
                            tableNumber.toInt(),
                            restaurantName,
                            tableCode
                        )
                    },
                    text = "Confirmar Comanda",
                    modifier = Modifier
                        .width(266.dp)
                        .height(52.dp)
                )
            }



            item {
                Spacer(modifier = Modifier.height(75.dp))
            }

        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier
                .fillMaxSize()

        ) {
            Footer(
                modifier = Modifier
                    .width(430.dp)
                    .height(54.dp)
                    .background(Color(0xFF161618))
            )
            Spacer(modifier = Modifier.height(16.dp).fillMaxWidth().background(Color(0xFF161618)))
        }
    }
}
