package com.example.telecomanda.screens.menuScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.telecomanda.dataClasses.Dish
import com.example.telecomanda.dataClasses.Drink
import com.example.telecomanda.enumClass.DishTypes
import com.example.telecomanda.enumClass.DrinkTypes
import com.example.telecomanda.footer.Footer
import com.example.telecomanda.header.Header
import com.example.telecomanda.menuitemshow.MenuItemShow
import com.example.telecomanda.smallbutton.SmallButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    navController: NavHostController,
    menuScreenViewModel: MenuScreenViewModel
) {
    val auth: FirebaseAuth = Firebase.auth
    var dishList by remember { mutableStateOf(emptyList<Dish>()) }
    var drinkList by remember { mutableStateOf(emptyList<Drink>()) }

    var selectedCategory by remember { mutableStateOf("Platos") }
    var selectedType by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            menuScreenViewModel.getDishData(
                onSuccess = { dishes ->
                    dishList = dishes
                },
                onFailure = {
                    println(it)
                }
            )
            menuScreenViewModel.getDrinkData(
                onSuccess = { drinks ->
                    drinkList = drinks
                },
                onFailure = {
                    println(it)
                }
            )
        }
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
                LazyRow {
                    item {
                        SmallButton(
                            onClick = { selectedCategory = "Platos" },
                            text = "Platos"
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.width(16.dp))
                    }

                    item {
                        SmallButton(
                            onClick = { selectedCategory = "Bebidas" },
                            text = "Bebidas"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Dropdown menu for selecting type
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
                        modifier = Modifier
                            .fillMaxWidth()
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
            }

            val filteredDishes = dishList.filter { it.type.contains(selectedType, ignoreCase = true) }
            val filteredDrinks = drinkList.filter { it.type.contains(selectedType, ignoreCase = true) }

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
            Spacer(modifier = Modifier.height(8.dp))
            val ingredientsText = dish.ingredients.joinToString(", ")

            if (imageUrl.isNullOrEmpty()) {
                MenuItemShow(
                    itemNameText = dish.name,
                    itemPriceText = "${dish.price}€",
                    itemIngredientsText = ingredientsText,
                    modifier = Modifier
                        .width(305.dp)
                        .height(156.dp)
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
                        .height(156.dp)
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
            Spacer(modifier = Modifier.height(8.dp))
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
