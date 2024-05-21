package com.example.telecomanda.screens.menuScreen

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

    // LaunchedEffect es como un init, al iniciarse la pantalla se ejecuta lo que haya dentro
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
                Text(text = "${dish.name} - ${dish.price} - ${dish.type}\n${dish.ingredients}")
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            items(drinkList) { drink ->
                Text(text = "${drink.name} - ${drink.price} - ${drink.type}")
            }
        }
    }
}
