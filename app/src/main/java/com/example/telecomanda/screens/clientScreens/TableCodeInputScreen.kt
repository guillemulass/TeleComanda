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
import androidx.compose.runtime.livedata.observeAsState

@Composable
fun TableCodeInputScreen(
    navController: NavHostController,
    tableCodeInputViewModel: TableCodeInputViewModel = viewModel()
) {
    val isValidTable by tableCodeInputViewModel.isValidTable.observeAsState(false)
    val invalidCodeMessage = remember { mutableStateOf("") }

    Box (
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
                value = tableCodeInputViewModel.restaurantEmail,
                onValueChange = { tableCodeInputViewModel.updateRestaurantEmail(it) },
                label = { Text("Correo del Restaurante") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    tableCodeInputViewModel.checkTableCode(
                        onSuccess = {
                            navController.navigate("clientOrderScreen/${tableCodeInputViewModel.tableCode}/${tableCodeInputViewModel.restaurantEmail}")
                        },
                        onFailure = {
                            invalidCodeMessage.value = "Código o correo inválido"
                        }
                    )
                },
                modifier = Modifier
            ) {
                Text(text = "Aceptar")
            }

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
