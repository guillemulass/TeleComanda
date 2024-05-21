package com.example.telecomanda.screens.tableQuantityController

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavHostController

@Composable
fun TableQuantityControllerScreen(
    navController: NavHostController,
    tableQuantityControllerViewModel: TableQuantityControllerViewModel
) {
    val tableQuantity by tableQuantityControllerViewModel.tableQuantity.observeAsState(0)

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
    }
}
