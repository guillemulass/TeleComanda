package com.example.telecomanda.screens.logIn

import androidx.compose.foundation.ScrollState
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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.telecomanda.routes.Routes

@Composable
fun LogInEmployee(
    navController: NavHostController,
    logInEmployeeViewModel: LogInViewModel
) {

    var text by remember { mutableStateOf("") }

    Box (
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
                    fontSize = 40.sp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Iniciar Sesion",
                style = TextStyle(
                    fontWeight = Bold,
                    fontSize = 30.sp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            TextField (
                value = logInEmployeeViewModel.email,
                onValueChange = { logInEmployeeViewModel.changeEmail(it) },
                label = { Text("Email") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier
                    .width(330.dp)
                    .padding(top = 55.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFD9D9D9),
                    unfocusedBorderColor = Color(0xFFD9D9D9),
                    focusedLabelColor = Color(0xFFD9D9D9),
                    unfocusedLabelColor = Color(0xFFD9D9D9),
                    unfocusedTextColor = Color(0xFFD9D9D9),
                    focusedTextColor = Color(0xFFD9D9D9)
                ),
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Email, contentDescription = null)
                },
                singleLine = true

            )

            TextField(
                value = logInEmployeeViewModel.password,
                onValueChange = { logInEmployeeViewModel.changePassword(it) },
                label = { Text("Contraseña") },
                modifier = Modifier
                    .width(330.dp)
                    .padding(top = 30.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFD9D9D9),
                    unfocusedBorderColor = Color(0xFFD9D9D9),
                    focusedLabelColor = Color(0xFFD9D9D9),
                    unfocusedLabelColor = Color(0xFFD9D9D9),
                    unfocusedTextColor = Color(0xFFD9D9D9),
                    focusedTextColor = Color(0xFFD9D9D9)
                ),
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Lock, contentDescription = null)
                },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(60.dp))


            Button(
                onClick = {
                    logInEmployeeViewModel.login { navController.navigate(Routes.EmployeeWorkScreenRoute.route) }
                },
                modifier = Modifier
            ) {
                Text(text = "Iniciar Sesion")
            }


        }
    }

}