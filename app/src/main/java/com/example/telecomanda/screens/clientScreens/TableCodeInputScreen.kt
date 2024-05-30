package com.example.telecomanda.screens.clientScreens

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController

@Composable
fun TableCodeInputScreen(
    navController: NavHostController,
    tableCodeInputViewModel: TableCodeInputViewModel = viewModel()
) {
    val invalidCodeMessage = remember { mutableStateOf("") }
    val msg = remember { mutableStateOf("") }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(ScrollState(10000), enabled = true, reverseScrolling = true)
                .padding(top = 35.dp)
        ) {

            Text(
                text = "TeleComanda",
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 40.sp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            TextField(
                value = tableCodeInputViewModel.tableCode,
                onValueChange = { tableCodeInputViewModel.updateTableCode(it) },
                label = { Text("Código de Mesa") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = tableCodeInputViewModel.restaurantName,
                onValueChange = { tableCodeInputViewModel.updateRestaurantName(it) },
                label = { Text("Nombre del Restaurante") }
            )

            Spacer(modifier = Modifier.height(16.dp))


            Button(
                onClick = {
                    tableCodeInputViewModel.checkTableCode(
                        onSuccess = {
                            tableCodeInputViewModel.getRestaurantEmailByName(
                                restaurantName = tableCodeInputViewModel.restaurantName,
                                onSuccess = { email ->
                                    if (email != null) {
                                        tableCodeInputViewModel.getTableNumberByCode(
                                            restaurantEmail = email,
                                            tableCode = tableCodeInputViewModel.tableCode,
                                            onSuccess = { tableNumber ->
                                                if (tableNumber != null) {
                                                    msg.value = "tableNumber $tableNumber\n" +
                                                            "restaurantName ${tableCodeInputViewModel.restaurantName}"
                                                    //navController.navigate("clientOrderScreen/$tableNumber/${tableCodeInputViewModel.restaurantName}")
                                                } else {
                                                    invalidCodeMessage.value = "Código de mesa inválido"
                                                }
                                            },
                                            onFailure = {
                                                invalidCodeMessage.value = "Error al verificar el código de mesa"
                                            }
                                        )
                                    } else {
                                        invalidCodeMessage.value = "Nombre de restaurante no encontrado"
                                    }
                                },
                                onFailure = {
                                    invalidCodeMessage.value = "Error al verificar el nombre del restaurante, intentelo de nuevo"
                                }
                            )
                        },
                        onFailure = {
                            invalidCodeMessage.value = "Error al verificar el nombre del restaurante, intentelo de nuevo"
                        }

                    )
                },
                modifier = Modifier
            ) {
                Text(text = "Aceptar")
            }

            Button(onClick = {
                tableCodeInputViewModel.getRestaurantEmailByName(
                    restaurantName = tableCodeInputViewModel.restaurantName,
                    onSuccess = {
                        tableCodeInputViewModel.updateRestaurantEmail(it!!)
                    },
                    onFailure = {}
                )

                var tableNum = ""

                tableCodeInputViewModel.getTableNumberr(tableCodeInputViewModel.restaurantEmail_, tableCodeInputViewModel.tableCode) { tableNumber ->
                    if (tableNumber != null) {
                        tableNum = tableNumber
                        println("Table number: $tableNumber")
                    } else {
                        println("Table not found")
                    }
                }

                msg.value = "Succes\n" +
                        "restaurantName ${tableCodeInputViewModel.restaurantName}\n" +
                        "restaurantEmail ${tableCodeInputViewModel.restaurantEmail_}\n" +
                        "tableCode ${tableCodeInputViewModel.tableCode}\n" +
                        "tableNum = $tableNum \n" +
                        "tableNum = ${tableCodeInputViewModel.tableNumber.value}"
                println(msg.value)
            }
            ) {
                Text(text = "Prueba")
            }

            Text(
                text = msg.value,
                style = TextStyle(color = androidx.compose.ui.graphics.Color.Red, fontSize = 16.sp),
                modifier = Modifier.padding(top = 8.dp)
            )

            if (invalidCodeMessage.value.isNotEmpty()) {
                Text(
                    text = invalidCodeMessage.value,
                    style = TextStyle(color = androidx.compose.ui.graphics.Color.Red, fontSize = 16.sp),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}
