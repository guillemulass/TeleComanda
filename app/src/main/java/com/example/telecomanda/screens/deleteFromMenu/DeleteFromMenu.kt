package com.example.telecomanda.screens.deleteFromMenu

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.telecomanda.dataClasses.Dish
import com.example.telecomanda.dataClasses.Drink
import com.example.telecomanda.footer.Footer
import com.example.telecomanda.header.Header
import com.example.telecomanda.logo.Logo
import kotlinx.coroutines.launch

@Composable
fun DeleteFromMenuScreen(
    navController: NavHostController,
    deleteFromMenuViewModel: DeleteFromMenuViewModel = viewModel()
) {
    var dishList by remember { mutableStateOf(emptyList<Dish>()) }
    var drinkList by remember { mutableStateOf(emptyList<Drink>()) }
    var showDialog by remember { mutableStateOf(false) }
    var itemToDelete by remember { mutableStateOf<Pair<String, String>?>(null) } // Pair(type, name)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        deleteFromMenuViewModel.getDishData(
            onSuccess = { dishes ->
                dishList = dishes
            },
            onFailure = {
                println(it)
            }
        )
        deleteFromMenuViewModel.getDrinkData(
            onSuccess = { drinks ->
                drinkList = drinks
            },
            onFailure = {
                println(it)
            }
        )
    }

    if (showDialog && itemToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Confirmar eliminación") },
            text = { Text("¿Estás seguro de que quieres eliminar este elemento de la carta? Esta acción es irreversible.") },
            confirmButton = {
                TextButton(onClick = {
                    coroutineScope.launch {
                        deleteFromMenuViewModel.deleteItemFromMenu(
                            itemToDelete!!.first,
                            itemToDelete!!.second
                        )
                        // Refresh the screen
                        deleteFromMenuViewModel.getDishData(
                            onSuccess = { dishes ->
                                dishList = dishes
                            },
                            onFailure = {
                                println(it)
                            }
                        )
                        deleteFromMenuViewModel.getDrinkData(
                            onSuccess = { drinks ->
                                drinkList = drinks
                            },
                            onFailure = {
                                println(it)
                            }
                        )
                        showDialog = false
                    }
                }) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF161618))
    ) {

        Box(
            modifier = Modifier
                .background(Color(0xFF161618))
                .padding(top = 35.dp)

        ) {

            Header(
                modifier = Modifier
                    .width(450.dp)
                    .height(60.dp),
                onClick = { navController.popBackStack() }
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(ScrollState(0))
                    .padding(top = 35.dp)
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                Logo(
                    modifier = Modifier
                        .width(136.dp)
                        .height(159.dp)
                )
            }

        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(ScrollState(0))
                .padding(top = 270.dp)
        ) {

            Spacer(modifier = Modifier.height(32.dp))

            dishList.forEach { dish ->
                Text(text = "${dish.name} - ${dish.price} - ${dish.type}\n${dish.ingredients}")
                Button(onClick = {
                    itemToDelete = "Dish" to dish.name
                    showDialog = true
                }) {
                    Text(text = "Eliminar")
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            drinkList.forEach { drink ->
                Text(text = "${drink.name} - ${drink.price} - ${drink.type}")
                Button(onClick = {
                    itemToDelete = "Drink" to drink.name
                    showDialog = true
                }) {
                    Text(text = "Eliminar")
                }
                Spacer(modifier = Modifier.height(8.dp))
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
                    .background(Color(0xFF161618))
            )
            Spacer(modifier = Modifier.height(16.dp).fillMaxWidth().background(Color(0xFF161618)))
        }
    }
}
