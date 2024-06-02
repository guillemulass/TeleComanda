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
    val restaurantEmail = remember { mutableStateOf<String?>(null) }
    var drinks by remember { mutableStateOf(listOf<Drink>()) }
    var dishes by remember { mutableStateOf(listOf<Dish>()) }
    val orderList by clientOrderViewModel.orderList.collectAsState()
    val totalPrice by clientOrderViewModel.totalPrice.collectAsState()
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
                modifier = Modifier.fillMaxHeight(0.5f)
            ) {
                items(drinks) { drink ->
                    Button(onClick = { clientOrderViewModel.addDrinkToList(drink) }) {
                        Text(text = drink.name)
                    }
                }

                items(dishes) { dish ->
                    Button(onClick = { clientOrderViewModel.addDishToList(dish) }) {
                        Text(text = dish.name)
                    }
                }
            }
        } else {
            Text(text = "No drinks or dishes available")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxHeight(0.5f)
        ) {
            items(orderList) { orderItem ->
                Text(text = "${orderItem.name} - ${orderItem.price}€ | x${orderItem.quantity}")
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Total: ${totalPrice}€")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            restaurantEmail.value?.let {
                clientOrderViewModel.sendOrderToServer(
                    tableNumber.toInt(),
                    restaurantName,
                    tableCode)
            }
        }) {
            Text(text = "Confirmar Comanda")
        }
    }
}
