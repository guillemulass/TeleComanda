package com.example.telecomanda.screens.clientScreens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ClientOrderScreen(
    tableCode: String,
    restaurantEmail: String
) {
    // Lógica para mostrar el menú y permitir pedidos
    Text(text = "Table Code: $tableCode, Restaurant Email: $restaurantEmail")
    // Aquí añadirás más lógica para mostrar el menú y permitir que el cliente haga pedidos.
}
