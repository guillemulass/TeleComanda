package com.example.telecomanda.screens.clientScreens

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.telecomanda.botonbig24sp.BotonBig24sp
import com.example.telecomanda.footer.Footer
import com.example.telecomanda.header.Header
import com.example.telecomanda.logo.Logo

@Composable
fun TableCodeInputScreen(
    navController: NavHostController,
    tableCodeInputViewModel: TableCodeInputViewModel = viewModel()
) {
    val invalidCodeMessage = remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF161618))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(ScrollState(0))
                .padding(top = 35.dp)
        ) {

            Box{
                Header(
                    modifier = Modifier
                        .width(450.dp)
                        .height(60.dp),
                    onClick = {navController.popBackStack()}
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Logo(
                modifier = Modifier
                    .width(199.dp)
                    .height(232.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            TextField (
                value = tableCodeInputViewModel.tableCode,
                onValueChange = { tableCodeInputViewModel.updateTableCode(it) },
                label = { Text("Código de Mesa") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier
                    .width(330.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFD9D9D9),
                    unfocusedBorderColor = Color(0xFFD9D9D9),
                    focusedLabelColor = Color(0xFFD9D9D9),
                    unfocusedLabelColor = Color(0xFFD9D9D9),
                    unfocusedTextColor = Color(0xFFD9D9D9),
                    focusedTextColor = Color(0xFFD9D9D9)
                ),
                leadingIcon = {
                    Icon(imageVector = Icons.Default.LocationOn, contentDescription = null)
                },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField (
                value = tableCodeInputViewModel.restaurantName,
                onValueChange = { tableCodeInputViewModel.updateRestaurantName(it) },
                label = { Text("Nombre del Restaurante") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier
                    .width(330.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFD9D9D9),
                    unfocusedBorderColor = Color(0xFFD9D9D9),
                    focusedLabelColor = Color(0xFFD9D9D9),
                    unfocusedLabelColor = Color(0xFFD9D9D9),
                    unfocusedTextColor = Color(0xFFD9D9D9),
                    focusedTextColor = Color(0xFFD9D9D9)
                ),
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Home, contentDescription = null)
                },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(32.dp))

            BotonBig24sp(
                onClick = {
                    tableCodeInputViewModel.checkTableCode(
                        onSuccess = {
                            tableCodeInputViewModel.getTableNumberByCode(
                                tableCode = tableCodeInputViewModel.tableCode,
                                onSuccess = { tableNumber ->
                                    if (tableNumber != null) {
                                        tableCodeInputViewModel.updateTableNumber(tableNumber.toString())
                                        navController.navigate("clientOrderScreen/${tableCodeInputViewModel.tableNumber}/${tableCodeInputViewModel.restaurantName}/${tableCodeInputViewModel.tableCode}")
                                    } else {
                                        invalidCodeMessage.value = "Código de mesa inválido"
                                    }
                                },
                                onFailure = {
                                    println("Error al verificar el código de mesa")
                                    invalidCodeMessage.value = "Error al verificar el código de mesa"
                                }
                            )
                        },
                        onFailure = {
                            println("Error al verificar el nombre del restaurante, intentelo de nuevo")
                            invalidCodeMessage.value = "Error al verificar el nombre del restaurante, intentelo de nuevo"
                        }
                    )
                },
                text = "Continuar",
                modifier = Modifier.width(266.dp).height(52.dp)
            )

            if (invalidCodeMessage.value.isNotEmpty()) {
                Text(
                    text = invalidCodeMessage.value,
                    style = TextStyle(color = Color.Red, fontSize = 16.sp),
                    modifier = Modifier.padding(top = 8.dp)
                )
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
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
