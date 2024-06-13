package com.example.telecomanda.screens.addEmployee

import android.widget.Toast
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.telecomanda.botonbig32sp.BotonBig32sp
import com.example.telecomanda.footer.Footer
import com.example.telecomanda.header.Header
import com.example.telecomanda.logo.Logo
import com.example.telecomanda.routes.Routes

@Composable
fun AddEmployee(
    navController: NavHostController,
    addEmployeeViewModel: AddEmployeeViewModel
) {

    var password by remember { mutableStateOf("") }
    var passwordConfirmation by remember { mutableStateOf("") }
    var warningText by remember { mutableStateOf("") }
    val context = LocalContext.current

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

            Header(
                modifier = Modifier
                    .width(430.dp)
                    .height(60.dp)
                    .background(Color(0xFF161618))
                ,
                onClick = { navController.popBackStack() }
            )

            Spacer(modifier = Modifier.height(32.dp))

            Logo(
                modifier = Modifier
                    .width(169.dp)
                    .height(197.dp)
            )

            TextField(
                value = addEmployeeViewModel.restaurantName,
                onValueChange = { addEmployeeViewModel.changeRestaurantName(it) },
                label = { Text("Nombre del Restaurante") },
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

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = addEmployeeViewModel.restaurantUID,
                onValueChange = { addEmployeeViewModel.changeRestaurantUID(it) },
                label = { Text("ID del Restaurante") },
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
                    Icon(imageVector = Icons.Default.Info, contentDescription = null)
                },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = addEmployeeViewModel.employeeEmail,
                onValueChange = { addEmployeeViewModel.changeEmail(it) },
                label = { Text("Email del usuario") },
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
                    Icon(imageVector = Icons.Default.Email, contentDescription = null)
                },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = password,
                onValueChange = { newText ->
                    password = newText
                },
                label = { Text("Contraseña") },
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
                    Icon(imageVector = Icons.Default.Lock, contentDescription = null)
                },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = passwordConfirmation,
                onValueChange = { newText ->
                    passwordConfirmation = newText
                },
                label = { Text("Confirmar Contraseña") },
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
                    Icon(imageVector = Icons.Default.Lock, contentDescription = null)
                },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true
            )


            if (warningText.isNotEmpty()) {
                Text(
                    text = warningText,
                    style = TextStyle(color = Color.Red, fontSize = 24.sp),
                )
            }

            if (addEmployeeViewModel.statusText.isNotEmpty()) {
                Text(
                    text = addEmployeeViewModel.statusText,
                    style = TextStyle(color = Color.Red, fontSize = 24.sp),
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            BotonBig32sp(
                onClick = {
                    if (password.length >= 6 || passwordConfirmation.length >= 6) {
                        if (password == passwordConfirmation) {
                            addEmployeeViewModel.changePassw(passwordConfirmation)
                            warningText = ""
                            addEmployeeViewModel.registerEmployee(
                                onSuccess = { navController.navigate(Routes.EmployeeWorkScreenRoute.route) },
                                onFailure = { Toast.makeText(context, "Error al crear la cuenta, intentelo de nuevo", Toast.LENGTH_SHORT).show() }
                            )
                        } else {
                            warningText = "Las contraseñas no coinciden"
                        }
                    } else {
                        warningText = "La contraseña debe tener al menos 6 caracteres"
                    }
                },
                text = "Continuar"
            )

            Spacer(modifier = Modifier.height(77.dp))

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