package com.example.telecomanda.screens.registerScreen

import android.widget.Toast
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.telecomanda.routes.Routes

@Composable
fun RegisterScreen(
    navController: NavHostController,
    registerScreenViewModel: RegisterScreenViewModel
) {

    var password by remember { mutableStateOf("") }
    var passwordConfirmation by remember { mutableStateOf("") }
    var warningText by remember { mutableStateOf("") }
    val context = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(
                    ScrollState(10000),
                    enabled = true,
                    reverseScrolling = true
                )
                .padding(
                    top = 35.dp
                )
        ) {

            Text(
                text = "TeleComanda",
                style = TextStyle(
                    fontWeight = Bold,
                    fontSize = 40.sp
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Crear Restaurante",
                style = TextStyle(
                    fontWeight = Bold,
                    fontSize = 30.sp
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            TextField(
                value = registerScreenViewModel.restaurantName,
                onValueChange = { registerScreenViewModel.changeRestaurantName(it) },
                label = { Text("Nombre del Restaurante") },
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = registerScreenViewModel.restaurantEmail,
                onValueChange = { registerScreenViewModel.changeEmail(it) },
                label = { Text("Email del Restaurante") },
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = password,
                onValueChange = { newText ->
                    password = newText
                },
                label = { Text("Contraseña del Restaurante") },
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = passwordConfirmation,
                onValueChange = { newText ->
                    passwordConfirmation = newText
                },
                label = { Text("Confirmar Contraseña") },
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (warningText.isNotEmpty()) {
                Text(text = warningText)
            }

            if (registerScreenViewModel.statusText.isNotEmpty()) {
                Text(text = registerScreenViewModel.statusText)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (password.length >= 6 || passwordConfirmation.length >= 6) {
                        if (password == passwordConfirmation) {
                            registerScreenViewModel.changePassw(passwordConfirmation)
                            warningText = ""
                            registerScreenViewModel.registerRestaurant(
                                onSuccess = { navController.navigate(Routes.ConfigurationScreenRoute.route) },
                                onFailure = { Toast.makeText(context, "Error al crear la cuenta, intentelo de nuevo", Toast.LENGTH_SHORT).show() }
                            )
                        } else {
                            warningText = "Las contraseñas no coinciden"
                        }
                    } else {
                        warningText = "La contraseña debe tener al menos 6 caracteres"
                    }
                },
                modifier = Modifier
            ) {
                Text(text = "Continuar")
            }

            Spacer(modifier = Modifier.height(16.dp))


        }
    }
}
