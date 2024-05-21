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
import androidx.navigation.NavHostController
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext

@Composable
fun TableQuantityControllerScreen(
    navController: NavHostController,
    tableQuantityControllerViewModel: TableQuantityControllerViewModel
) {
    val context = LocalContext.current
    var tableQuantity by remember { mutableStateOf(0) }
    val stateText by tableQuantityControllerViewModel.stateText.observeAsState("")

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text(
            text = "Seleccionar Mesa",
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 30.sp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            tableQuantityControllerViewModel.editTableQuantity { newQuantity ->
                tableQuantity = newQuantity
            }
            tableQuantityControllerViewModel.editStateText("Añadida la mesa $tableQuantity")
        }) {
            Text("Añadir nueva mesa")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stateText,
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 30.sp)
        )
    }
}
