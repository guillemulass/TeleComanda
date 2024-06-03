package com.example.telecomanda.screens.clientScreens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.telecomanda.dataClasses.Dish
import com.example.telecomanda.dataClasses.Drink

@Composable
fun ClientOrderScreen(
    tableNumber: String,
    restaurantName: String,
    tableCode: String
) {
    val clientOrderViewModel: ClientOrderViewModel = viewModel()
    var drinks by remember { mutableStateOf(listOf<Drink>()) }
    var dishes by remember { mutableStateOf(listOf<Dish>()) }
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
            onFailure = {
                // Handle error
            }
        )
        clientOrderViewModel.getDishData(
            restaurantName,
            onSuccess = { fetchedDishes ->
                dishes = fetchedDishes
            },
            onFailure = {
                // Handle error
            }
        )
        clientOrderViewModel.listenToOrderUpdates(tableNumber.toInt(), restaurantName)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Table Number: $tableNumber")
        Text(text = "Restaurant Name: $restaurantName")

        Spacer(modifier = Modifier.height(16.dp))

        errorMessage?.let {
            Text(text = "Error: $it")
        }

        if (drinks.isNotEmpty() || dishes.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.fillMaxHeight(0.3f)
            ) {
                items(drinks) { drink ->
                    Button(onClick = { clientOrderViewModel.addDrinkToCurrentList(drink) }) {
                        Text(text = drink.name)
                    }
                }

                items(dishes) { dish ->
                    Button(onClick = { clientOrderViewModel.addDishToCurrentList(dish) }) {
                        Text(text = dish.name)
                    }
                }
            }
        } else {
            Text(text = "No drinks or dishes available")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Current Order")
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxHeight(0.3f)
        ) {
            items(currentOrderList) { orderItem ->
                Text(text = "${orderItem.name} - ${orderItem.price}€ | x${orderItem.quantity}")
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Total: ${currentOrderPrice}€")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Total Order")
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxHeight(0.3f)
        ) {
            items(totalOrderList) { orderItem ->
                Text(text = "${orderItem.name} - ${orderItem.price}€ | x${orderItem.quantity}")
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Total Order Price: ${totalOrderPrice}€")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { clientOrderViewModel.clearCurrentOrderList() }) {
            Text(text = "Eliminar Comanda Actual")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { clientOrderViewModel.removeLastItemFromCurrentOrder() }) {
            Text(text = "Eliminar Último Item Añadido")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            clientOrderViewModel.sendOrderToServer(
                tableNumber.toInt(),
                restaurantName,
                tableCode
            )
        }) {
            Text(text = "Confirmar Comanda")
        }
    }
}
