package com.example.telecomanda.screens.employeeScreens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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

@Composable
fun EmployeeTableSelectionScreen(
    navController: NavHostController,
    addOrderViewModel: EmployeeAddOrderViewModel
) {
    val tables by addOrderViewModel.tableList.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        addOrderViewModel.fetchTables()
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Seleccionar Mesa",
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 30.sp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            items(tables) { table ->
                Button(
                    onClick = {
                        navController.navigate("employeeAddOrder/${table.number}")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(text = "Mesa ${table.number} - Código: ${table.code}")
                }
            }
        }
    }
}
