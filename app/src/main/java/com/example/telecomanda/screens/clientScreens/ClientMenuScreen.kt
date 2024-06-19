package com.example.telecomanda.screens.clientScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.telecomanda.buttonsmallmenu.ButtonSmallMenu
import com.example.telecomanda.dataClasses.Dish
import com.example.telecomanda.dataClasses.Drink
import com.example.telecomanda.enumClass.DishTypes
import com.example.telecomanda.enumClass.DrinkTypes
import com.example.telecomanda.footer.Footer
import com.example.telecomanda.header.Header
import com.example.telecomanda.menuitemshow.MenuItemShow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientMenuScreen(
    navController: NavHostController,
    clientOrderViewModel: ClientOrderViewModel,
    restaurantName: String
) {
    var dishList by remember { mutableStateOf(emptyList<Dish>()) }
    var drinkList by remember { mutableStateOf(emptyList<Drink>()) }

    var selectedCategory by remember { mutableStateOf("Platos") }
    var selectedType by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

    val filteredDishes = dishList.filter { it.type.contains(selectedType, ignoreCase = true) && it.name.contains(searchText, ignoreCase = true) }
    val filteredDrinks = drinkList.filter { it.type.contains(selectedType, ignoreCase = true) && it.name.contains(searchText, ignoreCase = true) }

    LaunchedEffect(restaurantName) {
        clientOrderViewModel.getDishData(
            restaurantName,
            onSuccess = { dishes ->
                dishList = dishes
            },
            onFailure = {
                println(it)
            }
        )
        clientOrderViewModel.getDrinkData(
            restaurantName,
            onSuccess = { drinks ->
                drinkList = drinks
            },
            onFailure = {
                println(it)
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF161618))
            .padding(top = 35.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            Header(
                modifier = Modifier
                    .width(430.dp)
                    .height(60.dp)
                    .background(Color(0xFF161618)),
                onClick = { navController.popBackStack() }
            )
        }

        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 70.dp, bottom = 70.dp)
        ) {

            item {
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow {
                    item {
                        ButtonSmallMenu(
                            onClick = { selectedCategory = "Platos" },
                            text = "Platos",
                            textColor = if (selectedCategory == "Platos") Color.White else Color.Gray
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.width(16.dp))
                    }

                    item {
                        ButtonSmallMenu(
                            onClick = { selectedCategory = "Bebidas" },
                            text = "Bebidas",
                            textColor = if (selectedCategory == "Bebidas") Color.White else Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                var selectedText by remember { mutableStateOf("") }
                val types = if (selectedCategory == "Platos") DishTypes.values() else DrinkTypes.values()

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    TextField(
                        value = selectedText,
                        onValueChange = { selectedText = it },
                        label = { Text("Tipo") },
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFD9D9D9),
                            unfocusedBorderColor = Color(0xFFD9D9D9),
                            focusedLabelColor = Color(0xFFD9D9D9),
                            unfocusedLabelColor = Color(0xFFD9D9D9),
                            unfocusedTextColor = Color(0xFFD9D9D9),
                            focusedTextColor = Color(0xFFD9D9D9)
                        ),
                        modifier = Modifier
                            .width(330.dp)
                            .padding(horizontal = 16.dp)
                            .menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        types.forEach { type ->
                            DropdownMenuItem(
                                text = { Text(type.name) },
                                onClick = {
                                    selectedText = type.name
                                    selectedType = type.name
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // TextField para la búsqueda
                TextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    label = { Text(text = "Buscar...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
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
            }

            if (selectedCategory == "Platos") {
                items(filteredDishes) { dish ->
                    DishItem(dish)
                }
            } else {
                items(filteredDrinks) { drink ->
                    DrinkItem(drink)
                }
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

@Composable
fun DishItem(dish: Dish) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        dish.imageUrl.let { imageUrl ->
            Spacer(modifier = Modifier.height(16.dp))
            val ingredientsText = dish.ingredients.joinToString(", ")

            if (imageUrl.isNullOrEmpty()) {
                MenuItemShow(
                    itemNameText = dish.name,
                    itemPriceText = "${dish.price}€",
                    itemIngredientsText = ingredientsText,
                    modifier = Modifier
                        .width(305.dp)
                )
            } else {
                val painter = rememberAsyncImagePainter(model = imageUrl)
                MenuItemShow(
                    itemImg = painter,
                    itemNameText = dish.name,
                    itemPriceText = "${dish.price}€",
                    itemIngredientsText = ingredientsText,
                    modifier = Modifier
                        .width(305.dp)
                )
            }
        }
    }
}

@Composable
fun DrinkItem(drink: Drink) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        drink.imageUrl?.let { imageUrl ->
            Spacer(modifier = Modifier.height(16.dp))
            if (imageUrl.isNullOrEmpty()) {
                MenuItemShow(
                    itemNameText = drink.name,
                    itemPriceText = "${drink.price}€",
                    modifier = Modifier
                        .width(305.dp)
                        .height(156.dp)
                )
            } else {
                val painter = rememberAsyncImagePainter(model = imageUrl)
                MenuItemShow(
                    itemImg = painter,
                    itemNameText = drink.name,
                    itemPriceText = "${drink.price}€",
                    modifier = Modifier
                        .width(305.dp)
                        .height(156.dp)
                )
            }
        }
    }
}
