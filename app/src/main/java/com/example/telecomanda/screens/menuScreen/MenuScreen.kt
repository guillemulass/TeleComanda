package com.example.telecomanda.screens.menuScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.telecomanda.dataClasses.Dish
import com.example.telecomanda.dataClasses.Drink
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun MenuScreen(
    navController: NavHostController,
    menuScreenViewModel: MenuScreenViewModel
) {
    val auth: FirebaseAuth = Firebase.auth
    var dishList by remember { mutableStateOf(emptyList<Dish>()) }
    var drinkList by remember { mutableStateOf(emptyList<Drink>()) }

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
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 35.dp)
        ) {
            item {
                Text(
                    text = "TeleComanda",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 40.sp
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Crear Restaurante",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp
                    )
                )

                Spacer(modifier = Modifier.height(32.dp))
            }

            items(dishList) { dish ->
                DishItem(dish)
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            items(drinkList) { drink ->
                DrinkItem(drink)
            }
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
            .padding(16.dp)
    ) {
        Text(text = "${dish.name} - ${dish.price} - ${dish.type}\n${dish.ingredients}")

        dish.imageUrl?.let { imageUrl ->
            Spacer(modifier = Modifier.height(8.dp))
            Image(
                painter = rememberImagePainter(data = imageUrl),
                contentDescription = "Dish Image",
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
            )
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
            .padding(16.dp)
    ) {
        Text(text = "${drink.name} - ${drink.price} - ${drink.type}")

        drink.imageUrl?.let { imageUrl ->
            Spacer(modifier = Modifier.height(8.dp))
            Image(
                painter = rememberImagePainter(data = imageUrl),
                contentDescription = "Drink Image",
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
            )
        }
    }
}
