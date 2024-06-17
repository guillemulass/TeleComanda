package com.example.telecomanda.screens.deleteFromMenu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.telecomanda.dataClasses.Dish
import com.example.telecomanda.dataClasses.Drink
import com.example.telecomanda.deleteitemcard.DeleteItemCard
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
    var filteredDishList by remember { mutableStateOf(emptyList<Dish>()) }
    var filteredDrinkList by remember { mutableStateOf(emptyList<Drink>()) }
    var showDialog by remember { mutableStateOf(false) }
    var itemToDelete by remember { mutableStateOf<Pair<String, String>?>(null) }
    val coroutineScope = rememberCoroutineScope()
    var searchText by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        deleteFromMenuViewModel.getDishData(
            onSuccess = { dishes ->
                dishList = dishes
                filteredDishList = dishes
            },
            onFailure = {
                println(it)
            }
        )
        deleteFromMenuViewModel.getDrinkData(
            onSuccess = { drinks ->
                drinkList = drinks
                filteredDrinkList = drinks
            },
            onFailure = {
                println(it)
            }
        )
    }

    LaunchedEffect(searchText) {
        filteredDishList = dishList.filter { it.name.contains(searchText, ignoreCase = true) }
        filteredDrinkList = drinkList.filter { it.name.contains(searchText, ignoreCase = true) }
    }

    if (showDialog && itemToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(
                text = "Confirmar eliminación",
                style = TextStyle(fontSize = 30.sp)

            ) },
            text = {
                Text("¿Estás seguro de que quieres eliminar este elemento de la carta? Esta acción es irreversible.",
                        style = TextStyle(fontSize = 20.sp)

                ) },
            confirmButton = {
                TextButton(onClick = {
                    coroutineScope.launch {
                        deleteFromMenuViewModel.deleteItemFromMenu(
                            itemToDelete!!.first,
                            itemToDelete!!.second
                        )
                        deleteFromMenuViewModel.getDishData(
                            onSuccess = { dishes ->
                                dishList = dishes
                                filteredDishList = dishes.filter { it.name.contains(searchText, ignoreCase = true) }
                            },
                            onFailure = {
                                println(it)
                            }
                        )
                        deleteFromMenuViewModel.getDrinkData(
                            onSuccess = { drinks ->
                                drinkList = drinks
                                filteredDrinkList = drinks.filter { it.name.contains(searchText, ignoreCase = true) }
                            },
                            onFailure = {
                                println(it)
                            }
                        )
                        showDialog = false
                    }
                }) {
                    Text(
                        "Confirmar",
                        style = TextStyle(fontSize = 24.sp)
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(
                        "Cancelar",
                        style = TextStyle(fontSize = 24.sp)

                    )
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF161618))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxSize()
        ) {
            Header(
                modifier = Modifier
                    .width(450.dp)
                    .height(60.dp),
                onClick = { navController.popBackStack() }
            )

            Spacer(modifier = Modifier.height(32.dp))

            Logo(
                modifier = Modifier
                    .width(136.dp)
                    .height(159.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            TextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                placeholder = { Text("Buscar...") },
                textStyle = TextStyle(color = Color.White),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFD9D9D9),
                    unfocusedBorderColor = Color(0xFFD9D9D9),
                    focusedLabelColor = Color(0xFFD9D9D9),
                    unfocusedLabelColor = Color(0xFFD9D9D9),
                    unfocusedTextColor = Color(0xFFD9D9D9),
                    focusedTextColor = Color(0xFFD9D9D9)
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(filteredDishList.size) { index ->
                    val dish = filteredDishList[index]
                    DeleteItemCard(
                        modifier = Modifier
                            .width(167.dp)
                            .height(254.dp)
                            .padding(8.dp),
                        itemName = dish.name,
                        itemPrice = "${dish.price}€",
                        onClickDelete = {
                            itemToDelete = "Dish" to dish.name
                            showDialog = true
                        }
                    )
                }

                items(filteredDrinkList.size) { index ->
                    val drink = filteredDrinkList[index]
                    DeleteItemCard(
                        modifier = Modifier
                            .width(167.dp)
                            .height(254.dp)
                            .padding(8.dp),
                        itemName = drink.name,
                        itemPrice = "${drink.price}€",
                        onClickDelete = {
                            itemToDelete = "Drink" to drink.name
                            showDialog = true
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(70.dp))

            Footer(
                modifier = Modifier
                    .width(430.dp)
                    .height(54.dp)
                    .background(Color(0xFF161618))
            )

            Spacer(
                modifier = Modifier
                    .height(16.dp)
                    .fillMaxWidth()
                    .background(Color(0xFF161618))
            )
        }
    }
}
