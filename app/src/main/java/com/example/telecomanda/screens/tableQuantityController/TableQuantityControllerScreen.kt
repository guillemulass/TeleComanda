package com.example.telecomanda.screens.tableQuantityController

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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

@Composable
fun TableQuantityControllerScreen(
    navController: NavHostController,
    tableQuantityControllerViewModel: TableQuantityControllerViewModel = viewModel()
) {
    val tableQuantity by tableQuantityControllerViewModel.tableQuantity.observeAsState(0)
    var tableNumberToDelete by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        tableQuantityControllerViewModel.getTableQuantity { quantity ->
            tableQuantityControllerViewModel.tableQuantity.value = quantity
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Cantidad de Mesas: $tableQuantity",
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 30.sp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                tableQuantityControllerViewModel.editTableQuantity { newQuantity ->
                    tableQuantityControllerViewModel.tableQuantity.value = newQuantity
                }
            },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            Text(text = "Agregar Mesa")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                tableQuantityControllerViewModel.deleteLastTable { newQuantity ->
                    tableQuantityControllerViewModel.tableQuantity.value = newQuantity
                }
            },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            Text(text = "Eliminar Última Mesa")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = tableNumberToDelete,
            onValueChange = { tableNumberToDelete = it },
            label = { Text("Número de Mesa a Eliminar") },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val tableNumber = tableNumberToDelete.toIntOrNull()
                if (tableNumber != null) {
                    tableQuantityControllerViewModel.deleteTable(tableNumber) { newQuantity ->
                        tableQuantityControllerViewModel.tableQuantity.value = newQuantity
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            Text(text = "Eliminar Mesa")
        }

        Button(
            onClick = {
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            Text(text = "Volver")
        }
    }
}
